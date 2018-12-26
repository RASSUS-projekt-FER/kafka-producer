package rassus.projekt.kafka.model;

import rassus.projekt.kafka.util.DefaultMetricGenerator;
import rassus.projekt.kafka.util.MetricGenerator;

/**
 * Ovaj razred simulira uređaj u mreži. Pri pokretanju iz naredbenog retka uzima id iz liste argumenata
 * i čita vlastitu konfiguraciju. Periodično generira mjerenja i šalje na Kafka klaster.
 */
public class Device {
    //TODO: dopuniti
    private static int INSTANCE_COUNT = 0;
    //    private String name;
    private final MetricGenerator generator = new DefaultMetricGenerator(); //TODO: privatna implementacija ili generalna
    private int id;

    public Device(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Neispravan id uređaja: " + id);
        }
        this.id = id;
        INSTANCE_COUNT++;
    }
}
