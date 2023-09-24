package org.lab7;


import org.lab7.collection.data.Route;
import org.lab7.collection.data.User;
import org.lab7.collection.data.UserCredentials;
import org.lab7.udp.Client;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;
import org.lab7.udp.exception.ServerRuntimeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ConnectionManager class is responsible for managing the communication with the server.
 * It sends various commands to the server and handles server responses.
 */
public class ConnectionManager {
    private Client client;

    /**
     * Initializes a new ConnectionManager instance and establishes a connection with the server.
     *
     * @throws IOException if there is an issue with establishing the connection.
     */
    public ConnectionManager() throws IOException {
        client = new Client();
    }

    /**
     * Sends a command to the server and receives a response.
     *
     * @param command The command to be sent to the server.
     * @return The response from the server.
     */
    public ServerCommand send(ServerCommand command) {
        try {
            byte[] response = client.sendMsg(Utils.serializeObject(command));
            if (response == null)
                return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("An error occurred while receiving packets from the server"), null);
            return (ServerCommand) Utils.deserializeObject(response);
        } catch (IOException e) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Server response timeout exceeded"), null);
        }
    }

    /**
     * Clears the collection on the server.
     */
    public void clear() {
        if (send(new ServerCommand(ServerCommandType.CLEAR, null, Main.getCredentials())).type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("Failed to clear the collection.");
    }

    /**
     * Retrieves information about the collection from the server.
     *
     * @return A list of strings containing collection information.
     */
    public List<String> getInfo() {
        ServerCommand response = send(new ServerCommand(ServerCommandType.GET_INFO, null, Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("Failed to get the information.");
        return (ArrayList<String>) Utils.deserializeObject(response.data);
    }

    /**
     * Removes all routes with IDs greater than the specified ID from the collection on the server.
     *
     * @param id The ID used as the threshold for removal.
     */
    public void removeGreater(int id) {
        if (send(new ServerCommand(ServerCommandType.REMOVE_GREATER, Utils.intToBytes(id), Main.getCredentials())).type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("Failed to carry out the deletion.");
    }

    /**
     * Removes the last route from the collection on the server.
     *
     * @return True if a route was removed successfully, false if the collection is empty.
     */
    public boolean removeLast() {
        ServerCommand response = send(new ServerCommand(ServerCommandType.REMOVE_LAST, null, Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("Failed to delete the item.");
        return response.data[0] == 1;
    } // false = коллекция пуста

    /**
     * Reorders the routes in the collection on the server.
     */
    public void reorder() {
        if (send(new ServerCommand(ServerCommandType.REORDER, null, Main.getCredentials())).type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("Failed to execute the operation.");
    }

    /**
     * Retrieves the route with the maximum distance from the server's collection.
     *
     * @return The route with the maximum distance.
     */
    public Route getMaxByDistance() {
        ServerCommand response = send(new ServerCommand(ServerCommandType.GET_MAX_DISTANCE, null, Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("Failed to get the information.");
        return (Route) Utils.deserializeObject(response.data);
    }

    /**
     * Retrieves a page of routes from the server's collection.
     *
     * @param page The page number to retrieve.
     * @return A list of objects representing routes on the specified page.
     */
    public List<Object> show(int page) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.SHOW, Utils.intToBytes(page), Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR) {
            if (response.data == null || response.data.length == 0)
                throw new ServerRuntimeException("Failed to get the information.");
            throw new ServerRuntimeException((String) Utils.deserializeObject(response.data));
        }
        return (List<Object>) Utils.deserializeObject(response.data);
    }

    /**
     * Removes a route with the specified ID from the server's collection.
     *
     * @param id The ID of the route to be removed.
     * @return True if the route was removed successfully, false if the route was not found.
     */
    public boolean removeById(int id) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.REMOVE_BY_ID, Utils.intToBytes(id), Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("The specified element was not found.");
        return response.data[0] == 1;
    }

    /**
     * Adds a new route to the server's collection.
     *
     * @param route The route to be added.
     * @return True if the route was added successfully, false if there was a validation error.
     */
    public boolean add(Route route) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.ADD, Utils.serializeObject(route), Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR)
            throw new ServerRuntimeException((String) Utils.deserializeObject(response.data));
        return response.data[0] == 1;
    } // по дефолту отправляется org.id = -1; false - ошибка валидации, передавать в ответе текст ошибки

    /**
     * Retrieves a route with the specified ID from the server's collection.
     *
     * @param id The ID of the route to be retrieved.
     * @return The route with the specified ID.
     */
    public Route get(int id) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.GET, Utils.intToBytes(id), Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("The object with the given \"id\" does not exist.");
        return (Route) Utils.deserializeObject(response.data);
    } // часть команды Update - перед обновлением происходит проверка на существование объекта и получение его данных

    /**
     * Updates an existing route in the server's collection.
     *
     * @param organization The updated route to be saved.
     * @return True if the update was successful, false if there was a validation error.
     */
    public boolean update(Route organization) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.UPDATE, Utils.serializeObject(organization), Main.getCredentials()));
        if (response.type == ServerCommandType.ERROR)
            throw new ServerRuntimeException((String) Utils.deserializeObject(response.data));
        return response.data[0] == 1;
    } // отправляется org.id = объект для изменения; false - ошибка валидации, передавать в ответе текст ошибки

    /**
     * Performs an actualization operation to update server data.
     *
     * @throws ServerRuntimeException If the actualization process fails.
     */
    public void actualize() {
        if (send(new ServerCommand(ServerCommandType.ACTUALIZE, null, Main.getCredentials())).type == ServerCommandType.ERROR)
            throw new ServerRuntimeException("Failed to update data.");
    }

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param credentials The user credentials for authentication.
     * @return The authenticated user.
     * @throws ServerRuntimeException If authentication fails due to invalid credentials or other errors.
     */
    public User auth(UserCredentials credentials) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.AUTH, null, credentials));
        if (response.type == ServerCommandType.ERROR) {
            if (response.data != null)
                throw new ServerRuntimeException((String) Utils.deserializeObject(response.data));
            throw new ServerRuntimeException("Invalid login or password");
        }
        return (User) Utils.deserializeObject(response.data);
    }

    /**
     * Registers a new user with the provided credentials.
     *
     * @param credentials The user credentials for registration.
     * @throws ServerRuntimeException If registration fails due to errors, such as an existing user with the same login.
     */
    public void register(UserCredentials credentials) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.REGISTER, Utils.serializeObject(credentials), null));
        if (response.type == ServerCommandType.ERROR) {
            if (response.data != null)
                throw new ServerRuntimeException((String) Utils.deserializeObject(response.data));
            throw new ServerRuntimeException("A user with this login already exists");
        }
    }
}
