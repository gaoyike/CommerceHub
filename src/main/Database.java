package main;


import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/21/16.
 * This is a database class. it is a thread class that implements InventoryManagementSystem
 * It implements concurrency to ensure the real-time picking and restocking.
 * It requires the initial inventory when it starts.
 */

public class Database implements InventoryManagementSystem, Runnable {
    // use ConcurrentHashMap to ensure  synsynchronization
    private ConcurrentHashMap<String, Item> items;// product id -> item

    /**
     * the database constructor
     *
     * @param items the input item.
     */
    public Database(ConcurrentHashMap<String, Item> items) throws NullPointerException {
        Objects.requireNonNull(items); // initial inventory can not be null
        this.items = items;
    }

    /**
     * the synchronized picking method.
     *
     * @param productId    The ID of the product to pick
     * @param amountToPick The quantity of the product to pick
     * @return the picking result
     */
    @Override
    public synchronized PickingResult pickProduct(String productId, int amountToPick) throws NullPointerException, InvalidParameterException{
        Objects.requireNonNull(items);//productId can not be null
        if (amountToPick < 0) {
            throw new InvalidParameterException();
        }//amountToRestock must be large than 0;
        Item i = items.get(productId);
        if (i == null)
            return new PickingResult(null,0,Status.NoSuchItem);
        if (amountToPick > i.getLevels() || i.getLevels()==0) {
            return new PickingResult(i,i.getLevels(),Status.InsufficientInventory);
        }
        int currentInventory = i.decreaseLevel(amountToPick);
        return new PickingResult(i, currentInventory, Status.Success);
    }

    /**
     * the synchronized restocing method
     *
     * @param productId       The ID of the product to restock
     * @param amountToRestock The quantity of the product to restock
     * @return the restocking result
     */
    @Override
    public synchronized RestockingResult restockProduct(String productId, int amountToRestock) throws NullPointerException, InvalidParameterException{
        Objects.requireNonNull(items);//productId can not be null
        if (amountToRestock < 0) {
            throw new InvalidParameterException();
        }//amountToRestock must be large than 0;
        Item i = items.get(productId);
        if (i == null)
            return new RestockingResult(null,0,Status.NoSuchItem);
        int currentInventory = i.increaseLevel(amountToRestock);
        return new RestockingResult(i, currentInventory,Status.Success);
    }

    @Override
    public void run() {
        System.out.println("Starting database.. Thread Id:" + Thread.currentThread().getId());
    }
}
