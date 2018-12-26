package rassus.projekt.kafka.util;

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
    private int tcpSent; //TODO: devijacije veza, generiranje veza?
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
        this.name = name;
        this.avgCpu = avgCpu;
        this.cpuSpike = cpuSpike;
        this.avgRam = avgRam;
        this.ramSpike = ramSpike;
        this.tcpSent = tcpSent;
        this.tcpReceived = tcpReceived;
        this.udpSent = udpSent;
        this.udpReceived = udpReceived;
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
