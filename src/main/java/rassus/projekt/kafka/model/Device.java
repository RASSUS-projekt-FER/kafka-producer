package rassus.projekt.kafka.model;

import rassus.projekt.kafka.util.DefaultMetricGenerator;
import rassus.projekt.kafka.util.MetricGenerator;

public class Device {
    //TODO: dopuniti
    private static int INSTANCE_COUNT = 0;
    private int id;
    //    private String name;
    private final MetricGenerator generator = new DefaultMetricGenerator(); //TODO: privatna implementacija ili generalna
}
