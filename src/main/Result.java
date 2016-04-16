package main;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/21/16.
 * The abstract class of Result
 * It uses to abstract pickingresult and restockingresult.
 *
 */

public abstract class Result {
    private Item i; // the return item
    private int currentInventory;// This is a updated inventory, just use to ensure the client do not make endless useless requests.  therefore it should not be synced
    private Status status;
    /**
     * get item
     *
     * @return the item
     */
    public Item getItem(){
        return i;
    }

    /**
     * return Status
     * @return return Status
     */
    public Status getStatus(){
        return status;
    }

    /**
     * set Status
     * @param status  set Status
     */
    public void setStatus(Status status){
        this.status = status;
    }

    /**
     * return currentInventory
     * @return currentInventory
     */
    public int getCurrentInventory() {
        return currentInventory;
    }

    /**
     * set currentInventory
     * @param currentInventory currentInventory
     */
    public void setCurrentInventory(int currentInventory) {
        this.currentInventory = currentInventory;
    }

    /**
     * set item
     * @param i item
     */
    public void setItem(Item i){
        this.i = i;
    }

    /**
     * return true if the item is null
     * @return true if item is null.
     */
    public boolean isNull(){
        return i==null;
    }
    public String toString() {
        return "Item:" + i + " current inventory:" + currentInventory;
    }
}
