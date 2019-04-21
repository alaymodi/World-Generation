package byog.Core;
//import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.util.Map;
import java.util.Random;
//import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;

public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     * @Source the coolest floormate for finding my bug
     */
    public void playWithKeyboard() {
        mainMenu();
        boolean check = false;
        while (!StdDraw.hasNextKeyTyped() || !check) {
            if (StdDraw.hasNextKeyTyped()) {
                char lowerCase = StdDraw.nextKeyTyped();
                if (lowerCase == 'n' || lowerCase == 'N') {
                    check = true;
                    newGame("");
                } else if (lowerCase == 's' || lowerCase == 'S') {
                    nameAdd();
                } else if (lowerCase == 'l' || lowerCase == 'L') {
                    check = true;
                    WorldGenerator world = loadWorld();
                    if (world == null) {
                        System.exit(0);
                    } else {
                        Map<Integer, Position> doors = world.getMyDoors();
                        TETile[][] tileWorld = world.getMyWorld();
                        ter.initialize(WIDTH, HEIGHT + 2);
                        ter.renderFrame(tileWorld);
                        Character theChara = new Character(world.getRand(), tileWorld, doors);
                        theChara.resetCharacPos(world.getCharacPos());
                        theChara.setPower(world.getPower());
                        moveInGame(theChara, tileWorld, world, world.getName());
                    }
                } else if (lowerCase == 'q' || lowerCase == 'Q') {
                    check = true;
                    System.exit(0);
                }
            }
        }
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        //generate method has to take in seed and in that method make a worldgenerator
        // also needs to return a 2d array

        //generate(double seedNum);

        boolean checker = false;
        boolean newChecker = false;
        boolean stopChecker = false;
        Long seed;
        String stringSeed = "";
        String lower = input.toLowerCase();
        String rest = "";
        for (int i = 0; i < lower.length(); i++) {
            if (lower.contains("l") || lower.contains("L")) {
                WorldGenerator world = loadWorld();
                TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
                finalWorldFrame = world.getMyWorld();
                Map<Integer, Position> doors = world.getMyDoors();
                Character theChara = new Character(world.getRand(), finalWorldFrame, doors);
                theChara.resetCharacPos(world.getCharacPos());
                moveCharaWithString(theChara, lower.substring(1), world);
                return finalWorldFrame;
            }
            if (lower.substring(i, i + 1).compareTo("n") == 0) {
                newChecker = true;
            } else if (lower.substring(i, i + 1).compareTo("s") == 0) {
                stopChecker = true;
                rest = lower.substring(i + 1);
            }
            if (newChecker && (!stopChecker)) {
                if (!(lower.substring(i, i + 1).compareTo("n") == 0)) {
                    stringSeed += input.substring(i, i + 1);
                }
            }
        }
        seed = Long.valueOf(stringSeed);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        Random randObj = new Random(seed);
        WorldGenerator generator = new WorldGenerator(finalWorldFrame, randObj);
        finalWorldFrame = generator.generateWorld();
        Character theChara = new Character(randObj, finalWorldFrame, generator.getMyDoors());
        theChara.makeCharacter();
        moveCharaWithString(theChara, rest, generator);
        return finalWorldFrame;
    }


    /* moves the character using inputs from a string */
    private void moveCharaWithString(Character chara, String str, WorldGenerator world) {
        String charas = "aAwWsSdD:qQ";
        Boolean check = false;
        for (int i = 0; i < str.length(); i++) {
            String str1 = str.substring(i, i + 1);
            world.resetCharacPos(chara.getCharacPos());
            if (charas.contains(str1)) {
                if (str1.contains("a") || str1.contains("A")) {
                    chara.moveLeft(false);
                } else if (str1.contains("w") || str1.contains("W")) {
                    chara.moveUP(false);
                } else if (str1.contains("s") || str1.contains("S")) {
                    chara.moveDown(false);
                } else if (str1.contains("d") || str1.contains("D")) {
                    chara.moveRight(false);
                } else if (str1.contains(":")) {
                    check = true;
                } else if (str1.contains("q") || str1.contains("Q")) {
                    if (check) {
                        saveWorld(world);
                        break;
                    } else {
                        check = false;
                    }
                }
            }
        }
    }

    /* method to generate the main menu */
    private void mainMenu() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(.5, .75, "CS61B: The Rom");
        StdDraw.text(.5, .5, "New game(N)");
        StdDraw.text(.5, .4, "Start game with name(S)");
        StdDraw.text(.5, .3, "Load game(L)");
        StdDraw.text(.5, .2, "Quit game(Q)");
    }

    /* method used for keyboard inputs if user presses n/N */
    private void newGame(String name) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(.5, .65, "Please enter your choice of seed for ");
        StdDraw.text(.5, .60, "the world followed by s or S");
        boolean check = false;
        String numbers = "1234567890sS";
        String num = "";
        while (!StdDraw.hasNextKeyTyped() || !check) {
            if (StdDraw.hasNextKeyTyped()) {
                char chara = StdDraw.nextKeyTyped();
                if (numbers.indexOf(chara) >= 0) {
                    if (chara != 's' && chara != 'S') {
                        num += chara;
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.text(.5, .65, "Please enter your choice of seed for ");
                        StdDraw.text(.5, .60, "the world followed by s or S");
                        StdDraw.text(.5, .55, num);
                    } else {
                        Long seed = Long.valueOf(num);
                        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
                        Random randObj = new Random(seed);
                        WorldGenerator gen = new WorldGenerator(finalWorldFrame, randObj);
                        ter.initialize(WIDTH, HEIGHT + 2);
                        finalWorldFrame = gen.generateWorld();
                        Character theC = new Character(randObj, finalWorldFrame, gen.getMyDoors());
                        theC.makeCharacter();
                        ter.renderFrame(finalWorldFrame);
                        check = true;
                        moveInGame(theC, finalWorldFrame, gen, name);
                    }
                }
            }
        }
    }

    private void nameAdd() {
        StdDraw.clear(StdDraw.BLACK);
        Font font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(.5, .65, "Please enter your choice of name ");
        StdDraw.text(.5, .60, "Please only enter in numbers or letters");
        StdDraw.text(.5, .55, "and press :n when done");
        boolean check = false;
        boolean check1 = false;
        String possible = "1234567890aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ:";
        String name = "";
        while (!StdDraw.hasNextKeyTyped() || !check) {
            if (StdDraw.hasNextKeyTyped()) {
                char chara = StdDraw.nextKeyTyped();
                if (possible.indexOf(chara) >= 0) {
                    if (check1) {
                        if (chara == 'n' || chara == 'N') {
                            newGame(name);
                            check = true;

                        } else {
                            check1 = false;
                        }
                    } else {
                        if (chara == ':') {
                            check1 = true;
                        } else {
                            name += chara;
                            StdDraw.clear(StdDraw.BLACK);
                            StdDraw.text(.5, .65, "Please enter your choice of name ");
                            StdDraw.text(.5, .60, "Please only enter in numbers or letters");
                            StdDraw.text(.5, .55, "and press :n when done");
                            StdDraw.text(.5, .50, name);
                        }
                    }
                }
            }
        }
    }
    /* method used to allow the character to move */
    private void moveInGame(Character chara, TETile [][] world, WorldGenerator gen, String name) {
        boolean check = false;
        boolean check1 = false;
        boolean breakable = false;
        while (!StdDraw.hasNextKeyTyped() || !check) {
            updateMouseLocation(gen, name);
            gen.resetCharacPos(chara.getCharacPos());
            if (StdDraw.hasNextKeyTyped()) {
                char lowerCase = StdDraw.nextKeyTyped();
                if (lowerCase == 't' || lowerCase == 'T') {
                    breakable = true;
                }
                if (lowerCase == 'w' || lowerCase == 'W') {
                    //go up
                    chara.moveUP(breakable);
                    breakable = false;
                    ter.renderFrame(world);
                } else if (lowerCase == 'a' || lowerCase == 'A') {
                    chara.moveLeft(breakable); // left
                    breakable = false;
                    ter.renderFrame(world);
                } else if (lowerCase == 'd' || lowerCase == 'D') {
                    chara.moveRight(breakable); // right
                    breakable = false;
                    ter.renderFrame(world);
                } else if (lowerCase == 's' || lowerCase == 'S') {
                    chara.moveDown(breakable); // down
                    breakable = false;
                    ter.renderFrame(world);
                } else if (lowerCase == ':') {
                    check1 = true;
                } else if (check1) {
                    if (lowerCase == 'q' || lowerCase == 'Q') {
                        check = true;
                        gen.setPower(chara.getPower());
                        gen.setName(name);
                        saveWorld(gen);
                        System.exit(0);
                    } else {
                        check1 = false;
                    }
                }
            }
        }
    }

    /* constantly updates and provides the time and mouse location
        for the hud
     */
    private void updateMouseLocation(WorldGenerator gen, String name) {
        Constructor construct = new Constructor();
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        Position pos = new Position(x, y);
        LocalDateTime currentTime = LocalDateTime.now();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(20, 41, 80, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        if (x < WIDTH && y < HEIGHT) {
            if (construct.isPositionOutOfBounds(pos).equals(pos)) {
                StdDraw.textLeft(1, 41, gen.getMyWorld()[x][y].description());
            }
        } else {
            StdDraw.textLeft(1, 41, "nothing");
        }
        StdDraw.text(40, 41, name);
        StdDraw.textRight(79, 41, currentTime.toString());
        StdDraw.show();
    }

    /* @source savedemo code thank you Prof Hug
     * code used to save the current world */
    private void saveWorld(WorldGenerator world) {
        File file = new File("savefile.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream output = new ObjectOutputStream(fileStream);
            output.writeObject(world);
            output.close();
        } catch (FileNotFoundException e) {
            System.out.println("No file found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /* @source savedemo code thank you Prof Hug
     * code used to load the world that was saved
     * otherwise exits*/
    private WorldGenerator loadWorld() {
        File file = new File("savefile.txt");
        if (file.exists()) {
            try {
                FileInputStream fileStream = new FileInputStream(file);
                ObjectInputStream input = new ObjectInputStream(fileStream);
                WorldGenerator world = (WorldGenerator) input.readObject();
                input.close();
                return world;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println(e);
                System.exit(0);
            }
        }
        return null;
    }

    /* method used to check if the character is
        any iteration of q/Q, l/L etc..
     */
    private boolean check(char c) {
        boolean q = c != 'q' || c != 'Q';
        boolean l = c != 'l' || c != 'L';
        boolean n = c != 'n' || c != 'N';
        return (q || l || n);
    }

}