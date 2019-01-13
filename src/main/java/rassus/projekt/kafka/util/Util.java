package rassus.projekt.kafka.util;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Util {
    public static final String CPU_USAGE_TOPIC = "cpu";
    public static final String RAM_USAGE_TOPIC = "ram";
    public static final String TCP_SENT_TOPIC = "tcp-sent";
    public static final String TCP_RECEIVED_TOPIC = "tcp-received";
    public static final String UDP_SENT_TOPIC = "udp-sent";
    public static final String UDP_RECEIVED_TOPIC = "udp-received";
    public static final Set<String> KAFKA_TOPICS = createTopicSet();
    public static final int DEFAULT_PARTITIONS = 1;
    public static final short DEFAULT_REPLICATION_FACTOR = 1;
    private static final String KAFKA_CLUSTER_ADDRESS = "localhost:9092";
    private static final String KAFKA_KEY_SERIALIZER_CONFIG = "key.serializer";
    private static final String KAFKA_VALUE_SERIALIZER_CONFIG = "value.serializer";

    private static Set<String> createTopicSet() {
        return new HashSet<>(Arrays.asList(CPU_USAGE_TOPIC, RAM_USAGE_TOPIC, TCP_SENT_TOPIC, TCP_RECEIVED_TOPIC, UDP_SENT_TOPIC, UDP_RECEIVED_TOPIC));
    }

    public static Properties fillProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CLUSTER_ADDRESS);
//        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        properties.put(KAFKA_KEY_SERIALIZER_CONFIG, StringSerializer.class.getName());
        properties.put(KAFKA_VALUE_SERIALIZER_CONFIG, IntegerSerializer.class.getName());


        return properties;
    }

    static void startup(StreamsBuilder builder, Properties properties) throws InterruptedException {
        final Topology topology = builder.build();
        System.out.println(topology.describe());

        final KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        final CountDownLatch latch = new CountDownLatch(1);

        // shutdown handler da napravim nešto s Ctrl-c
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            kafkaStreams.close();
            latch.countDown();
        }, "streams-shutdown-hook"));

        kafkaStreams.start();
        latch.await();
    }
}
