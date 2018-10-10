package com.aiven.producer;

public class ExampleProducer {

    private static final String ARTIFACT_NAME = "producer.jar";

    private static String usage() {
        return ARTIFACT_NAME + " <topic> <max send>";
    }


    public static void main(String[] args) {

        if(args.length != 2){
            System.err.println("Argument(s) missing. Usage: " + usage());
            return;
        }

        ProducerWrapper producerWrapper =
            new ProducerWrapper(args[0].toString());
        producerWrapper.start(Integer.parseInt(args[1]));
    }
}
