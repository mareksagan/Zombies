package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class for the Zombie Apocalypse assignment.
 *
 * @author Marek Sagan
 */
public class Zombies {

    /**
     * Minimum zombie ID. Synchronised here and incremented per zombie.
     */
    static int zombieCounter = 1;
    /**
     * Cache used for storing the zombie final positions
     */
    static TreeSet<String> zombiePositions = new TreeSet<>();
    /**
     * Cache used for storing the creature final positions (if any)
     */
    static TreeSet<String> creaturePositions = new TreeSet<>();
    /**
     * Class logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Zombies.class);

    public static void main(String[] args) throws IOException {
        parseFromConsole(System.in, System.out);
    }

    /**
     * Method parsing the input arguments from console by default
     *
     * @param in            Input stream
     * @param out           Output stream
     * @throws IOException
     */
    public static void parseFromConsole(InputStream in, OutputStream out) throws IOException {
        try (Scanner scanner = new Scanner(in)) {
            // Parsing the grid size
            int size = scanner.nextInt();

            // Parsing the grid size
            boolean[][] creatureMap = new boolean[size][size];

            // Parsing the 1st zombie start coordinates
            String zombieStart = scanner.next().trim();
            String[] zombieStartXY = zombieStart.replace("(", "")
                    .replace(")", "")
                    .split(",");
            int zombieStartX = Integer.parseInt(zombieStartXY[0]);
            int zombieStartY = Integer.parseInt(zombieStartXY[1]);

            // Parsing and storing the creature positions
            String creaturesStart = scanner.next();
            parseCreatures(creaturesStart, creatureMap);

            // Parsing and storing the zombie path
            char[] zombiePath = scanner.next().toCharArray();

            // Grid limit needs to be smaller than the size
            String result = run(size-1, zombieStartX, zombieStartY, creatureMap, zombiePath);
            out.write(result.getBytes());
        } catch (Exception e) {
            out.write("Wrong input".getBytes());
        }
    }

    /**
     * Method used for parsing the creatures' map
     *
     * @param input         Input string containing the creatures' coordinates
     * @param creatureMap   Map to be initialized
     */
    public static void parseCreatures(String input, boolean[][] creatureMap) {
        String[] creatures = input.replace(")(", " ")
                .replace("(", "")
                .replace(")", "")
                .split(" ");
        for (String creature : creatures) {
            String[] creatureXY = creature.split(",");
            int creatureX = Integer.parseInt(creatureXY[0]);
            int creatureY = Integer.parseInt(creatureXY[1]);
            creatureMap[creatureX][creatureY] = true;
            creaturePositions.add("("+creatureX+","+creatureY+")");
        }
    }

    /**
     * Method simulating the zombie movements and infections
     *
     * @param limit         Limit of the grid values
     * @param x             First zombie X coordinate
     * @param y             First zombie Y coordinate
     * @param creatures     Creatures' map
     * @param path          Zombie path
     * @return              Result string with coordinates of zombies and creatures
     */
    public static String run(int limit, int x, int y, boolean[][] creatures, char[] path) {
        // Resulting string
        StringBuilder sb = new StringBuilder();

        // Simulating the zombie moves and infections
        moveZombie(limit, x, y, creatures, path, 1);

        // Final results
        sb.append("zombies’ positions:\n");
        for (String zombiePosition : zombiePositions) {
            sb.append(zombiePosition);
        }
        sb.append("\n");
        sb.append("creatures’ positions:\n");
        if (creaturePositions.isEmpty()) {
            sb.append("none");
        } else {
            for (String creaturePosition : creaturePositions) {
                sb.append(creaturePosition);
            }
        }

        // Cleaning up the zombie cache
        if (!zombiePositions.isEmpty()) {
            zombiePositions.clear();
        }

        // Cleaning up the creatures cache
        if (!creaturePositions.isEmpty()) {
            creaturePositions.clear();
        }

        return sb.toString();
    }

    /**
     * Method used to simulate zombie movement and infection spreading.
     *
     * @param limit     Limit of the grid values
     * @param x         Initial zombie position on the X axis
     * @param y         Initial zombie position on the Y axis
     * @param creatures Creatures' map
     * @param path      Zombie path
     * @param id        Assigned zombie ID
     */
    public static void moveZombie(int limit, int x, int y, boolean[][] creatures, char[] path, int id) {
        int zombieX = x;
        int zombieY = y;
        // Queue used to handle the order of the new zombie moves
        Queue<Integer[]> infections = new LinkedList<>();

        for (char move : path) {
            if (move == 'U') {
                if (zombieY == 0) {
                    zombieY = limit;
                } else {
                    zombieY--;
                }
            } else if (move == 'D') {
                if (zombieY == limit) {
                    zombieY = 0;
                } else {
                    zombieY++;
                }
            } else if (move == 'L') {
                if (zombieX == 0) {
                    zombieX = limit;
                } else {
                    zombieX--;
                }
            } else if (move == 'R') {
                if (zombieX == limit) {
                    zombieX = 0;
                } else {
                    zombieX++;
                }
            }

            if (creatures[zombieX][zombieY]) {
                infections.add(new Integer[]{zombieX, zombieY});
                creatures[zombieX][zombieY] = false;
                creaturePositions.remove("("+zombieX+","+zombieY+")");
                logger.info("zombie " + id + " infected creature at " + "(" + zombieX + "," + zombieY + ")");
            }

            logger.info("zombie " + id + " moved to " + "(" + zombieX + "," + zombieY + ")");
        }

        while (!infections.isEmpty()) {
            // FIFO order of new zombie moves maintained with a queue
            moveZombie(limit, infections.peek()[0], infections.peek()[1], creatures, path, ++zombieCounter);
            infections.remove();
        }

        zombiePositions.add("("+zombieX+","+zombieY+")");
    }
}
