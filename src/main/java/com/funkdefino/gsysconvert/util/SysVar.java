package com.funkdefino.gsysconvert.util;

import com.funkdefino.common.util.UtilException;
import com.funkdefino.common.util.xml.*;
import java.util.*;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class SysVar {

    //** ------------------------------------------------------------- Constants

    private final static String ElmntVolume  = "Volume";
    private final static String ElmntSwitch  = "Switch";
    private final static String AttrbWidth   = "pulseWidth";

    private final static byte ID_PULSE_WIDTH = 0x00;

    //** ------------------------------------------------------------------ Data

    private Map<Byte,Integer> sys = new HashMap<Byte,Integer>();

    //** ---------------------------------------------------------- Construction

    /**
     * Ctor.
     * @param config a configuration element.
     * @throws UtilException on error.
     */
    public SysVar(XmlElement config) throws UtilException {
        initialise(config);
    }

    //** ------------------------------------------------------------ Operations

    public int size() {return sys.size();}

    /**
     * Formats system variables as byte-encoded key / value pairs
     * @return a the formated array.
     */
    public byte[] format() {

        int i = 0;
        byte[] arr = new byte[sys.size() * 4];
        for(Map.Entry<Byte,Integer> entry : sys.entrySet()) {
            int val  = entry.getValue();
            arr[i++] = entry.getKey();
            arr[i++] = (byte)((val >> 14) & 0x0003);
            arr[i++] = (byte)((val >> 7 ) & 0x007F);
            arr[i++] = (byte)(val & 0x007F);
        }

        return arr;

    }   // format()

    /**
     * Returns a textual description.
     * @return the description.
     */
    @Override
    public String toString() {

        byte[] arr = format();
        StringBuilder sb = new StringBuilder();
        sb.append("SysVar;[");

        for(int i = 0; i < sys.size()*4; i+=4) {
            sb.append(String.format("%02x:%02x,%02x,%02x ,", arr[i], arr[i+1], arr[i+2], arr[i+3]));
        }

        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");

        return sb.toString();

    }   // toString()

    //** -------------------------------------------------------- Implementation

    /**
     * This performs startup initialisation.
     * @param config a configuration element.
     * @throws UtilException on error.
     */
    private void initialise(XmlElement config) throws UtilException {

        XmlElement volume = XmlValidate.getElement(config, ElmntVolume);
        XmlElement swtch  = XmlValidate.getElement(volume, ElmntSwitch);

        String width = XmlValidate.getAttribute(swtch, AttrbWidth);

        sys.put(ID_PULSE_WIDTH, Integer.parseInt(width));

    }   // initialise()

}   // class SysVar
