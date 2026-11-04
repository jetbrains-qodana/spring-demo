package com.example.springtest.messaging;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EventListener
@interface ComposedEventListener {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ComposedEventListener
@interface CommandEventListener {
}

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@KafkaListener
@interface ComposedKafkaListener {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ComposedKafkaListener
@interface CommandKafkaListener {
}

@RestController
@RequestMapping("/callbacks")
public class MessagingCallbacks {
  private final ApplicationEventPublisher publisher;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final RabbitTemplate rabbitTemplate;
  private final JmsTemplate jmsTemplate;

  public MessagingCallbacks(
    ApplicationEventPublisher publisher,
    KafkaTemplate<String, String> kafkaTemplate,
    RabbitTemplate rabbitTemplate,
    JmsTemplate jmsTemplate
  ) {
    this.publisher = publisher;
    this.kafkaTemplate = kafkaTemplate;
    this.rabbitTemplate = rabbitTemplate;
    this.jmsTemplate = jmsTemplate;
  }

  @GetMapping("/event")
  public void publishEvent(@RequestParam String command) { // Step 1/0
    publisher.publishEvent(new CommandEvent(command)); // Step 2/0
  }

  @GetMapping("/rabbit")
  public void publishRabbit(@RequestParam String command) { // Step 1/4
    rabbitTemplate.send("commands", command); // Step 2/4
  }

  @GetMapping("/jms")
  public void publishJms(@RequestParam String command) { // Step 1/5
    jmsTemplate.convertAndSend("commands", command); // Step 2/5
  }

  @GetMapping("/jms-unmatched")
  public void publishUnmatchedJms(@RequestParam String command) {
    // CONTROL: This destination has no listener.
    jmsTemplate.convertAndSend("other-commands", command);
  }

  @GetMapping("/kafka")
  public void publishKafka(@RequestParam String command) { // Step 1/6; Step 1/7
    kafkaTemplate.send("commands", command); // Step 2/6; Step 2/7
  }

  @GetMapping("/kafka-default")
  public void publishKafkaDefault(@RequestParam String command) { // Step 1/9; Step 1/10
    kafkaTemplate.send(command); // Step 2/9; Step 2/10
  }

  @GetMapping("/event-without-record")
  public void publishEventWithoutRecord() {
    // CONTROL: A constant event reaches listeners by kind, but it carries no request taint.
    publisher.publishEvent("application-started");
  }

  @GetMapping("/jms-from-static-factory")
  public void publishJmsFromStaticFactory() {
    // CONTROL: The static helper returns a publisher, but the message payload is a safe constant.
    JmsTemplate selected = select(jmsTemplate);
    selected.convertAndSend("commands", "scheduled-command");
  }

  public void publishKafkaAliasControl(String value) {
    // CONTROL: An ordinary publisher can use local aliases, but this value is not a request source.
    String payload = value;
    payload = value;
    kafkaTemplate.send("commands", payload);
  }

  @GetMapping("/kafka-control")
  public void configureKafka(@RequestParam String topic) {
    // CONTROL: Configuration does not publish the request value.
    kafkaTemplate.setDefaultTopic(topic);
  }

  @Service
  public static class EventConsumer {
    @CommandEventListener
    public void consumeEvent(CommandEvent event) throws IOException { // Step 3/0
      Runtime.getRuntime().exec(event.command()); // Inspection: java-basic-command-injection, taint.CMDi; Step 4/0
    }
  }

  @EventListener
  public void consumeOtherEvent(Other.CommandEvent event) throws IOException {
    // CONTROL: This event has the same short name but a different type.
    Runtime.getRuntime().exec(event.command());
  }

  @EventListener
  public void observeAnyEvent() {
    // CONTROL: A listener can react to an event without reading its payload.
  }

  @KafkaListener
  public void observeAnyKafka(String command) {
    // CONTROL: A listener without a destination can observe each Kafka destination.
  }

  @KafkaListener(topics = "commands")
  public void consumeKafka(String command) throws IOException { // Step 1/1; Step 3/6; Step 3/9
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/1; Step 4/6; Step 4/9
  }

  @CommandKafkaListener
  public void consumeAnyKafka(String command) throws IOException { // Step 1/8; Step 3/7; Step 3/10
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/8; Step 4/7; Step 4/10
  }

  @KafkaListener(topics = "commands")
  public void auditKafka(String command, int partition) {
    // CONTROL: A listener can receive a payload and transport metadata without using either value.
  }

  @KafkaListener(topics = "commands")
  public void countKafkaDelivery() {
    // CONTROL: A listener does not have to declare a payload parameter.
  }

  @RabbitListener(queues = {"audit", "commands"})
  public void consumeRabbit(String command) throws IOException { // Step 1/2; Step 3/4
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/2; Step 4/4
  }

  @JmsListener(destination = "commands")
  public void consumeJms(String command) throws IOException { // Step 1/3; Step 3/5
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/3; Step 4/5
  }

  public record CommandEvent(String command) {
  }

  public static class Other {
    public record CommandEvent(String command) {
    }
  }

  private static JmsTemplate select(JmsTemplate template) {
    return template;
  }
}
