
package org.sourcebin.digitalprime.spamtrap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author d4z
 */
public class DuplicateMessageTest {

    public DuplicateMessageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isDuplicate method, of class DuplicateMessage.
     */
    @Test
    public void testIsDuplicate() {
        System.out.println("isDuplicate");
        DuplicateMessage instance = new DuplicateMessage();

        boolean result = instance.isDuplicate("test");
        assertEquals(false, result);
        result = instance.isDuplicate("test");
        assertEquals(true, result);
        result = instance.isDuplicate("test!!!!");
        assertEquals(true, result);
        result = instance.isDuplicate("another test!!!!");
        assertEquals(false, result);
        result = instance.isDuplicate("another  test!!?!!");
        assertEquals(true, result);
    }
}
