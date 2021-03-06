package rassus.projekt.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.DoubleDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsConfig;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static rassus.projekt.kafka.util.Util.getProperties;

public class TestConsumer {
    public static void main(String[] args) {
        Properties properties = getProperties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-gejo");
        properties.put("group.id", "testni-consumer");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", DoubleDeserializer.class.getName());

        KafkaConsumer<String, Double> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("cpu"));
        for (int i = 0; i < 5; i++) {
            ConsumerRecords<String, Double> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, Double> c : records) {
                System.out.println(c.topic() + "," +
                        c.key() + ":" + c.value());
            }
        }

        consumer.commitAsync();
        consumer.close();
    }
}
