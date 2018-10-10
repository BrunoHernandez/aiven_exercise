package com.aiven.producer;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerWrapper {

    private static String PROPERTIES_PATH = "producer.properties";

    private Properties properties;
    private Producer<String, String> producer;
    private String topicName;

    public ProducerWrapper(String topicName) {
        fillProperties();
        producer = new KafkaProducer<String, String> (properties);
        this.topicName = topicName;
    }

    private void fillProperties() {
        properties = new Properties();
        try {
            properties.load(new java.io.FileInputStream(PROPERTIES_PATH));
        } catch (java.io.IOException exception) {
            System.err.println("Unable to load properties file: "
                               + exception);
        }
    }

    public void start(int maxSend) {
        for(int i = 0; i < maxSend; i++)
            producer.send(new ProducerRecord<String, String>(
                topicName,
                Integer.toString(i), Integer.toString(i)));
        producer.close();
    }

}
