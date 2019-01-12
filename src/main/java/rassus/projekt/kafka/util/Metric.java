package rassus.projekt.kafka.util;

public class Metric {
    private String name;
    private int cpu;
    private int ram;
    private int tcpSent;
    private int tcpReceived;
    private int udpSent;
    private int udpReceived;

    public Metric(String name, int cpu, int ram, int[] tcp, int[] udp) {
        this.name = name;
        this.cpu = cpu;
        this.ram = ram;
        this.tcpSent = tcp[0];
        this.tcpReceived = tcp[1];
        this.udpSent = udp[0];
        this.udpReceived = udp[1];
    }

    public String getName() {
        return name;
    }

    public int getCpu() {
        return cpu;
    }

    public int getRam() {
        return ram;
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

    @Override
    public String toString() {
        return "Metric{" +
                "name='" + name + '\'' +
                ", cpu=" + cpu +
                ", ram=" + ram +
                ", tcpSent=" + tcpSent +
                ", tcpReceived=" + tcpReceived +
                ", udpSent=" + udpSent +
                ", udpReceived=" + udpReceived +
                '}';
    }
}
