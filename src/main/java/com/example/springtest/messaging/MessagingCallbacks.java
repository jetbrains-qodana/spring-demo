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
  public void publishEvent(@RequestParam String command) {
    publisher.publishEvent(new CommandEvent(command));
  }

  @GetMapping("/rabbit")
  public void publishRabbit(@RequestParam String command) {
    rabbitTemplate.convertAndSend("commands", command);
  }

  @GetMapping("/jms")
  public void publishJms(@RequestParam String command) {
    jmsTemplate.convertAndSend("commands", command);
  }

  @GetMapping("/jms-unmatched")
  public void publishUnmatchedJms(@RequestParam String command) {
    jmsTemplate.convertAndSend("other-commands", command);
  }

  @GetMapping("/kafka")
  public void publishKafka(@RequestParam String command) {
    kafkaTemplate.send("commands", command);
  }

  @GetMapping("/kafka-default")
  public void publishKafkaDefault(@RequestParam String command) {
    kafkaTemplate.send(command);
  }

  @GetMapping("/event-without-record")
  public void publishEventWithoutRecord() {
    publisher.publishEvent("application-started");
  }

  @GetMapping("/jms-from-static-factory")
  public void publishJmsFromStaticFactory() {
    JmsTemplate selected = select(jmsTemplate);
    selected.convertAndSend("commands", "scheduled-command");
  }

  public void publishKafkaAliasControl(String value) {
    String payload = value;
    payload = value;
    kafkaTemplate.send("commands", payload);
  }

  @GetMapping("/kafka-control")
  public void configureKafka(@RequestParam String topic) {
    kafkaTemplate.setDefaultTopic(topic);
  }

  @Service
  public static class EventConsumer {
    @CommandEventListener
    public void consumeEvent(CommandEvent event) throws IOException {
      Runtime.getRuntime().exec(event.command());
    }
  }

  @EventListener
  public void consumeOtherEvent(Other.CommandEvent event) throws IOException {
    Runtime.getRuntime().exec(event.command());
  }

  @EventListener
  public void observeAnyEvent() {
  }

  @KafkaListener
  public void observeAnyKafka(String command) {
  }

  @KafkaListener(topics = "commands")
  public void consumeKafka(String command) throws IOException {
    Runtime.getRuntime().exec(command);
  }

  @CommandKafkaListener
  public void consumeAnyKafka(String command) throws IOException {
    Runtime.getRuntime().exec(command);
  }

  @KafkaListener(topics = "commands")
  public void auditKafka(String command, int partition) {
  }

  @KafkaListener(topics = "commands")
  public void countKafkaDelivery() {
  }

  @RabbitListener(queues = {"audit", "commands"})
  public void consumeRabbit(String command) throws IOException {
    Runtime.getRuntime().exec(command);
  }

  @JmsListener(destination = "commands")
  public void consumeJms(String command) throws IOException {
    Runtime.getRuntime().exec(command);
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
