package org.lab7.collection.data;


import java.io.Serial;
import java.io.Serializable;

/**
 * A class representing a pair of coordinates (x, y).
 */
public class Coordinates implements Serializable {

    @Serial
    private static final long serialVersionUID = 6529685098267757690L-1L;

    private float x; //Поле не может быть null
    private int y;


    public Coordinates(float x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        if (Float.isNaN(x)) {
            throw new IllegalArgumentException("The value of X must be a number");
        }
        this.x = x;
    }

    public void setY(int y) {
        try {
            Integer.parseInt(String.valueOf(y));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The value of Y must be a number");
        }

        this.y = y;
    }

    public float getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates[" +
                "x=" + x +
                ", y=" + y +
                ']';
    }
}
