package rassus.projekt.kafka.model;

import rassus.projekt.kafka.util.DefaultMetricGenerator;
import rassus.projekt.kafka.util.MetricGenerator;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
    /**
     * Generator metrika
     */
    private final MetricGenerator generator = new DefaultMetricGenerator(); //TODO: privatna implementacija ili generalna
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
            //todo: metoda zasad samo ispisuje generirane metrike, ovdje treba slati na raspodijeljene teme u kafki
            System.out.println("Generiram podatke...");
            System.out.println(String.format("cpu:%d,ram:%d,tcp:%s,udp:%s",
                    generator.generateCPUUsage(id),
                    generator.generateMemoryUsage(id),
                    Arrays.toString(generator.generateTCPTraffic(id)),
                    Arrays.toString(generator.generateUDPTraffic(id))
            ));
        }
    }
}
