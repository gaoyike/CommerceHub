package main;

/**
 * Created by Chenguang He (readman@iastate.edu) on 3/22/16.
 * This is a status class used in Result class to shows status of result.
 */
public enum Status {
    Success("Action Success!"),
    InsufficientInventory("Insufficient inventory"),
    NoSuchItem("Item does not exists!");
    private String msg;
    Status(String msg){
        this.msg=msg;
    }
}
