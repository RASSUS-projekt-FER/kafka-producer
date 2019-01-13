package rassus.projekt.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import rassus.projekt.kafka.util.DefaultMetricGenerator;
import rassus.projekt.kafka.util.Metric;
import rassus.projekt.kafka.util.MetricGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static rassus.projekt.kafka.util.Util.CPU_USAGE_TOPIC;
import static rassus.projekt.kafka.util.Util.DEFAULT_PARTITIONS;
import static rassus.projekt.kafka.util.Util.DEFAULT_REPLICATION_FACTOR;
import static rassus.projekt.kafka.util.Util.KAFKA_TOPICS;
import static rassus.projekt.kafka.util.Util.RAM_USAGE_TOPIC;
import static rassus.projekt.kafka.util.Util.TCP_RECEIVED_TOPIC;
import static rassus.projekt.kafka.util.Util.TCP_SENT_TOPIC;
import static rassus.projekt.kafka.util.Util.UDP_RECEIVED_TOPIC;
import static rassus.projekt.kafka.util.Util.UDP_SENT_TOPIC;
import static rassus.projekt.kafka.util.Util.getProperties;

/**
 * Ovaj razred simulira uređaj u mreži. Pri pokretanju iz naredbenog retka uzima id iz liste argumenata
 * i čita vlastitu konfiguraciju. Periodično generira mjerenja i šalje na Kafka klaster.
 */
@Log4j2
public class Device {

    /**
     * Naredba koju je potrebno upisati kako bi se emulator uređaja isključio.
     */
    private static final String SHUTDOWN_COMMAND = "off";
    /**
     * Vremenski interval nakon kojeg se generiraju nove metrike.
     */
    private static final long METRIC_INTERVAL_MILLIS = 5000;

    /**
     * Generator metrika.
     */
    private final MetricGenerator generator = new DefaultMetricGenerator();

    /**
     * Id uređaja iz konfiguracije.
     */
    private int id;
    /**
     * Timer za periodično generiranje metrika.
     */
    private Timer metricTimer = new Timer(true);
    /**
     * Indikator jesu li topicsi kreirani.
     */
    private boolean topicsCreated = false;

    /**
     * Konstruktor.
     *
     * @param id id uređaja
     */
    private Device(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Neispravan id uređaja: " + id);
        }
        if (id > generator.getNumberOfNodes()) {
            throw new IllegalArgumentException("Neispravan id uređaja: " + id);
        }

        this.id = id;
    }

    /**
     * Main metoda.
     *
     * @param args id uređaja (integer)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            log.warn("Korištenje: device <id-uređaja>");
            System.exit(1);
        }

        int id = 0;
        try {
            id = Integer.parseInt(args[0]);
            if (id < 1) {
                log.error("id uređaja mora biti prirodan broj");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            log.error("id uređaja mora biti prirodan broj");
            System.exit(1);
        }

        Device device = new Device(id);
        device.run();

    }

    private void addShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            metricTimer.cancel();
            log.info("Goodbye");
        }));
    }

    /**
     * Metoda emulira rad uređaja. Pokreće se Timer i "čeka" se naredba zaustavljanja.
     */
    private void run() {
        addShutdownHooks();

        Scanner sc = new Scanner(System.in);
        metricTimer.scheduleAtFixedRate(new MetricTask(), METRIC_INTERVAL_MILLIS, METRIC_INTERVAL_MILLIS);
        while (sc.hasNextLine()) {
            String cmd = sc.nextLine();
            if (cmd == null || cmd.equalsIgnoreCase(SHUTDOWN_COMMAND)) {
                return;
            }
        }
    }

    /**
     * Ekstenzija razreda {@link TimerTask} za periodično generiranje metrika.
     */
    private class MetricTask extends TimerTask {

        @Override
        public void run() {
            log.info("Generiram podatke...");
            Metric metric = new Metric(generator.getDeviceName(id), generator.generateCPUUsage(id),
                    generator.generateMemoryUsage(id),
                    generator.generateTCPTraffic(id),
                    generator.generateUDPTraffic(id));

            log.info(metric);
            try {
                sendMetricToCluster(metric);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void sendMetricToCluster(Metric metric) throws ExecutionException, InterruptedException {
            String name = metric.getName();
            Properties properties = getProperties();
            properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-" + name);

            if (!topicsCreated) {
                createTopics(properties);
            }
            Producer<String, Integer> producer = new KafkaProducer<>(properties);
            producer.send(new ProducerRecord<>(CPU_USAGE_TOPIC, name, metric.getCpu()));
            producer.send(new ProducerRecord<>(RAM_USAGE_TOPIC, name, metric.getRam()));
            producer.send(new ProducerRecord<>(TCP_SENT_TOPIC, name, metric.getTcpSent()));
            producer.send(new ProducerRecord<>(TCP_RECEIVED_TOPIC, name, metric.getTcpReceived()));
            producer.send(new ProducerRecord<>(UDP_SENT_TOPIC, name, metric.getUdpSent()));
            producer.send(new ProducerRecord<>(UDP_RECEIVED_TOPIC, name, metric.getUdpReceived()));

            producer.close();
            log.info("Podaci poslani na klaster...");
        }

        private void createTopics(Properties properties) throws ExecutionException, InterruptedException {
            AdminClient client = AdminClient.create(properties);

            Set<String> topics = client.listTopics().names().get();
            List<NewTopic> newTopics = new LinkedList<>();
            for (String t : KAFKA_TOPICS) {
                if (!topics.contains(t)) {
                    newTopics.add(new NewTopic(t, DEFAULT_PARTITIONS, DEFAULT_REPLICATION_FACTOR));
                }
            }
            if (!newTopics.isEmpty()) {
                client.createTopics(newTopics);
            }

            topicsCreated = true;
        }
    }
}
