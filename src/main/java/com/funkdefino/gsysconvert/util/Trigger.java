package com.funkdefino.gsysconvert.util;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Funkdefino (David M. Lang)
 * @version $Revision: $
 */

public enum Trigger {

    Vtrg("vtrg"),
    Strg("strg"),
    Ltrg("ltrg");

    Trigger(String id) {this.id = id;}
    private String id;

    /**
     * String to Trigger type conversion.
     * @param trg the trigger string.
     * @return the equivalent Trigger type.
     */
    public Trigger convert(String trg)  {

        for(Trigger t : Trigger.values()) {
            if(t.id.equals(trg)) {
                return t;
            }
        }

        throw new RuntimeException(String.format("Unknown trigger type '%s'", trg));
    }

}   // enum Trigger
