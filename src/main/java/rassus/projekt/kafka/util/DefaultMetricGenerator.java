package rassus.projekt.kafka.util;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.StrictMath.round;
import static java.lang.StrictMath.toIntExact;

/**
 * Implementacija sučelja {@linkplain MetricGenerator}. Za generiranje nasumičnih vrijednosti
 * koristit će se normalna (Gaussova) razdioba i njena implementacija u {@link java.util.Random}.
 */
public class DefaultMetricGenerator implements MetricGenerator {
//    private static final Path CONFIG_PATH = Paths.get("src/main/resources/konfiguracija-mreze.txt");
//    private static final String COMMENT_LINE = "#";
//    private static final int NO_CONFIG_PARAMETERS = 10;
    /**
     * Mapa koja sadrži konfiguracije svih postojećih uređaja
     */
    private static final Map<Integer, Config> k = fillMap(); //TODO: statička konfiguracija ili periodično čitanje
    //odabrana statička konfiguracija
    /**
     * {@link Random} objekt za nasumična mjerenja
     */
    private static final Random RANDOM = new Random();

    /**
     * @return popunjena mapa konfiguracija svih uređaja
     */
    private static Map<Integer, Config> fillMap() {
        try {
            Map<Integer, Config> m = new HashMap<>();
            for (String line : Files.readAllLines(CONFIG_PATH)) {
                if (line.isEmpty() || line.startsWith(COMMENT_LINE)) {
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length != NO_CONFIG_PARAMETERS) {
                    System.out.println("Redak: '" + line + "' nije ispravna formata");
                } else {
                    m.put(Integer.parseInt(fields[0]),
                            new Config(fields[1],
                                    Integer.parseInt(fields[2]),
                                    Integer.parseInt(fields[3]),
                                    Integer.parseInt(fields[4]),
                                    Integer.parseInt(fields[5]),
                                    Integer.parseInt(fields[6]),
                                    Integer.parseInt(fields[7]),
                                    Integer.parseInt(fields[8]),
                                    Integer.parseInt(fields[9])));
                }
            }

            return m;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Vraća {@link Config} objekt za primljeni id, ako postoji.
     *
     * @param deviceId id uređaja
     *
     * @return Config uređaja
     */
    private static Config getConfigForId(int deviceId) {
        Config c = k.get(deviceId); //nekako riješiti
        if (c == null) {
            throw new IllegalArgumentException("Uređaj s id: " + deviceId + " ne postoji...");
        }

        return c;
    }

    @Override
    public String getDeviceName(int deviceId) {
        return getConfigForId(deviceId).getName();
    }

    @Override
    public int generateCPUUsage(int deviceId) {
        Config c = getConfigForId(deviceId);
        return toIntExact(round(RANDOM.nextGaussian() * c.getCpuSpike() + c.getAvgCpu()));
    }

    @Override
    public int generateMemoryUsage(int deviceId) {
        Config c = getConfigForId(deviceId);
        return toIntExact(round(RANDOM.nextGaussian() * c.getRamSpike() + c.getAvgRam()));
    }

//    @Override
//    public int generateNoConnections(int deviceId) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public int[] generateTCPTraffic(int deviceId) {
        Config c = getConfigForId(deviceId);
        int sDeviation = (int) (PACKET_DEVIATION_FACTOR * c.getTcpSent());
        int rDeviation = (int) (PACKET_DEVIATION_FACTOR * c.getTcpReceived());
        return new int[]{
                Math.toIntExact(round(RANDOM.nextGaussian() * sDeviation
                        + c.getTcpSent())), //poslani paketi
                Math.toIntExact(round(RANDOM.nextGaussian() * rDeviation
                        + c.getTcpReceived()))}; //primljeni paketi
    }

    @Override
    public int[] generateUDPTraffic(int deviceId) {
        Config c = getConfigForId(deviceId);
        int sDeviation = (int) (PACKET_DEVIATION_FACTOR * c.getTcpSent());
        int rDeviation = (int) (PACKET_DEVIATION_FACTOR * c.getTcpReceived());
        return new int[]{
                Math.toIntExact(round(RANDOM.nextGaussian() * sDeviation
                        + c.getUdpSent())), //poslani paketi
                Math.toIntExact(round(RANDOM.nextGaussian() * rDeviation
                        + c.getUdpReceived()))}; //primljeni paketi
    }

    @Override
    public int getNumberOfNodes() {
        return k == null ? 0 : k.size();
    }
}
