package byog.Core;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Character extends WorldGenerator implements Serializable {
    private int power = 5;
    private Position characPos;
    private TETile coveredTile  = Tileset.FLOOR;
    private Random rand;
    private TETile[][]  myWorld;
    private Map<Integer, Position> myDoors;

    /* constructor to make a character */
    public Character(Random rand, TETile [][] world, Map<Integer, Position> myDoors) {
        this.rand = rand;
        myWorld = world;
        this.myDoors = myDoors;
    }

    /* makes a character */
    public void makeCharacter() {
        Position pos = createCharaPosition();
        while (!myWorld[pos.getX()][pos.getY()].description().equals("floor")){
            pos = createCharaPosition();
        }
        coveredTile = myWorld[pos.getX()][pos.getY()];
        myWorld[pos.getX()][pos.getY()] = Tileset.PLAYER;
        characPos = pos;
    }

    /* moves the character */
    public Position move(Position newPos, int direction) {
        if (isDoor(newPos)) {
            int x = getRandomDoor();
            Position pos = myDoors.get(x);
            Position up = new Position(pos.getX(), pos.getY() + 1);
            Position down = new Position(pos.getX(), pos.getY() - 1);
            Position right = new Position(pos.getX() + 1, pos.getY());
            Position left = new Position(pos.getX() - 1, pos.getY());
            if (!isWall(up) && !isNothing(up)) {
                newPos = up;
            } else if (!isWall(down) && !isNothing(down)) {
                newPos = down;
            } else if (!isWall(left) && !isNothing(left)) {
                newPos = left;
            } else {
                newPos = right;
            }
            myWorld[characPos.getX()][characPos.getY()] = Tileset.FLOOR;
            myWorld[newPos.getX()][newPos.getY()] = Tileset.PLAYER;
            characPos = newPos;
        }
        if (!isWall(newPos)) {
            myWorld[characPos.getX()][characPos.getY()] = Tileset.FLOOR;
            myWorld[newPos.getX()][newPos.getY()] = Tileset.PLAYER;
            characPos = newPos;
        }
        return characPos;
    }

    /* breaks wall */
    public Position breakWall(Position newPos, int direction) {
        if (isWallBreakable(newPos, direction)) {
            myWorld[newPos.getX()][newPos.getY()] = Tileset.PLAYER;
            myWorld[characPos.getX()][characPos.getY()] = Tileset.FLOOR;
            characPos = newPos;
        }
        return characPos;
    }

    /* returns int for a random door */
    public int getRandomDoor() {
        int x = Math.abs(this.rand.nextInt()) % myDoors.size();
        return x;
    }

    /* moves character up */
    public Position moveUP(boolean isBreakWall) {
        Position newPos = new Position(characPos.getX(), characPos.getY() + 1);
        if (newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
            if (!isBreakWall) {
                return move(newPos, 0);
            } else {
                return breakWall(newPos, 0);
            }
        }
        return null;
    }

    /* moves character down */
    public Position moveDown(boolean isBreakWall) {
        Position newPos = new Position(characPos.getX(), characPos.getY() - 1);
        if (newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
            if (!isBreakWall) {
                return move(newPos, 1);
            } else {
                return breakWall(newPos, 1);
            }
        }
        return null;
    }

    /* moves character right */
    public Position moveRight(boolean isBreakWall) {
        Position newPos = new Position(characPos.getX() + 1, characPos.getY());
        if (newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
            if (!isBreakWall) {
                return move(newPos, 3);
            } else {
                return breakWall(newPos, 3);
            }
        }
        return null;
    }

    /* moves character left */
    public Position moveLeft(boolean isBreakWall) {
        Position newPos = new Position(characPos.getX() - 1, characPos.getY());
        if (newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
            if (!isBreakWall) {
                return move(newPos, 2);
            } else {
                return breakWall(newPos, 2);
            }
        }
        return null;
    }

    /* checks if wall is breakable */
    public boolean isWallBreakable(Position newPos, int direction) {
        boolean bool = power > 0;
        boolean bool1 = (myWorld[newPos.getX()][newPos.getY()].description().equals("wall"));
        boolean bool2 = isBreakable(newPos, direction);
        if (bool && bool1 && bool2) {
            power -= 1;
            return true;
        }
        return false;
    }

    /* checks if its breakable */
    private boolean isBreakable(Position pos, int direction) {
        if (direction == 0) {
            Position newPos = new Position(pos.getX(), pos.getY() + 1);
            if (!newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
                return false;
            }
            return (myWorld[pos.getX()][pos.getY() + 1].description().equals("floor"));
        } else if (direction == 1) {
            Position newPos = new Position(pos.getX(), pos.getY() - 1);
            if (!newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
                return false;
            }
            return (myWorld[pos.getX()][pos.getY() - 1].description().equals("floor"));
        } else if (direction == 2) {
            Position newPos = new Position(pos.getX() - 1, pos.getY());
            if (!newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
                return false;
            }
            return (myWorld[pos.getX() - 1][pos.getY()].description().equals("floor"));
        } else {
            Position newPos = new Position(pos.getX() + 1, pos.getY());
            if (!newPos.equals(Constructor.isPositionOutOfBounds(newPos))) {
                return false;
            }
            return (myWorld[pos.getX() + 1][pos.getY()].description().equals("floor"));
        }
    }

    /* checks if its nothing */;
    private boolean isNothing(Position pos) {
        return myWorld[pos.getX()][pos.getY()].description().equals("nothing");
    }

    /* checks if its a wall */
    private boolean isWall(Position pos) {
        return myWorld[pos.getX()][pos.getY()].description().equals("wall");
    }

    /* checks if its a door */
    private boolean isDoor(Position pos) {
        return myWorld[pos.getX()][pos.getY()].description().equals("locked door");
    }

    /* creates the position of the character */
    private Position createCharaPosition() {
        int xPos = Math.abs(this.rand.nextInt()) % 100;
        int yPos = Math.abs(this.rand.nextInt()) % 100;
        while (xPos >= Game.WIDTH) {
            xPos = xPos % 10;
        }
        while (yPos >= Game.HEIGHT) {
            yPos = yPos % 10;
        }
        return new Position(xPos, yPos);
    }

    /* sets power variable */
    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    /* gets the positon of the character */
    public Position getCharacPos() {
        return characPos;
    }

    /* resets the character the position */
    public void resetCharacPos(Position pos) {
        characPos = pos;
    }
}