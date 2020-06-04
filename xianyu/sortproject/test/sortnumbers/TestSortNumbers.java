package sortnumbers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This code will test your method you write for Sort Numbers, and tell you if it's correct.
 *
 * Don't worry too much about how this works at the moment - you'll learn unit testing later on in the course :)
 */
class TestSortNumbers {

    private SortNumbers cr;

    private PipedInputStream pIn;
    private PipedOutputStream pOut;
    private BufferedReader reader;

    private PrintStream oldOut;


    @BeforeEach
    void setUp() throws IOException {
        cr = new SortNumbers();

        oldOut = System.out;

        // Hack so we can read things that you print using System.out.println
        pIn = new PipedInputStream();
        pOut = new PipedOutputStream(pIn);
        System.setOut(new PrintStream(pOut));
        reader = new BufferedReader(new InputStreamReader(pIn));

    }

    @AfterEach
    void tearDown() throws IOException {

        System.setOut(oldOut);
        reader.close();
        pOut.close();

    }

    @Test
    public void testSortNumberAscending() throws IOException {

        cr.sortNumberByAscending(35, -4, 7, 6); // The numbers are: -4, 6, 7, 35
        assertEquals("The numbers are: -4, 6, 7, 35", reader.readLine());

        cr.sortNumberByAscending(-1, 0, 18, -10); // The numbers are: -10, -1, 0, 18
        assertEquals("The numbers are: -10, -1, 0, 18", reader.readLine());

        cr.sortNumberByAscending(1, 2, 3, 4); // The numbers are: 1, 2, 3, 4
        assertEquals("The numbers are: 1, 2, 3, 4", reader.readLine());

    }
}