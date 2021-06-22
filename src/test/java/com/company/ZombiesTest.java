package com.company;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests the Zombies class behavior.
 */
public class ZombiesTest {
    private static final String NON_NUMERIC_STRING = "a";
    private static final String WRONG_INPUT = "Wrong input";

    private static final int USECASE_SIZE = 4;
    private static final int USECASE_ZOMBIE_X = 3;
    private static final int USECASE_ZOMBIE_Y = 1;
    private static final String USECASE_CREATURES = "(0,1)(1,2)(1,1)";
    private static final String USECASE_PATH = "RDRU";
    private static final String USECASE_OUTPUT = "zombies’ positions:" + "\n" + "(1,1)(2,1)(3,1)(3,2)" + "\n" +
            "creatures’ positions:" + "\n" + "none";

    private static final int NO_INFECTION_SIZE = 20;
    private static final int NO_INFECTION_ZOMBIE_X = 1;
    private static final int NO_INFECTION_ZOMBIE_Y = 1;
    private static final String NO_INFECTION_CREATURES = "(5,5)";
    private static final String NO_INFECTION_PATH = "RR";
    private static final String NO_INFECTION_OUTPUT = "zombies’ positions:" + "\n" + "(3,1)" + "\n" +
            "creatures’ positions:" + "\n" + "(5,5)";

   @Test
   public void checkIfWrongArgumentsStopsProgram() {
        try {
            InputStream in = new ByteArrayInputStream(NON_NUMERIC_STRING.getBytes());
            OutputStream out = new ByteArrayOutputStream();
            Zombies.parseFromConsole(in, out);
            assertEquals(WRONG_INPUT, out.toString());
        } catch (IOException e) {
            fail("Test failed");
        }
    }

    @Test
    public void checkIfUseCaseWorks() {
       boolean[][] creatures = new boolean[USECASE_SIZE][USECASE_SIZE];
       Zombies.parseCreatures(USECASE_CREATURES, creatures);
       assertEquals(USECASE_OUTPUT, Zombies.run(USECASE_SIZE-1, USECASE_ZOMBIE_X, USECASE_ZOMBIE_Y, creatures,
               USECASE_PATH.toCharArray()));
    }

    @Test
    public void checkIfNoInfectionWorks() {
        boolean[][] creatures = new boolean[NO_INFECTION_SIZE][NO_INFECTION_SIZE];
        Zombies.parseCreatures(NO_INFECTION_CREATURES, creatures);
        assertEquals(NO_INFECTION_OUTPUT, Zombies.run(NO_INFECTION_SIZE-1, NO_INFECTION_ZOMBIE_X,
                NO_INFECTION_ZOMBIE_Y, creatures, NO_INFECTION_PATH.toCharArray()));
    }
}
