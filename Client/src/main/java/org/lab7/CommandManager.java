package org.lab7;


import org.lab7.collection.data.User;
import org.lab7.collection.data.UserCredentials;
import org.lab7.task.*;
import org.lab7.task.Task;
import org.lab7.udp.exception.ServerRuntimeException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The CommandManager class manages the available commands in the application.
 * It allows registering, executing, and retrieving a list of commands.
 */
public class CommandManager {
    private Map<String, Task> commands = new LinkedHashMap<>();

    /**
     * Constructs a CommandManager and registers default commands.
     */
    public CommandManager() {
        registerCommand("help", new HelpTask());
        registerCommand("info", new InfoTask());
        registerCommand("show", new ShowTask());
        registerCommand("add", new AddTask());
        registerCommand("update", new UpdateTask());
        registerCommand("remove_by_id", new RemoveTask());
        registerCommand("clear", new ClearTask());
        registerCommand("execute_script", new ExecuteTask());
        registerCommand("exit", new ExitTask());
        registerCommand("remove_greater", new RemoveGreaterTask());
        registerCommand("remove_last", new RemoveLastTask());
        registerCommand("reorder", new ReorderTask());
        registerCommand("max_by_distance", new MaxByDistanceTask());
        registerCommand("actualize", new ActualizeTask());
    }

    /**
     * Starts reading user input and executing corresponding commands.
     * Prints available commands when the application starts.
     */
    public void run() {
        System.out.println("To display the available commands, write 'help'");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            ExecuteTask.executedScripts.clear();
            System.out.print("> ");
            try {
                executeInput(scanner.nextLine());
            } catch (ServerRuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Executes a command based on user input.
     *
     * @param input Command with arguments
     */
    public final void executeInput(String input) {
        String[] command = input.split(" ", 2);
        if (!commands.containsKey(command[0]))
            System.out.println("Unknown command: '" + command[0] + "'");
        else
            commands.get(command[0]).execute(command.length > 1 ? command[1].split(" ") : new String[0]);
    }

    /**
     * Registers a new command.
     *
     * @param name    Command name
     * @param command Object implementing the Task interface
     */
    public final void registerCommand(String name, Task command) {
        commands.put(name, command);
    }

    /**
     * Retrieves the available commands.
     *
     * @return A map of command names to corresponding Task objects.
     */
    public Map<String, Task> getCommands() {
        return commands;
    }

    /**
     * Authenticates the user or allows for user registration, interacting with the user through the console.
     * If the user is already authenticated, this method returns without any action.
     *
     * @throws ServerRuntimeException If there are errors during authentication or registration.
     */
    public void auth() {
        if (Main.getCredentials() != null)
            return;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Do you want to log in or register?");
                System.out.println("1 = Log in");
                System.out.println("2 = Register");
                System.out.print("> ");
                String request = scanner.nextLine().strip();
                if ("1".equals(request)) { // авторизация
                    System.out.print("Enter login: ");
                    String login = scanner.nextLine().strip();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    UserCredentials credentials = new UserCredentials(login, password);
                    User auth = Main.getConnectionManager().auth(credentials);
                    System.out.println("Successful authorization. User - " + auth.getUsername());
                    Main.setCredentials(credentials);
                    Main.setCurrentUser(auth);
                    break;
                } else if ("2".equals(request)) { // регистрация
                    System.out.print("Enter login: ");
                    String login = scanner.nextLine().strip();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    UserCredentials credentials = new UserCredentials(login, password);
                    Main.getConnectionManager().register(credentials);
                    System.out.println("User " + login + " registered. Please log in to continue.");
                }
            } catch (ServerRuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

