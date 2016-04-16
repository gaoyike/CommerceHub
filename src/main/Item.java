package main;

import com.sun.tools.javac.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Chenguang He (readman@iastate.edu)on 3/21/16.
 *
 * The item class, database implements the CREW in PRAM computational model, in other world, it have to make sure all threads safe.
 * Therefore, i use AtomicInteger to increase and decrease the integer, to ensure the rare condition not happen.
 */
public class Item {
    private String productId;
    private AtomicInteger levels;
    private String location;

    private Item() {
    }// do not allow default constructor

    /**
     * main.Item constructor
     *
     * @param productId productId
     * @param levels    level
     * @param location  the location
     */
    public Item(String productId, int levels, String location) {
        Assert.check(levels > 0,"Inventory should be larger than 0");
        Assert.checkNonNull(productId,"ProductId is unique and required");
        Objects.requireNonNull(location,"Location is unique and required");
        this.productId = productId;
        this.levels = new AtomicInteger(levels);
        this.location = location;
    }

    /**
     * get productId
     *
     * @return productId
     */
    public String getProductId() {
        return this.productId;
    }

    /**
     * set productId
     *
     * @param productId productId
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * get level
     *
     * @return level
     */
    public int getLevels() {
        return this.levels.get();
    }

    /**
     * get location
     *
     * @return location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * set location
     *
     * @param location location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * increase level
     *
     * @param level must bigger than 0
     * @return the level after increase
     */
    public int increaseLevel(int level) {
        Assert.check(level >= 0,"increase inventory must be larger than 0");
        return this.levels.addAndGet(level);
    }

    /**
     * decrease level
     *
     * @param level must bigger than 0
     * @return the level after decrease
     */
    public int decreaseLevel(int level) {
        Assert.check(level >= 0,"decrease inventory must be larger than 0");
        return this.levels.addAndGet(-level);
    }

    public int hashCode() {
        Object[] o = {levels, location, productId};
        return Arrays.hashCode(o);
    }

    public String toString() {
        return "Location:" + location + " level:" + levels.get() + " productId:" + productId;
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Item))
            return false;
        Item i = (Item) o;
        return this.productId.equals(i.productId) && this.levels.equals(i.levels) && this.location.equals(i.location);
    }
}
