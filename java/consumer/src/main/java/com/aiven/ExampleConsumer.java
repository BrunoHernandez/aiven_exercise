package com.aiven.consumer;

public class ExampleConsumer {

    private static final String ARTIFACT_NAME = "consumer.jar";

    private static String usage() {
        return ARTIFACT_NAME + " <topic> <max send> <group name>";
    }


    public static void main(String[] args) {

        if(args.length != 3){
            System.err.println("Argument(s) missing. Usage: " + usage());
            return;
        }

        ConsumerWrapper consumerWrapper =
            new ConsumerWrapper(args[0].toString(), args[2].toString());
        consumerWrapper.start(Integer.parseInt(args[1]));
    }
}
