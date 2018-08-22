package com.funkdefino.gsysconvert.util;

import com.funkdefino.common.util.UtilException;
import com.funkdefino.common.util.xml.*;
import java.math.BigInteger;
import java.util.*;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Funkdefino (David M. Lang)
 * @version $Revision: $
 */
public final class Bank {

    //** ------------------------------------------------------------- Constants

    private final static String AttrbName   = "name";
    private final static String AttrbNumber = "number";
    private final static String AttrbVtrg   = "vtrg";
    private final static String AttrbStrg   = "strg";

    //** ------------------------------------------------------------------ Data

    private Map<Integer,Byte> presets  = new HashMap<Integer,Byte>();
    private Map<Trigger,Byte> triggers = new EnumMap<Trigger,Byte>(Trigger.class);

    private String name;  // Bank name
    private int  number;  // Bank number

    //** ---------------------------------------------------------- Construction

    /**
     * Ctor.
     * @param config a configuration element.
     * @throws UtilException on error.
     */
    public Bank(XmlElement config) throws UtilException{
        initialise(config);
    }

    //** ------------------------------------------------------------ Operations

    public String getName  ()  {return name;  }
    public int    getNumber()  {return number;}
    public int    presets  ()  {return presets.size();}
    public int    triggers ()  {return triggers.size();}

    public Map<Integer,Byte> getPresets() {return presets;}
    public byte              getTrigger(Trigger trg) {return triggers.get(trg);}

    /**
     * Returns a textual description.
     * @return the description.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(number).append(';');
        sb.append(name).append(";[");

        for(Map.Entry<Integer,Byte> entry : presets.entrySet())
           sb.append(String.format("%02x,%02x;", entry.getKey(), entry.getValue()));
        sb.append("][");

        sb.append(String.format("%05d", fmt(triggers.get(Trigger.Vtrg)))).append("][");
        sb.append(String.format("%05d", fmt(triggers.get(Trigger.Strg)))).append(']');

        return sb.toString();

    }   // toString()

    //** -------------------------------------------------------- Implementation

    /**
     * This performs startup initialisation.
     * @param config a configuration element.
     * @throws UtilException on error.
     */
    private void initialise(XmlElement config) throws UtilException {

        number = Integer.parseInt(XmlValidate.getAttribute(config, AttrbNumber));
        name = XmlValidate.getAttribute(config, AttrbName, "");

        Iterator<XmlElement> ii = config.getChildren().iterator();
        while(ii.hasNext()) {
            XmlElement preset = ii.next();
            int id = Integer.parseInt(XmlValidate.getAttribute(preset, AttrbNumber));
            byte c = Byte.parseByte(preset.getContent(), 2);
            presets.put(id, c);
        }

        String vt = XmlValidate.getAttribute(config, AttrbVtrg, "0");
        String st = XmlValidate.getAttribute(config, AttrbStrg, "0");

        triggers.put(Trigger.Vtrg, Byte.parseByte(vt, 2));
        triggers.put(Trigger.Strg, Byte.parseByte(st, 2));

    }   // initialise()

    /**
     * Binary trigger formatting for toString().
     * @param trg the trigger byte.
     * @return the formatted trigger.
     */
    private static BigInteger fmt(byte trg) {
        return new BigInteger(Integer.toBinaryString(trg));
    }

}   // class Bank
