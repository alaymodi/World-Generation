package byog.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    /* constructor for a position object */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* get method to get private variable x */
    public int getX() {
        return x;
    }

    /* set method to set private variable x */
    public void setX(int num) {
        this.x = num;
    }

    /* get method to get private variable y */
    public int getY() {
        return y;
    }

    /* set method to set private variable y */
    public void setY(int num) {
        this.y = num;
    }

    /* equals method for position objects to ensure that they are equal */
    public boolean equals(Position pos) {
        return (this.getX() == pos.getX()) && (this.getY() == pos.getY());
    }

}