package com.aiven.consumer;

public class ExampleConsumer {

    private static final String ARTIFACT_NAME = "consumer.jar";
    private static final double MAIN_THREAD_POLLING_MS = 200;

    private static String usage() {
        return ARTIFACT_NAME + " <topic> <max send> <group name>";
    }


    private static void addSqlHook(ScopedPostgreSqlConnection sqlConnection) {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.err.println(
                    "Program terminated. Closing connections...");
                sqlConnection.close();
            }
        });
    }

    public static void main(String[] args) {

        if(args.length != 3){
            System.err.println("Argument(s) missing. Usage: " + usage());
            return;
        }

        final ScopedPostgreSqlConnection sqlConnection =
            new ScopedPostgreSqlConnection();
        addSqlHook(sqlConnection);
        final ConsumerWrapper consumerWrapper =
            new ConsumerWrapper(args[0].toString(),
                                args[2].toString());
        consumerWrapper.start(Integer.parseInt(args[1]));
    }
}
