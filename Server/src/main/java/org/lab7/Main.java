package org.lab7;


import java.io.File;
import java.io.IOException;


/**
 * Main class for lab 7
 */


public class Main {
    private static StorageManager storageManager;
    private static ConnectionManager connectionManager;

    public static void main(String[] args)  {

        System.out.println("Route management");
        File file = new File("data.json");
        String filename = file.getAbsolutePath();

        while (true) {
            storageManager = new StorageManager(filename);
            connectionManager = new ConnectionManager();
            try {
                connectionManager.run();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Application restart..." + ex);
            }
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
}



