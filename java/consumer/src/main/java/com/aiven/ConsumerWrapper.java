package com.aiven.consumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerWrapper {

    private static String PROPERTIES_PATH = "application.properties";
    private static long POLLING_TIMEOUT_MS = 200;

    private Properties properties;
    private KafkaConsumer<String, String> consumer;
    private String topicName;
    private DataAccessObject dataAccessObject;

    public ConsumerWrapper(String topicName,
                           String group,
                           DataAccessObject dataAccessObject) {
        fillProperties(group);
        consumer = new KafkaConsumer<String, String> (properties);
        this.topicName = topicName;
        this.dataAccessObject = dataAccessObject;
    }

    private void fillProperties(String group) {
        properties = new Properties();
        properties.put("group.id", group);
        try {
            properties.load(new java.io.FileInputStream(PROPERTIES_PATH));
        } catch (java.io.IOException exception) {
            System.err.println("Unable to load properties file: "
                               + exception);
        }
    }

    public void start(int maxSend) {
        consumer.subscribe(Arrays.asList(topicName));
        System.out.println("Subscribed to topic \"" + topicName + "\"");
        for(int i = 0; i < maxSend; i++) {
            ConsumerRecords<String, String> records =
                consumer.poll(POLLING_TIMEOUT_MS);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s",
                    record.offset(), record.key(), record.value());
                try {
                    dataAccessObject.insertRecord(record.key(),
                                                  record.value());
                } catch (java.sql.SQLException exception) {
                    System.err.println("Could not insert record in database. "
                                       + "Exception: "
                                       + exception.getMessage());
                }
            }
        }
        consumer.close();
    }

}
