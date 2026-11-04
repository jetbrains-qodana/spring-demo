package org.springframework.kafka.core;

public class KafkaTemplate<K, V> {
  public void setDefaultTopic(String topic) {
  }

  public void send(String topic, V value) {
  }

  public void send(V value) {
  }
}
