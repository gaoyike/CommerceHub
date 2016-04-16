package main;

import java.util.Arrays;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/21/16.
 */
public class PickingResult extends Result {

    public PickingResult(Item item, int currentInventory, Status status) {
        super.setItem(item);
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
        if (!(o instanceof PickingResult))
            return false;
        PickingResult i = (PickingResult) o;
        return this.getItem().equals(i.getItem()) && this.getCurrentInventory() == i.getCurrentInventory() && this.getStatus().equals(i.getStatus());
    }
}
