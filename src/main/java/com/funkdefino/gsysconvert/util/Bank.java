package com.funkdefino.gsysconvert.util;
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

    //** ------------------------------------------------------------------ Data

    private Map<Integer,Byte> presets = new HashMap<Integer, Byte>();
    private String name;
    private int number;

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

    public int getNumber () {return number;}
    public String getName() {return name;  }
    public Map<Integer,Byte> getPresets() {return presets;}
    public int size() {return presets.size();}

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
            sb.append(String.format("%02d,%02d;", entry.getKey(),entry.getValue()));
        sb.append(']');

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

    }   // initialise()

}   // class Bank
