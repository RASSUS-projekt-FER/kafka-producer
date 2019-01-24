package rassus.projekt.kafka.model;

public class Metric {
    private String name;
    private double cpu;
    private double ram;
    private double tcpSent;
    private double tcpReceived;
    private double udpSent;
    private double udpReceived;

    public Metric(String name, double cpu, double ram, double[] tcp, double[] udp) {
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

    public double getCpu() {
        return cpu;
    }

    public double getRam() {
        return ram;
    }

    public double getTcpSent() {
        return tcpSent;
    }

    public double getTcpReceived() {
        return tcpReceived;
    }

    public double getUdpSent() {
        return udpSent;
    }

    public double getUdpReceived() {
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
