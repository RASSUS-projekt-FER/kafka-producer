package rassus.projekt.kafka.util;

import java.util.Map;
import java.util.Random;

import static java.lang.StrictMath.round;
import static java.lang.StrictMath.toIntExact;

/**
 * Implementacija sučelja {@linkplain MetricGenerator}. Za generiranje nasumičnih vrijednosti
 * koristit će se normalna (Gaussova) razdioba i njena implementacija u {@link java.util.Random}.
 */
public class DefaultMetricGenerator implements MetricGenerator {
    /**
     * Mapa koja sadrži konfiguracije svih postojećih uređaja
     */
    private static final Map<Integer, Config> k = fillMap(); //TODO: statička konfiguracija ili periodično čitanje
    /**
     * {@link Random} objekt za nasumična mjerenja
     */
    private static final Random RANDOM = new Random();

    /**
     * @return popunjena mapa konfiguracija svih uređaja
     */
    private static Map<Integer, Config> fillMap() {
        throw new UnsupportedOperationException();
    }

    /**
     * Vraća {@link Config} objekt za primljeni id, ako postoji.
     *
     * @param deviceId id uređaja
     *
     * @return Config uređaja
     */
    private static Config getConfigForId(int deviceId) {
        Config c = k.get(deviceId);
        if (c == null) {
            throw new IllegalArgumentException("Uređaj s id: " + deviceId + " ne postoji...");
        }

        return c;
    }

    @Override
    public int generateCPUUsage(int deviceId) {
        Config c = getConfigForId(deviceId);
        //TODO: vidi todu u razredu Config
        return toIntExact(round(RANDOM.nextGaussian() * c.getCpuSpike() + c.getAvgCpu()));
    }

    @Override
    public int generateMemoryUsage(int deviceId) {
        Config c = getConfigForId(deviceId);
        return toIntExact(round(RANDOM.nextGaussian() * c.getRamSpike() + c.getAvgRam()));
    }

    @Override
    public int generateNoConnections(int deviceId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] generateTCPTraffic(int deviceId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] generateUDPTraffic(int deviceId) {
        throw new UnsupportedOperationException();
    }
}
