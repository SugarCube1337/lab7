package org.lab7;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.lab7.collection.data.Route;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The `StorageManager` class is responsible for managing a collection of `Route` objects.
 * It allows for saving, loading, adding, updating, and performing various operations on the collection.
 */
public class StorageManager {

    private String filename;
    private LocalDate initializationDate;
    public static String autosaveName = System.getProperty("java.io.tmpdir") + "autosave.json";
    private HashSet<Route> data = new HashSet<>();

    /**
     * Constructs a `StorageManager` with the specified filename.
     *
     * @param filename The name of the file used for saving and loading the collection.
     */
    public StorageManager(String filename) {
        this.filename = filename;
        this.initializationDate = LocalDate.now();
        try {
            if (new File(autosaveName).exists()) {
                System.out.print("You have unsaved data, write '+' to load it: ");
                if ((new Scanner(System.in)).nextLine().equals("+")) {
                    parse(autosaveName);
                    System.out.println("The data was downloaded from an automatic save.");
                } else {
                    new File(autosaveName).delete();
                    parse(filename);
                }
            } else
                parse(filename);
        } catch (Exception exception) {
            System.out.println("Failed to read corrupted data from the file. The collection contains " + data.size() + " elements.");
        }
    }

    /**
     * Convert JSON file to a collection objects
     *
     * @param filename File name
     * @throws IOException Exceptions with saving, i.e. access exceptions, not found exceptions
     */
    public void parse(String filename) throws IOException {
        try (FileReader fileReader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Route>>() {
            }.getType();
            List<Route> routes = gson.fromJson(fileReader, listType);
            data.addAll(routes);
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Add object to collection
     *
     * @param route New Route
     */
    public void add(Route route) {
        data.add(route);
    }

    /**
     * Update collection object
     *
     * @param route Existing route
     */
    public void update(Route route) {
        Route foundRoute = null;
        for (Route existingRoute : data) {
            if (Objects.equals(existingRoute.getId(), route.getId())) {
                foundRoute = existingRoute;
                break;
            }
        }

        if (foundRoute != null) {
            data.remove(foundRoute); // Удаляем существующий элемент
            data.add(route); // Добавляем новый элемент
        } else {
            throw new NullPointerException("Editable object not found");
        }
    }

    /**
     * Remove object by id
     *
     * @param id ID
     */
    public void remove(int id) {
        Route routeToRemove = null;
        for (Route route : data) {
            if (route.getId() == id) {
                routeToRemove = route;
                break;
            }
        }

        if (routeToRemove != null) {
            data.remove(routeToRemove);
        } else {
            throw new NullPointerException("An object with id " + id + " not found");
        }
    }

    /**
     * Remove objects with greater id
     *
     * @param id ID
     */
    public void removeGreater(int id) {
        Set<Route> toRemove = new HashSet<>();
        for (Route route : data) {
            if (route.getId() > id) {
                toRemove.add(route);
            }
        }

        data.removeAll(toRemove);
    }

    /**
     * Remove last element of the collection
     */
    public void removeLast() {
        if (!data.isEmpty())
            data.remove(data.size() - 1);
        else
            throw new NullPointerException("The collection is empty");
    }

    /**
     * Clear collection
     */
    public void clear() {
        data.clear();
    }

    /**
     * Reverse collection elements
     */
    public void reverse() {
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
     */

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
    }



    /**
     * Get active file name
     *
     * @return File name
     */
    public String getFilename() {
        return filename;
    }

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


}


