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
    private static final Map<Integer, Config> k = fillMap(); //TODO: statička konfiguracija ili periodično čitanje
    private static final Random RANDOM = new Random();

    private static Map<Integer, Config> fillMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int generateCPUUsage(int deviceId) {
        Config c = k.get(deviceId);
        //TODO: vidi todu u razredu Config
        return toIntExact(round(RANDOM.nextGaussian() * c.getCpuSpike() + c.getAvgCpu()));
    }

    @Override
    public int generateMemoryUsage(int deviceId) {
        Config c = k.get(deviceId);
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
