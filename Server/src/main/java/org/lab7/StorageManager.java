package org.lab7;


import org.lab7.collection.data.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The `StorageManager` class is responsible for managing a collection of `Route` objects.
 * It allows for saving, loading, adding, updating, and performing various operations on the collection.
 */
public class StorageManager {

    private LocalDate initializationDate;
    private HashSet<Route> data = new HashSet<>();

    public StorageManager() {
        updateCollection();
    }

    /**
     * Add object to collection
     *
     * @param route New route
     */
    public void add(Route route) {
        boolean result = false;
        try {
            PreparedStatement sql = Main.getSqlManager().getConnection().prepareStatement("INSERT INTO collection "
                    + "(route_name, coordinates_x, coordinates_y, creation_date, distance,  location_x, location_y, location_name, owner) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            sql.setString(1, route.getName());
            sql.setFloat(2, route.getCoordinates().getX());
            sql.setInt(3, route.getCoordinates().getY());
            sql.setString(4, route.getCreationDate());
            sql.setFloat(5, route.getDistance());
            sql.setFloat(6, route.getLocation().getX());
            sql.setDouble(7, route.getLocation().getY());
            sql.setString(8, route.getLocation().getName());
            sql.setInt(9, route.getOwner().getId());
            result = Main.getSqlManager().send(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (!result)
            throw new IllegalArgumentException("Failed to add an element to the collection");
        data.add(route);
        updateCollection();
    }

    /**
     * Update collection object
     *
     * @param route  Existing organization
     * @param caller User performing the update
     */
    public void update(Route route, User caller) {
        updateCollection();
        Iterator<Route> iterator = data.iterator();

        while (iterator.hasNext()) {
            Route existingRoute = iterator.next();
            if (Objects.equals(existingRoute.getId(), route.getId())) {
                if (existingRoute.getOwner().getId() != caller.getId())
                    throw new NullPointerException("You do not have permissions to edit someone else's object");

                try {
                    PreparedStatement sql = Main.getSqlManager().getConnection()
                            .prepareStatement("UPDATE collection SET route_name = ?, coordinates_x = ?, coordinates_y = ?, distance = ?,  location_x = ?, location_y = ?, location_name = ? WHERE id = ?;");
                    sql.setString(1, route.getName());
                    sql.setFloat(2, route.getCoordinates().getX());
                    sql.setInt(3, route.getCoordinates().getY());
                    sql.setFloat(4, route.getDistance());
                    sql.setFloat(5, route.getLocation().getX());
                    sql.setDouble(6, route.getLocation().getY());
                    sql.setString(7, route.getLocation().getName());
                    sql.setInt(8, route.getId());

                    boolean result = Main.getSqlManager().send(sql);

                    if (!result)
                        throw new IllegalArgumentException("Failed to update a collection element");

                    // Remove the old route and add the updated one
                    iterator.remove();
                    data.add(route);

                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(StorageManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        throw new NullPointerException("Editable object not found");
    }


    /**
     * Remove object by id
     * @param id ID
     */
    public void remove(int id, User caller) {
        updateCollection();
        Iterator<Route> iterator = data.iterator();

        while (iterator.hasNext()) {
            Route existingRoute = iterator.next();
            if (existingRoute.getId() == id) {
                if (existingRoute.getOwner().getId() != caller.getId())
                    throw new NullPointerException("You do not have permission to delete someone else's object");

                boolean result = Main.getSqlManager().sendRaw("DELETE FROM collection WHERE id IN (" + id + ");");

                if (!result)
                    throw new IllegalArgumentException("Failed to delete a collection object");

                // Remove the object from the HashSet
                iterator.remove();
                return;
            }
        }

        throw new NullPointerException("Object with id " + id + " not found");
    }

    /**
     * Remove objects with greater id
     * @param id ID
     */
    public void removeGreater(int id, User caller) {
        updateCollection();
        Iterator<Route> iterator = data.iterator();

        while (iterator.hasNext()) {
            Route existingRoute = iterator.next();
            if (existingRoute.getId() > id && existingRoute.getOwner().getId() == caller.getId()) {
                boolean result = Main.getSqlManager().sendRaw("DELETE FROM collection WHERE id IN (" + existingRoute.getId() + ");");

                if (!result)
                    throw new IllegalArgumentException("Failed to delete a collection object");

                // Remove the object from the HashSet
                iterator.remove();
            }
        }
    }

    /**
     * Remove the last element of the collection owned by the caller.
     *
     * @param caller User performing the removal
     */
    public void removeLast(User caller) {
        updateCollection();

        // Find the last element owned by the caller based on the highest ID
        Route toRemove = data.stream()
                .filter(o -> o.getOwner().getId() == caller.getId())
                .max((o1, o2) -> Integer.compare(o1.getId(), o2.getId()))
                .orElse(null);

        if (toRemove != null)
            remove(toRemove.getId(), caller); // Remove the found element
        else
            throw new NullPointerException("The last item is not yours or the collection is empty");
    }

    /**
     * Clear the collection by removing all elements owned by the caller.
     *
     * @param caller User performing the removal
     */
    public void clear(User caller) {
        removeGreater(0, caller); // Remove all elements with IDs greater than 0 owned by the caller
        //data.clear();
    }
    /**
     * Reverse collection elements
     */
    /*public void reverse() {
        List<Route> dataList = new ArrayList<>(data);
        int size = dataList.size();
        for (int i = 0; i < size / 2; i++) {
            Route temp = dataList.get(i);
            dataList.set(i, dataList.get(size - i - 1));
            dataList.set(size - i - 1, temp);
        }
        data = new HashSet<>(dataList);
    }

    /**
     * Save collection to the file
     *
     * @param filename File name
     * @throws IOException Exceptions with saving, i.e. access exceptions


    public void save(String filename) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write("["); // Открываем JSON-массив
            boolean isFirst = true;
            for (Route route : data) {
                if (!isFirst) {
                    fileWriter.write(","); // Добавляем запятую между объектами JSON, кроме первого
                } else {
                    isFirst = false;
                }
                String jsonData = gson.toJson(route); // Serialize the Route object to JSON
                fileWriter.write(jsonData); // Write the JSON data to the file
            }
            fileWriter.write("]"); // Закрываем JSON-массив
        }
    }*/



    /**
     * Get object by ID
     *
     * @param id ID
     * @return Route or null
     */
    public Route get(int id) {
        for (Route route : data) {
            if (route.getId() == id) {
                return route;
            }
        }
        return null;
    }

    /**
     * Get max Id of existing objects
     *
     * @return Max id
     */
    public int getMaxId() {
        int result = 0;
        for (Route route : data)
            result = Math.max(result, route.getId());
        return result;
    }

    /**
     * Get all objects
     *
     * @return All routes
     */
    public HashSet<Route> getAll() {
        return data;
    }

    /**
     * Get collection initialization date
     *
     * @return Initialization date
     */
    public LocalDate getInitializationDate() {
        return initializationDate;
    }

    /**
     * Updates the collection data from the database.
     * Retrieves and populates the collection with data from the database table 'collection' joined with 'users'
     * to include information about the owner of each route.
     * Clears the existing collection data and replaces it with the updated data from the database.
     * Sets the initialization date to the current date.
     *
     * @throws IllegalArgumentException if there is an issue retrieving or processing collection data from the database.
     */

    public void updateCollection() {
        ResultSet resultSet = Main.getSqlManager().getRaw("SELECT collection.*, users.username FROM collection JOIN users ON collection.owner = users.id ORDER BY id ASC");
        if(resultSet == null)
            throw new IllegalArgumentException("Failed to get collection data");
        data.clear();
        try {
            while(resultSet.next()) {
                Route route = new Route(
                        resultSet.getInt("id"),
                        resultSet.getString("route_name"),
                        new Coordinates(
                                resultSet.getFloat("coordinates_x"),
                                resultSet.getInt("coordinates_y")
                        ),
                        resultSet.getString("creation_date"),
                        resultSet.getFloat("distance"),
                        new Location(
                                resultSet.getFloat("location_x"),
                                resultSet.getDouble("location_y"),
                                resultSet.getString("location_name")
                        ),
                        new User(
                                resultSet.getInt("owner"),
                                resultSet.getString("username")
                        )
                );
                data.add(route);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        this.initializationDate = LocalDate.now();
    }

    /**
     * Authorizes a user with the provided credentials.
     * This method attempts to authenticate a user by querying the database
     * with the given username and hashed password.
     *
     * @param credentials UserCredentials object containing the username and password.
     * @return User object if authentication is successful, or null if not.
     * @throws IllegalArgumentException if there is an issue retrieving user data from the database.
     */
    public User authorizeUser(UserCredentials credentials) {
        if(credentials == null || credentials.getUsername() == null || credentials.getPassword() == null)
            return null;
        ResultSet resultSet = null;
        try {
            PreparedStatement sql = Main.getSqlManager().getConnection().prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            sql.setString(1, credentials.getUsername());
            sql.setString(2, Utils.hashPassword(credentials.getPassword()));
            resultSet = Main.getSqlManager().get(sql);
        }
        catch(SQLException ex) {}
        if(resultSet == null)
            throw new IllegalArgumentException("Failed to get user data");
        try {
            while(resultSet.next())
                return new User(resultSet.getInt("id"), resultSet.getString("username"));
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new user with the provided credentials.
     * This method inserts a new user into the database with the specified
     * username and hashed password.
     *
     * @param credentials UserCredentials object containing the username and password.
     * @return true if user creation is successful, or false if not.
     */
    public boolean createUser(UserCredentials credentials) {
        try {
            PreparedStatement sql = Main.getSqlManager().getConnection().prepareStatement("INSERT INTO users (username, password) VALUES (?, ?);");
            sql.setString(1, credentials.getUsername());
            sql.setString(2, Utils.hashPassword(credentials.getPassword()));
            return Main.getSqlManager().send(sql);
        } catch (SQLException ex) {
            return false;
        }
    }

}


