package rassus.projekt.kafka.model;

import java.util.Objects;

/**
 * Ovaj razred predstavlja konfiguraciju pojedinog uređaja. Sadrži parametre
 * pomoću kojih se generiraju metrike.
 */
public class Config {
    /**
     * naziv uređaja
     */
    private String name; //TODO: ima li smisla da su iznosi metrika samo cijeli brojevi
    /**
     * prosječan cpu usage
     */
    private int avgCpu;
    /**
     * Vjerojatnost da cpu usage naglo padne ili skoči
     */
    private int cpuSpike;
    /**
     * prosječan ram usage
     */
    private int avgRam;
    /**
     * Vjerojatnost da ram usage naglo padne ili skoči
     */
    private int ramSpike;
    /**
     * Prosječan broj poslanih tcp paketa za neki interval //TODO: deinirati interval
     */
    private int tcpSent;
    /**
     * Prosječan broj primljenih tcp paketa za neki interval
     */
    private int tcpReceived;
    /**
     * Prosječan broj poslanih udp paketa za neki interval //TODO: deinirati interval
     */
    private int udpSent;
    /**
     * Prosječan broj primljenih udp paketa za neki interval
     */
    private int udpReceived;

    public Config(String name, int avgCpu, int cpuSpike, int avgRam, int ramSpike, int tcpSent, int tcpReceived, int udpSent, int udpReceived) {
        this.name = Objects.requireNonNull(name);
        this.avgCpu = checkPercentage(avgCpu);
        this.cpuSpike = checkPercentage(cpuSpike);
        this.avgRam = checkPercentage(avgRam);
        this.ramSpike = checkPercentage(ramSpike);
        this.tcpSent = checkCount(tcpSent);
        this.tcpReceived = checkCount(tcpReceived);
        this.udpSent = checkCount(udpSent);
        this.udpReceived = checkCount(udpReceived);
    }

    private static int checkPercentage(int p) {
        if (p < 0 || p > 100) {
            throw new IllegalArgumentException("Postotak ne može biti manji od 0 ili veći od 100: " + p);
        }
        return p;
    }

    private static int checkCount(int c) {
        if (c < 0) {
            throw new IllegalArgumentException("Količina ne može biti manja od nula: " + c);
        }

        return c;
    }

    public String getName() {
        return name;
    }

    public int getAvgCpu() {
        return avgCpu;
    }

    public int getCpuSpike() {
        return cpuSpike;
    }

    public int getAvgRam() {
        return avgRam;
    }

    public int getRamSpike() {
        return ramSpike;
    }

    public int getTcpSent() {
        return tcpSent;
    }

    public int getTcpReceived() {
        return tcpReceived;
    }

    public int getUdpSent() {
        return udpSent;
    }

    public int getUdpReceived() {
        return udpReceived;
    }
}
