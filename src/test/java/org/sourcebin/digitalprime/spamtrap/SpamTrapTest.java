package org.sourcebin.digitalprime.spamtrap;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamTrapTest {

    public final static Logger console = Logger.getLogger("SpamTrapTest");
    public SpamTrap instance;

    public SpamTrapTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new SpamTrap();
    }

    @After
    public void tearDown() {
        instance.reset();
    }

    /**
     * Test of removeRepeating method, of class dSpam.
     */
    @Test
    public void testRemoveRepeating() {
        console.log(Level.INFO, "removeRepeating");

        String result = instance.removeRepeating("*   ");
        assertEquals("*  ", result);

        result = instance.removeRepeating("***");
        assertEquals("**", result);

        result = instance.removeRepeating("**   ");
        assertEquals("**  ", result);

        result = instance.removeRepeating(" ***   ");
        assertEquals(" **  ", result);

        result = instance.removeRepeating("tpaccccept");
        assertEquals("tpaccept", result);

        result = instance.removeRepeating("jaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!!!!!!!!");
        assertEquals("jaa!!", result);
    }

    @Test
    public void testRemoveRepeatingWithDigits() {
        console.log(Level.INFO, "removeRepeating");

        String result = instance.removeRepeating("123444455");
        assertEquals("123444455", result);

        result = instance.removeRepeating("***555!!!");
        assertEquals("**555!!", result);
    }

    /**
     * Test of removeWhitespace method, of class dSpam.
     */
    @Test
    public void testRemoveWhitespace() {
        console.log(Level.INFO, "removeWhitespace");

        String result = instance.removeWhitespace("a \t  \nb");
        assertEquals("a b", result);

        result = instance.removeWhitespace("a \t  \nb c ");
        assertEquals("a b c", result);
    }

    @Test
    public void testDuplicateTimedMessage() {
        console.log(Level.INFO, "testDuplicateMessage");
        SpamResult result = instance.isSpam("test test test");
        assertFalse(result.isSpam());
        result = instance.isSpam("test test test");
        assertTrue(result.isSpam());
        assertTrue(result.getType() == SpamResult.resultEnum.MESSAGE_TO_QUICK);
    }

    @Test
    public void testDuplicateXMessage() {
        console.log(Level.INFO, "testDuplicateMessage");
        instance.setTimeCheckEnabled(false);
        instance.setThreshold(1);
        SpamResult result = instance.isSpam("test test test");
        assertFalse(result.isSpam());
        result = instance.isSpam("test test test");
        assertFalse(result.isSpam());
        result = instance.isSpam("test test test");
        assertTrue(result.isSpam());
        assertTrue(result.getType() == SpamResult.resultEnum.LAST_DUPLICATE);
    }

    @Test
    public void testDuplicateLastMessage() {
        console.log(Level.INFO, "testDuplicateMessage");
        instance.setTimeCheckEnabled(false);
        instance.setThreshold(0);
        SpamResult result = instance.isSpam("test test test");
        assertFalse(result.isSpam());
        result = instance.isSpam("test test test");
        assertTrue(result.isSpam());
        assertTrue(result.getType() == SpamResult.resultEnum.LAST_DUPLICATE);
    }

}
