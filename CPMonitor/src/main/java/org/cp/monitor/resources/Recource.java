package org.cp.monitor.resources;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class Recource {

    protected int number;

    public Recource(final int number){
        this.number=number;
    }

    public String toString(){
        return String.format("I'm %s number %d", this.getClass().getName(),number);
    }
}
