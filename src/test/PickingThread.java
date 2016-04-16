package test;

import main.Database;
import main.PickingResult;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/22/16.
 * This is a test thread to test the pickingresult.
 * It input number of picking and productid
 * it picks ONE item per time in order to test the concurrency
 * For each time picking, it waits a random secs
 */

public class PickingThread implements Runnable {
    private Database db;
    private int num;
    private String pid;

    /**
     * the constructor
     * @param db database thread
     * @param num number of item to pick
     * @param pid product id
     */
    public PickingThread(Database db, int num, String pid) {
        this.db = db;
        this.num = num;
        this.pid = pid;
    }

    /**
     * pick item , ONE item for each time of picking
     * @throws InterruptedException if the threa is not safe anymore
     */
    public void pick() throws InterruptedException {
        PickingResult pickingResult = null;
        for (int i = 0; i < num; i++) {
            pickingResult = db.pickProduct(pid, 1);
            System.out.println("picks " + pid + " Current Inventory: " + pickingResult.getCurrentInventory());
            Thread.sleep((int) (Math.random() * 1000));//random wait time
        }

    }

    @Override
    public void run() {
        try {
            pick();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
