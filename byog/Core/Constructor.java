package byog.Core;

//import byog.SaveDemo.World;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Constructor extends WorldGenerator implements Serializable {
    private TETile [][] myWorld;

    /*default constructor for compilation */
    public Constructor() {

    }

    /* constructor that takes in a TETile world */
    public Constructor(TETile [][] world) {
        myWorld = world;
    }

    /* checks if the coordinate is out of bound */
    public static int isOutOfBounds(int x, int bound) {
        if (x < 0) {
            return 0;
        } else if (x >= bound) {
            return bound - 1;
        } else {
            return x;
        }
    }

    /* checks if the position is out of bonds */
    public static Position isPositionOutOfBounds(Position pos) {
        return new Position(isOutOfBounds(pos.getX(), Game.WIDTH), isOutOfBounds(pos.getY(), Game.HEIGHT));
    }

    /* makes horizontal line of tiles of given length and direction */
    public Position makeHorizontal(int length, int direction, Position pos, TETile tile) {
        if (direction == 2) {
            return buildLeft(length, pos, tile);
        } else {
            return buildRight(length, pos, tile);
        }
    }

    /* makes vertical line of tiles given a length and direction */
    public Position makeVertical(int length, int direction, Position pos, TETile tile) {
        if (direction == 0) {
            return buildUp(length, pos, tile);
        } else {
            return buildDown(length, pos, tile);
        }
    }

    /* changes the tile at a given position */
    public TETile changeTile(TETile current, TETile change, Position pos) {
        if (/*pos.getX() == 0 || */pos.getX() == Game.WIDTH - 1) {
            return change;
        }
        if (/*pos.getY() == 0 || */pos.getY() == Game.HEIGHT - 1) {
            return change;
        }
        return current;
    }
    private void placeTile(int x, int y, TETile tile) {
        TETile currTile = myWorld[x][y];
        TETile temp = changeTile(tile, Tileset.WALL, new Position(x, y));
        TETile temp1 = tile;
        if (temp != tile) {
            tile = changeTile(tile, Tileset.WALL, new Position(x, y));
            myWorld[x][y] = tile;
            tile = temp1;
        } else if (!currTile.equals(Tileset.FLOOR)) {
            myWorld[x][y] = tile;
        }
    }
    /* builds tiles in the left direction */
    public Position buildLeft(int length, Position pos, TETile tile) {
        int end = pos.getX() - length;
        end = isOutOfBounds(end, Game.WIDTH);
        pos = isPositionOutOfBounds(pos);
        for (int x = pos.getX(); x > end; x--) {
            placeTile(x, pos.getY(),tile);
        }
        return new Position(end + 1, pos.getY());
    }

    /* builds tiles in the right direction */
    public Position buildRight(int length, Position pos, TETile tile) {
        int end = pos.getX() + length;
        end = isOutOfBounds(end, Game.WIDTH);
        pos = isPositionOutOfBounds(pos);
        TETile changeTo = Tileset.WALL;
        for (int x = pos.getX(); x < end; x++) {
            placeTile(x, pos.getY(), tile);
        }
        return new Position(end - 1, pos.getY());
    }


    /* builds tiles in the up direction */
    public Position buildUp(int length, Position pos, TETile tile) {
        int end = pos.getY() + length;
        end = isOutOfBounds(end, Game.HEIGHT);
        pos = isPositionOutOfBounds(pos);
        for (int y = pos.getY(); y < end; y++) {
            placeTile(pos.getX(), y, tile);
        }
        return new Position(pos.getX(), end - 1);
    }

    /* builds tiles in the down direction */
    public Position buildDown(int length, Position pos, TETile tile) {
        int end = pos.getY() - length;
        end = isOutOfBounds(end, Game.HEIGHT);
        pos = isPositionOutOfBounds(pos);
        for (int y = pos.getY(); y > end; y--) {
            placeTile(pos.getX(), y, tile);
        }
        return new Position(pos.getX(), end + 1);
    }

    /* closes halls */
    private void closeHall(Position[] pos) {
        if ((pos[0].getX() == pos[1].getX())) {
            myWorld[pos[0].getX()] [pos[0].getY()] = Tileset.WALL;

        } else {
            myWorld[pos[0].getX() + 1] [pos[0].getY()] = Tileset.WALL;
        }
    }


    /* hth is ishalltohall
     * used to make a hall
     */
    public Position[] makeHall(int length, int direction, Position pos, Boolean hth) {
        Position[] poss = new Position[2];
        if (direction < 2) {
            if (pos.getX() >= myWorld.length - 2) {
                pos.setX(myWorld.length - 3);
            }
            poss = makeVHall(length, direction, pos, hth);
        } else {
            if (pos.getY() <= 1) {
                pos.setY(2);
            }
            poss = makeHHall(length, direction, pos, hth);
        }
        closeHall(poss);
        return poss;
    }

    /* hth is halltohall makeVHall is makeverticalHall
     * makes a vertical hall used in makeHall
     * */
    private Position[] makeVHall(int length, int direction, Position pos, Boolean hth) {
        makeVertical(length, direction, new Position(pos.getX() + 1, pos.getY()), Tileset.FLOOR);
        Position pos1 = makeVertical(length, direction, pos, Tileset.WALL);
        Position temp = new Position(pos.getX() + 2, pos.getY());
        Position pos2 = makeVertical(length, direction, temp, Tileset.WALL);
        Position[] poss = {pos1, pos2};
        if (hth) {
            myWorld[pos.getX() + 1] [pos.getY()] = Tileset.WALL;
        }
        return poss;
    }

    /* hth = hallto hall this is makehorizontalHall
     * makes a horizontal hall used in makeHall
     * */
    private Position[] makeHHall(int length, int direction, Position pos, Boolean hth) {
        makeHorizontal(length, direction, new Position(pos.getX(), pos.getY() - 1), Tileset.FLOOR);
        Position pos1 = makeHorizontal(length, direction, pos, Tileset.WALL);
        Position temp = new Position(pos.getX(), pos.getY() - 2);
        Position pos2 = makeHorizontal(length, direction, temp, Tileset.WALL);
        Position[] poss = {pos2, pos1};
        if (hth) {
            myWorld[pos.getX()][pos.getY() - 1] = Tileset.WALL;
        }
        return poss;
    }
}