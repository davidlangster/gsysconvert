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

        sb.append(String.format("%02x", triggers.get(Trigger.Vtrg))).append("][");
        sb.append(String.format("%02x", triggers.get(Trigger.Strg))).append(']');

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

        triggers.put(Trigger.Vtrg, convert(vt));
        triggers.put(Trigger.Strg, convert(st));

    }   // initialise()

    /**
     * Trigger offset conversion - 5-bit wide bitmask to byte, with bit 5 as LSB
     * @param vt the string bitmask (XXXXX).
     * @return the byte offset, 0 - 5.
     */
    private static byte convert(String vt) {

        byte vtrg = Byte.parseByte(vt, 2);

        if(bitSet(vtrg,0)) return 5;
        if(bitSet(vtrg,1)) return 4;
        if(bitSet(vtrg,2)) return 3;
        if(bitSet(vtrg,3)) return 2;
        if(bitSet(vtrg,4)) return 1;

        return 0;

    }   // convert()

    /**
     * Bit test.
     * @param b the byte
     * @param n the bit (0 - 7).
     * @return true if set; otherwise false.
     */
    private static boolean bitSet(byte b, int n) {
        return (((b >> n) & 0x01) == 0x01);
    }

}   // class Bank
