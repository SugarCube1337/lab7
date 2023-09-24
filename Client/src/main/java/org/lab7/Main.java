package org.lab7;


import org.lab7.collection.data.User;
import org.lab7.collection.data.UserCredentials;

import java.io.IOException;

/**
 * The Main class serves as the entry point for the Route management application.
 * It initializes the CommandManager, ConnectionManager, User and UserCredentials and manages the application's main loop.
 */

public class Main {

    private static CommandManager commandManager;
    private static ConnectionManager connectionManager;
    private static UserCredentials credentials = null;
    private static User currentUser = null;

    public static void main(String[] args) {


        System.out.println("Route management");

        while (true) {
            try {
                connectionManager = new ConnectionManager();
            } catch (IOException e) {
                System.out.println("client destroyed himself.");
            }
            commandManager = new CommandManager();
            try {
                commandManager.run();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Restarting the app...");
            }
        }
    }


    /**
     * Get command manager
     *
     * @return Command manager
     */
    public static CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Get the connection manager instance.
     *
     * @return The ConnectionManager instance.
     */
    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Gets the user credentials.
     *
     * @return The user credentials.
     */
    public static UserCredentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the user credentials.
     *
     * @param credentials The user credentials to set.
     */
    public static void setCredentials(UserCredentials credentials) {
        Main.credentials = credentials;
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The current user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param currentUser The current user to set.
     */
    public static void setCurrentUser(User currentUser) {
        Main.currentUser = currentUser;
    }
}


