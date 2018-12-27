package rassus.projekt.kafka.util;

import org.junit.Test;

import java.util.Arrays;

public class DefaultMetricGeneratorTest {

    @Test
    public void test1() {
        MetricGenerator mg = new DefaultMetricGenerator();
        System.out.println(String.format("cpu:%d,ram:%d,tcp:%s,udp:%s",
                mg.generateCPUUsage(1),
                mg.generateMemoryUsage(1),
                Arrays.toString(mg.generateTCPTraffic(1)),
                Arrays.toString(mg.generateUDPTraffic(1))
        ));
    }

    @Test
    public void generateMemoryUsage() {
    }

    @Test
    public void generateTCPTraffic() {
    }

    @Test
    public void generateUDPTraffic() {
    }
}