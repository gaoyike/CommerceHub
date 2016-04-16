package main;

import java.util.Arrays;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/21/16.
 * This is a restrocking result. it extends the Result class.
 */
public class RestockingResult extends Result {
    public RestockingResult(Item i, int currentInventory, Status status) {
        super.setItem(i);
        super.setCurrentInventory(currentInventory);
        super.setStatus(status);
    }

    public int hashCode() {
        Object[] o = {super.getCurrentInventory(), super.getItem()};
        return Arrays.hashCode(o);
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof RestockingResult))
            return false;
        RestockingResult i = (RestockingResult) o;
        return this.getItem().equals(i.getItem()) && this.getCurrentInventory() == i.getCurrentInventory() && this.getStatus().equals(i.getStatus());
    }

}
