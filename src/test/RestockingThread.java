package test;

import main.Database;
import main.RestockingResult;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/22/16.
 * This is a test thread to test the restockresult.
 * It input number of restocking and productid
 * it restocke ONE item per time in order to test the concurrency
 * For each time restocking, it waits a random secs
 */

public class RestockingThread  implements Runnable {
    private Database db;
    private int num;
    private String pid;

    /**
     * the restocking construtor
     * @param db database thread
     * @param num the number of item to restock
     * @param pid the product id
     */
    public RestockingThread(Database db,int num, String pid){
        this.db = db;
        this.num = num;
        this.pid = pid;
    }

    /**
     * the restock thread.
     * It restocks item one at a time.
     * It waits a random sec.
     * @throws InterruptedException the thread is not safe
     */
    public void restock() throws InterruptedException {
        RestockingResult restockingResult=null;
        for (int i = 0; i < num; i++) {
            restockingResult=db.restockProduct(pid,1);
            System.out.println("restock "+pid+" Current Inventory: "+restockingResult.getCurrentInventory());
            Thread.sleep((int)(Math.random()*1000));
        }
    }
    @Override
    public void run() {
        try {
            restock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
