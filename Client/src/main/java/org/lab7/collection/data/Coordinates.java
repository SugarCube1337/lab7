package org.lab7.collection.data;


import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

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

    public void setX(Scanner scan) {
        try {
            x = scan.nextFloat();
        } catch (Exception ex) {
            scan.nextLine();
            throw new IllegalArgumentException("The value of X must be a number");
        }

    }

    public void setY(Scanner scan) {
        try {
            y = scan.nextInt();
        } catch (Exception ex) {
            scan.nextLine();
            throw new IllegalArgumentException("The Y value must be a number");
        }

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
