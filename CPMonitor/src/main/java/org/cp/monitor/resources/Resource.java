package org.cp.monitor.resources;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class Resource {

    protected int number;

    protected boolean isLocked;

    public Resource(final Integer number){
        this.number=number;
    }

    public String toString(){
        return String.format("%s number %d", this.getClass().getSimpleName(),number);
    }

    public int getNumber() {
        return number;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
