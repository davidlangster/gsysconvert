package com.funkdefino.gsysconvert.util;

import com.funkdefino.gsysconvert.util.convert.*;
import com.funkdefino.common.util.UtilException;
import com.funkdefino.common.util.xml.*;

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
    private final static String AttrbLtrg   = "ltrg";

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
           sb.append(String.format("%02x:%02x,", entry.getKey(), entry.getValue()));
        sb.deleteCharAt(sb.length()-1);
        sb.append("][");

        sb.append(String.format("V:%02x", triggers.get(Trigger.Vtrg))).append("][");
        sb.append(String.format("S:%02x", triggers.get(Trigger.Strg))).append("][");
        sb.append(String.format("L:%02x", triggers.get(Trigger.Ltrg))).append(']');

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
            byte b = validate(Byte.parseByte(preset.getContent(), 2));
            presets.put(id, b);
        }

        String vt = XmlValidate.getAttribute(config, AttrbVtrg, "00000");
        String st = XmlValidate.getAttribute(config, AttrbStrg, "00000.00000");
        String lt = XmlValidate.getAttribute(config, AttrbLtrg, "00000");

        triggers.put(Trigger.Vtrg, VTrgConvert.convert(vt));
        triggers.put(Trigger.Strg, STrgConvert.convert(st));
        triggers.put(Trigger.Ltrg, LTrgConvert.convert(lt));

    }   // initialise()

    /**
     * Byte validation. Filters 'return without send' - e.g. nnnn.nn10B
     * @param b the byte.
     * @return the byte.
     * @throws UtilException on error.
     */
    private static byte validate(byte b) throws UtilException {

        if(bitSet(b,1) && !(bitSet(b,0))) {
            throw new UtilException(String.format("Invalid preset value : 0x%02x", b));
        }

        return b;

    }   // validate()

    /**
     * Bit-set check.
     * @param b the byte
     * @param n the bit.
     * @return true if set; otherwise false.
     */
    private static boolean bitSet(byte b, int n) {
        return ((b >> n) & 0x01) == 0x01;
    }

}   // class Bank
