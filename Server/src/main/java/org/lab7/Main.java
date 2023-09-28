package org.lab7;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * The main class of the Route Management Server.
 */


public class Main {
    private static StorageManager storageManager;
    private static ConnectionManager connectionManager;
    private static SQLManager sqlManager;
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Route management");
        try {
            while (true) {
                sqlManager = new SQLManager();
                storageManager = new StorageManager();
                connectionManager = new ConnectionManager();
                try {
                    connectionManager.run();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Application restart..." + ex);
                }
            }
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * Get storage manager
     *
     * @return Storage manager
     */
    public static StorageManager getStorageManager() {
        return storageManager;
    }

    /**
     * Get command manager
     *
     * @return Command manager
     */
    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public static SQLManager getSqlManager() {
        return sqlManager;
    }
}



