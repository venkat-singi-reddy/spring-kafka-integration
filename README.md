
# 🚀 Spring Boot Kafka Producer/Consumer with Docker Kafka Cluster

This project demonstrates how to set up a **Spring Boot (3.5.x, JDK 17) Kafka producer and consumer** using a **Dockerized Kafka cluster**.  
It covers producer/consumer configuration, topic creation, retries, idempotence, and async processing.

---

## 🐳 Docker Setup (Kafka Cluster)

We run a **3-broker Kafka cluster + Zookeeper** using Docker Compose.

### `docker-compose.yml`
```yaml

#docker-compose yaml in project will have additional configuration for kafka control-center, schema-registry, ksql-db, kafka-connect, etc

version: '2'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    hostname: zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  broker1:
    image: confluentinc/cp-kafka:7.5.0
    hostname: broker1
    container_name: broker1
    depends_on:
      - zookeeper
    ports:
      - "9091:9091"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://broker1:29091,EXTERNAL://localhost:9091
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0

  broker2:
    image: confluentinc/cp-kafka:7.5.0
    hostname: broker2
    container_name: broker2
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://broker2:29092,EXTERNAL://localhost:9092
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0

  broker3:
    image: confluentinc/cp-kafka:7.5.0
    hostname: broker3
    container_name: broker3
    depends_on:
      - zookeeper
    ports:
      - "9093:9093"
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://broker3:29093,EXTERNAL://localhost:9093
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
```

## 📌 Create Topic (user-topic)

Exec into one broker and create a topic:

```
docker exec -it broker1 kafka-topics --create \
  --topic user-topic \
  --bootstrap-server localhost:9091 \
  --replication-factor 3 \
  --partitions 3
```
Verify:

```
docker exec -it broker1 kafka-topics --list --bootstrap-server localhost:9091
```

## ⚙️ Spring Boot Configuration for Producer

### `application.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9091,localhost:9092,localhost:9093

    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 5
      batch-size: 16384
      buffer-memory: 33554432
      compression-type: snappy
      properties:
        enable.idempotence: true
        max.in.flight.requests.per.connection: 5
        linger.ms: 10
        delivery.timeout.ms: 120000

    template:
      default-topic: user-topic
```

## ⚙️ Spring Boot Configuration for Consumer

### `application.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9091,localhost:9092,localhost:9093
    consumer:
      group-id: user-consumer-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"

    template:
      default-topic: user-topic
```

## 👨‍💻 Producer Example

```
@Service
public class UserProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUser(UserEvent event) {
        kafkaTemplate.send("user-topic", event.getUserId(), event);
    }
}
```

## 👨‍💻 Consumer Example

```
@Service
public class UserConsumer {

    @KafkaListener(topics = "user-topic", groupId = "user-consumer-group",
                   containerFactory = "kafkaListenerContainerFactory")
    public void consume(UserEvent event) {
        System.out.println("Consumed UserEvent: " + event);
    }
}
```

## 🔄 Testing

Produce a message via CLI:

```
docker exec -it broker1 kafka-console-producer \
  --topic user-topic --bootstrap-server localhost:9091
```

Consume a message via CLI:

```
docker exec -it broker1 kafka-console-consumer \
  --topic user-topic --from-beginning --bootstrap-server localhost:9091
```




