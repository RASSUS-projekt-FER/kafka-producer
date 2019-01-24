package rassus.projekt.kafka.generator;

import lombok.extern.log4j.Log4j2;
import rassus.projekt.kafka.model.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Implementacija sučelja {@linkplain MetricGenerator}. Za generiranje nasumičnih vrijednosti
 * koristit će se normalna (Gaussova) razdioba i njena implementacija u {@link java.util.Random}.
 */
@Log4j2
public class DefaultMetricGenerator implements MetricGenerator {
    /**
     * Mapa koja sadrži konfiguracije svih postojećih uređaja.
     */
    private static final Map<Integer, Config> configMap = fillMap();
    /**
     * {@link Random} objekt za nasumična mjerenja
     */
    private static final Random RANDOM = new Random();

    /**
     * @return popunjena mapa konfiguracija svih uređaja
     */
    private static Map<Integer, Config> fillMap() {
        try {
            Map<Integer, Config> map = new HashMap<>();
            for (String line : Files.readAllLines(CONFIG_PATH)) {
                if (line.isEmpty() || line.startsWith(COMMENT_LINE)) {
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length != NUM_CONFIG_PARAMETERS) {
                    log.error("Redak: '" + line + "' nije ispravnog formata");
                } else {
                    map.put(Integer.parseInt(fields[0]),
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

            return map;
        } catch (IOException e) {
            throw new RuntimeException("Nemoguće učitati konfiguracijsku datoteku", e);
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
        Config c = configMap.get(deviceId);
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
    public double generateCPUUsage(int deviceId) {
        Config c = getConfigForId(deviceId);
        return RANDOM.nextGaussian() * c.getCpuSpike() + c.getAvgCpu();
    }

    @Override
    public double generateMemoryUsage(int deviceId) {
        Config c = getConfigForId(deviceId);
        return RANDOM.nextGaussian() * c.getRamSpike() + c.getAvgRam();
    }

    @Override
    public double[] generateTCPTraffic(int deviceId) {
        Config c = getConfigForId(deviceId);
        double sDeviation = PACKET_DEVIATION_FACTOR * c.getTcpSent();
        double rDeviation = PACKET_DEVIATION_FACTOR * c.getTcpReceived();
        return new double[]{
                RANDOM.nextGaussian() * sDeviation + c.getTcpSent(), //poslani paketi
                RANDOM.nextGaussian() * rDeviation + c.getTcpReceived()}; //primljeni paketi
    }

    @Override
    public double[] generateUDPTraffic(int deviceId) {
        Config c = getConfigForId(deviceId);
        double sDeviation = PACKET_DEVIATION_FACTOR * c.getTcpSent();
        double rDeviation = PACKET_DEVIATION_FACTOR * c.getTcpReceived();
        return new double[]{
                RANDOM.nextGaussian() * sDeviation + c.getUdpSent(), //poslani paketi
                RANDOM.nextGaussian() * rDeviation + c.getUdpReceived()}; //primljeni paketi
    }

    @Override
    public int getNumberOfNodes() {
        return configMap.size();
    }
}
