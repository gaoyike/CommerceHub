package test;

import main.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chenguang He (readman@iastate.edu) on 3/22/16.
 */
public class ItemTest {
    Item item;

    @Before
    public void setUp() throws Exception {
        item = new Item("tea", 10, "Ames");
    }

    @After
    public void tearDown() throws Exception {
        item = null;
        System.gc();//gc
    }

    @Test
    public void testGetProductId() throws Exception {
        assertEquals(item.getProductId(), "tea");
    }

    @Test
    public void testSetProductId() throws Exception {
        item.setProductId("food");
        assertEquals(item.getProductId(), "food");
    }

    @Test
    public void testGetLevels() throws Exception {
        assertEquals(item.getLevels(), 10);
    }

    @Test
    public void testGetLocation() throws Exception {
        assertEquals(item.getLocation(), "Ames");
    }

    @Test
    public void testSetLocation() throws Exception {
        item.setLocation("Albany");
        assertEquals(item.getLocation(), "Albany");
    }

    @Test
    public void testIncreaseLevel() throws Exception {
        item.increaseLevel(10);
        assertEquals(item.getLevels(), 20);
    }

    @Test
    public void testDecreaseLevel() throws Exception {
        item.decreaseLevel(5);
        assertEquals(item.getLevels(), 5);
    }

    @Test(expected = AssertionError.class)
    public void testDecreaseLevel1() throws Exception {
        item.decreaseLevel(-5);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() throws Exception {
        Item i = new Item("tea", 1, null);
    }

    @Test(expected = AssertionError.class)
    public void testAssertException() throws Exception {
        Item i = new Item(null, 1, "Ames");
    }

    @Test(expected = AssertionError.class)
    public void testAssertException1() throws Exception {
        Item i = new Item("tea", 0, "Ames");
    }

    @Test(expected = AssertionError.class)
    public void testAssertException2() throws Exception {
        Item i = new Item("tea", -1, "Ames");
    }
}