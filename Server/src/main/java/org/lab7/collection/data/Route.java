package org.lab7.collection.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lab7.Main;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Represents a route with a unique ID, name, coordinates, creation date, start and end locations, and distance.
 */
public class Route implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L - 3L;
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Float distance;
    private Location location;


    public Route() {
        id = Main.getStorageManager().getMaxId() + 1;
        creationDate = LocalDateTime.now();
    }

    private Route(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate, Float distance, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.distance = distance;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("The name value cannot be empty");
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null)
            throw new IllegalArgumentException("The coordinate value cannot be empty");
        this.coordinates = coordinates;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null)
            throw new IllegalArgumentException("The coordinate value cannot be empty");
        this.location = location;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        if(distance == 0.0f)
            throw new IllegalArgumentException("The distance value cannot be empty");
        this.distance = distance;
    }


    @Override
    public String toString() {
        return "Route[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", location=" + location.toString() +
                ", distance=" + distance +
                ']';
    }

    public static Route parseJSON(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        Integer id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        float x = jsonObject.get("coordinates").getAsJsonObject().get("x").getAsFloat();
        int y = jsonObject.get("coordinates").getAsJsonObject().get("y").getAsInt();
        LocalDateTime creationDate = LocalDateTime.parse(jsonObject.get("creationDate").getAsString());
        float distance = jsonObject.get("distance").getAsFloat();

        JsonObject locationObject = jsonObject.get("location").getAsJsonObject();
        float xL = locationObject.get("x").getAsFloat();
        float yL = locationObject.get("y").getAsFloat();
        String nameL = locationObject.get("name").getAsString();
        Location location = new Location(xL, yL, nameL);

        Coordinates coordinates = new Coordinates(x, y);
        return new Route(id, name, coordinates, creationDate, distance, location);
    }
}