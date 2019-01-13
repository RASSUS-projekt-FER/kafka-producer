package rassus.projekt.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import rassus.projekt.kafka.util.DefaultMetricGenerator;
import rassus.projekt.kafka.util.Metric;
import rassus.projekt.kafka.util.MetricGenerator;

import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static rassus.projekt.kafka.util.Util.CPU_USAGE_TOPIC;
import static rassus.projekt.kafka.util.Util.fillProperties;

/**
 * Ovaj razred simulira uređaj u mreži. Pri pokretanju iz naredbenog retka uzima id iz liste argumenata
 * i čita vlastitu konfiguraciju. Periodično generira mjerenja i šalje na Kafka klaster.
 */
public class Device {
    /**
     * Naredba koju je potrebno upisati kako bi se emulator uređaja isključio.
     */
    private static final String SHUTDOWN_COMMAND = "off";
    /**
     * Vremenski interval nakon kojeg se generiraju nove metrike.
     */
    private static final long METRIC_INTERVAL_MILLIS = 5000;
    private static final Properties PRODUCER_PROPERTIES = fillProperties();
    /**
     * Generator metrika
     */
    private final MetricGenerator generator = new DefaultMetricGenerator();
    /**
     * Id uređaja iz konfiguracije
     */
    private int id;
    /**
     * Timer za periodično generiranje metrika
     */
    private Timer metricTimer = new Timer(true);

    /**
     * Konstruktor.
     *
     * @param id id uređaja
     */
    private Device(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Neispravan id uređaja: " + id);
        }
        this.id = id;
        if (id > generator.getNumberOfNodes()) {
            throw new IllegalArgumentException("Neispravan id uređaja: " + id);
        }
    }

    /**
     * Main metoda.
     *
     * @param args id uređaja (integer)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Korištenje: device <id-uređaja>");
            System.exit(1);
        }


        int id = 0;
        try {
            id = Integer.parseInt(args[0]);
            if (id < 1) {
                System.out.println("id uređaja mora biti prirodan broj");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.out.println("id uređaja mora biti prirodan broj");
            System.exit(1);
        }

        new Device(id).run();

        System.out.println("Goodbye");
    }

    /**
     * Metoda emulira rad uređaja. Pokreće se Timer i "čeka" se naredba zaustavljanja.
     */
    private void run() {
        Scanner sc = new Scanner(System.in);
        metricTimer.scheduleAtFixedRate(new MetricTask(), METRIC_INTERVAL_MILLIS, METRIC_INTERVAL_MILLIS);
        //todo: u scheduled task poslati metrike na zasebne teme
        while (sc.hasNext()) {
            String cmd = sc.nextLine();
            if (cmd.toLowerCase().equals(SHUTDOWN_COMMAND)) {
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
            System.out.println("Generiram podatke...");
            Metric metric = new Metric(generator.getDeviceName(id), generator.generateCPUUsage(id),
                    generator.generateMemoryUsage(id),
                    generator.generateTCPTraffic(id),
                    generator.generateUDPTraffic(id));
            System.out.println(metric);
            sendMetricToCluster(metric);
        }

        private void sendMetricToCluster(Metric metric) {
            //todo ovo testirati
            String name = metric.getName();
            Producer<String, Integer> producer = new KafkaProducer<>(PRODUCER_PROPERTIES);
            producer.send(new ProducerRecord<>(CPU_USAGE_TOPIC, name, metric.getCpu()));
//            producer.send(new ProducerRecord<>(RAM_USAGE_TOPIC, name, metric.getRam()));
//            producer.send(new ProducerRecord<>(TCP_SENT_TOPIC, name, metric.getTcpSent()));
//            producer.send(new ProducerRecord<>(TCP_RECEIVED_TOPIC, name, metric.getTcpReceived()));
//            producer.send(new ProducerRecord<>(UDP_SENT_TOPIC, name, metric.getUdpSent()));
//            producer.send(new ProducerRecord<>(UDP_RECEIVED_TOPIC, name, metric.getUdpReceived()));

            System.out.println("Podaci poslani na klaster...");
        }
    }
}
