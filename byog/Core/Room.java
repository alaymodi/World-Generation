package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class Room extends WorldGenerator implements Serializable {
    private Position bottomLeftEdge;
    private Position bottomRightEdge;
    private Position topLeftEdge;
    private Position topRightEdge;
    private Position pos;
    private int width;
    private int height;
    private int direc;
    private Constructor construct;
    private static ArrayList<Room> thepos = new ArrayList<Room>();
    private TETile [][] myWorld;

    /* default constructor for a room */
    public Room() {

    }

    /* constructor for room that takes in multiple parameters
        int w is used for the width and int h is used for the height
     */
    public Room(TETile [][] world, Constructor construct, int w, int h, Position pos, int direc) {
        this.construct = construct;
        this.width = w;
        this.height = h;
        this.pos = pos;
        this.direc = direc;
        myWorld = world;
        bottomLeftEdge = new Position(pos.getX(), pos.getY());
        bottomRightEdge = new Position(pos.getX() + width + 1, pos.getY());
        topLeftEdge = new Position(pos.getX(), pos.getY() + height + 1);
        topRightEdge = new Position(pos.getX() + width + 1, pos.getY() + height + 1);
    }

    /* creates a room while also gathering the information of the coordinates from the
        four corners
     */
    public Room makeRoom() {
        int end = pos.getY() + height;
        if (construct.isXOutOfBounds(end) != end) {
            end = Game.WIDTH - 2;
        }
        for (int y = pos.getY() + 1; y <= end; y++) {
            if (y != 39) {
                construct.buildRight(width, new Position(pos.getX() + 1, y), Tileset.FLOOR);
            }
        }
        construct.makeHorizontal(width + 2, 3, pos, Tileset.WALL);
        construct.makeVertical(height + 2, 0, pos, Tileset.WALL);
        Position temp = new Position(pos.getX(), pos.getY() + height + 1);
        construct.makeHorizontal(width + 2, 3, temp, Tileset.WALL);
        temp = new Position(pos.getX() + width + 1, pos.getY());
        construct.makeVertical(height + 2, 0, temp, Tileset.WALL);
        thepos.add(this);
        return this;
    }

    /* checks to see if there is overlap between creation of rooms */
    public static boolean isOverlap(Position pos) {
        for (int i = 0; i < thepos.size(); i++) {
            Room rooms = thepos.get(i);
            if (pos.getX() == rooms.bottomLeftEdge.getX()) {
                if (pos.getY() == rooms.bottomLeftEdge.getY()) {
                    return true;
                }
            }
        }

        return false;
    }

    /* get method to return the ArrayList of rooms */
    public static ArrayList<Room> getArray() {
        return thepos;
    }

    /* set method to alter contents of Array */
    public static void setArray(ArrayList<Room> array) {
        thepos = array;
    }

    /* get method to return the bottomLeftEdge position object */
    public Position getBottomLeftEdge() {
        return bottomLeftEdge;
    }

    /* get method to return the bottomRightEdge position object */
    public Position getBottomRightEdge() {
        return bottomRightEdge;
    }

    /* get method to return the topLeftEdge position object */
    public Position getTopLeftEdge() {
        return topLeftEdge;
    }

    /* get method to return the topRightEdge position object */
    public Position getTopRightEdge() {
        return topRightEdge;
    }

    /* get method to return the width */
    public int getWidth() {
        return width;
    }

    /* get method to return the height */
    public int getHeight() {
        return height;
    }
}