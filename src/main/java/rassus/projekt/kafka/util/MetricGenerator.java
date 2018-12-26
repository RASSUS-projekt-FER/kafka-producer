package rassus.projekt.kafka.util;

/**
 * Ovo sučelje definira tvornicu mjerenja. Ideja je da implementacija sučelja
 * ima pristup nekoj datoteci gdje pišu parametri uređaja, zatim na temelju istih
 * generira neki rezultat. Ovo je podložno provjerama naravno :)
 */
public interface MetricGenerator {

    /**
     * Vraća CPU usage
     *
     * @param deviceId id nekog uređaja
     *
     * @return cpu usage
     */
    int generateCPUUsage(int deviceId);

    /**
     * Vraća Memory usage
     *
     * @param deviceId id uređaja
     *
     * @return memory usage
     */
    int generateMemoryUsage(int deviceId);

    /**
     * Vraća broj aktivnih veza.
     *
     * @param deviceId id uređaja
     *
     * @return broj veza
     */
    int generateNoConnections(int deviceId);

    /**
     * vraća uređeni par (poslanih,primljenih) TCP paketa.
     *
     * @param deviceId id uređaja
     *
     * @return TCP poslano, primljeno
     */
    int[] generateTCPTraffic(int deviceId);

    /**
     * vraća uređeni par (poslanih,primljenih) UDP paketa.
     *
     * @param deviceId id uređaja
     *
     * @return UDP poslano, primljeno
     */
    int[] generateUDPTraffic(int deviceId);

    //TODO: dopuniti
}
