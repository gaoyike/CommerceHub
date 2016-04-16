package test;

import main.*;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/22/16.
 */
public class DatabaseTest {
    Database database;
    Thread dbThread;

    @org.junit.Before
    public void setUp() throws Exception {
        Item tea = new Item("tea", 100, "location1");
        Item book = new Item("book", 100, "location2");
        Item cafe = new Item("cafe", 100, "location3");
        ConcurrentHashMap<String, Item> db = new ConcurrentHashMap<String, Item>();
        db.put("tea", tea);
        db.put("book", book);
        db.put("cafe", cafe);
        database = new Database(db);
        dbThread = new Thread(database);
        dbThread.start();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        dbThread.stop();
        database = null;
        System.gc();//gc
    }

    /**
     * test successful picking
     *
     * @throws Exception no exception
     */
    @org.junit.Test
    public void pickProduct1() throws Exception {
        PickingResult result = database.pickProduct("tea", 5);
        assertTrue(result.getCurrentInventory() == 95 && result.getItem().getProductId().equals("tea"));
        assertEquals(result.getStatus(), Status.Success);
        assertEquals(result.getItem().getProductId(), "tea");

        PickingResult result2 = database.pickProduct("tea", 5);
        assertTrue(result2.getCurrentInventory() == 90 && result2.getItem().getProductId().equals("tea"));
        assertEquals(result2.getStatus(), Status.Success);
        assertEquals(result2.getItem().getProductId(), "tea");

        PickingResult result3 = database.pickProduct("book", 5);
        assertTrue(result3.getCurrentInventory() == 95 && result3.getItem().getProductId().equals("book"));
        assertEquals(result3.getStatus(), Status.Success);
        assertEquals(result3.getItem().getProductId(), "book");
    }

    /**
     * test pick null, null point exception
     *
     * @throws Exception NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void pickProduct2() throws Exception {
        database.pickProduct(null, 5);
    }

    /**
     * test get unknown item
     *
     * @throws Exception no exception
     */
    @org.junit.Test
    public void pickProduct3() throws Exception {
        PickingResult pickingResult = database.pickProduct("food", 5);
        assertEquals(pickingResult.getStatus(), Status.NoSuchItem);
    }

    /**
     * test insufficient inventory
     *
     * @throws Exception no exception
     */
    @org.junit.Test
    public void pickProduct4() throws Exception {
        PickingResult pickingResult = database.pickProduct("tea", 500);
        assertEquals(pickingResult.getStatus(), Status.InsufficientInventory);
        assertEquals(pickingResult.getCurrentInventory(), 100);
    }

    /**
     * test successful restocking
     *
     * @throws Exception no exception expected
     */

    @org.junit.Test
    public void restockProduct1() throws Exception {
        RestockingResult restockingResult = database.restockProduct("tea", 100);
        assertEquals(restockingResult.getItem().getProductId(), "tea");
        assertEquals(restockingResult.getCurrentInventory(), 200);
        assertEquals(restockingResult.getStatus(), Status.Success);

        RestockingResult restockingResult2 = database.restockProduct("book", 1);
        assertEquals(restockingResult2.getItem().getProductId(), "book");
        assertEquals(restockingResult2.getCurrentInventory(), 101);
        assertEquals(restockingResult2.getStatus(), Status.Success);

        RestockingResult restockingResult3 = database.restockProduct("tea", 50);
        assertEquals(restockingResult3.getItem().getProductId(), "tea");
        assertEquals(restockingResult3.getCurrentInventory(), 250);
        assertEquals(restockingResult3.getStatus(), Status.Success);
    }

    /**
     * test restocking 'null'
     * @throws Exception expected NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void restockProduct2() throws Exception {
        RestockingResult restockingResult = database.restockProduct(null, 1);
    }

    /**
     * test restocking unknown item
     * @throws Exception no expected exception
     */
    @org.junit.Test
    public void restockProduct3() throws Exception {
        RestockingResult restockingResult = database.restockProduct("food", 5);
        assertEquals(restockingResult.getStatus(), Status.NoSuchItem);
    }

    /**
     * test concurrent picking and restocking.
     * the main thread waits two sub threads to finish.
     * the sub thread pick and restock same equity of same item.
     * @throws Exception
     */
    @org.junit.Test
    public void testConcurrency() throws Exception {
        PickingThread pt1 = new PickingThread(database, 5, "tea");
        RestockingThread pt2 = new RestockingThread(database, 5, "tea");
        Thread t1 = new Thread(pt1);
        Thread t2 = new Thread(pt2);
        t1.start();
        t2.start();
        //join will join two threads to wait sub threads
        t1.join();
        t2.join();
        PickingResult pickingResult = database.pickProduct("tea",0);
        assertEquals(pickingResult.getCurrentInventory(),100);
    }

    /**
     * this method test concurrent two thread picking sufficent inventory
     * @throws Exception
     */
    @org.junit.Test
    public void testConcurrency1() throws Exception {
        PickingThread pt1 = new PickingThread(database, 5, "tea");
        PickingThread pt2 = new PickingThread(database, 5, "tea");
        Thread t1 = new Thread(pt1);
        Thread t2 = new Thread(pt2);
        t1.start();
        t2.start();
        //join will join two threads to wait sub threads
        t1.join();
        t2.join();
        PickingResult pickingResult = database.pickProduct("tea",0);
        assertEquals(pickingResult.getCurrentInventory(),90);
    }

    /**
     * this method test concurrent two thread picking insufficent inventory
     * @throws Exception
     */
    @org.junit.Test
    public void testConcurrency2() throws Exception {
        database.pickProduct("tea",90);//reduce size of tea to make testing faster
        PickingThread pt1 = new PickingThread(database, 5, "tea");
        PickingThread pt2 = new PickingThread(database, 6, "tea");
        Thread t1 = new Thread(pt1);
        Thread t2 = new Thread(pt2);
        t1.start();
        t2.start();
        //join will join two threads to wait sub threads
        t1.join();
        t2.join();
        PickingResult pickingResult = database.pickProduct("tea",0);
        assertEquals(pickingResult.getCurrentInventory(),0);
        assertEquals(pickingResult.getStatus(),Status.InsufficientInventory);
    }

    /**
     * this method test concurrent two thread picking insufficent inventory then one thread restock inventory.
     * NOTE: THIS WILL LEAD TO UNKNOWN RESULT
     * @throws Exception no expect exception
     */
    @org.junit.Test
    public void testConcurrency3() throws Exception {
        database.pickProduct("tea",95);//reduce size of tea to make testing faster
        PickingThread pt1 = new PickingThread(database, 5, "tea");
        PickingThread pt2 = new PickingThread(database, 6, "tea");
        RestockingThread pt3 = new RestockingThread(database,7,"tea");
        Thread t1 = new Thread(pt1);
        Thread t2 = new Thread(pt2);
        Thread t3 = new Thread(pt3);
        t1.start();
        t2.start();
        t3.start();
        //join will join two threads to wait sub threads
        t1.join();
        t2.join();
        t3.join();
    }
}