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

    private final static String ElmntVolume      = "Volume";
    private final static String ElmntSwitch      = "Switch";
    private final static String ElmntDisplay     = "Display";
    private final static String AttrbTriggerEdge = "edge";
    private final static String AttrbPulseWidth  = "pulseWidth";
    private final static String AttrbRotation    = "rotation";
    private final static String AttrbBrightness  = "brightness";

    private final static String InvalidEdge      = "Invalid edge '%s'";

    private final static byte ID_SW_TRIGGER_EDGE = 0x00;
    private final static byte ID_SW_PULSE_WIDTH  = 0x01;
    private final static byte ID_DSP_ROTATION    = 0x02;
    private final static byte ID_DSP_BRIGHTNESS  = 0x03;

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
            sb.append(String.format("%02x:%02x,%02x,%02x;", arr[i], arr[i+1], arr[i+2], arr[i+3]));
        }

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

        XmlElement volume  = XmlValidate.getElement(config, ElmntVolume );
        XmlElement display = XmlValidate.getElement(config, ElmntDisplay);
        XmlElement swtch   = XmlValidate.getElement(volume, ElmntSwitch );

        String triggerEdge = XmlValidate.getAttribute(swtch,   AttrbTriggerEdge);
        String pulseWidth  = XmlValidate.getAttribute(swtch,   AttrbPulseWidth );
        String rotation    = XmlValidate.getAttribute(display, AttrbRotation   );
        String brightness  = XmlValidate.getAttribute(display, AttrbBrightness );

        int edge;
        if(triggerEdge.equalsIgnoreCase("falling")) edge = 2;
        else if(triggerEdge.equalsIgnoreCase("rising")) edge = 3;
        else {
            throw new UtilException(String.format(InvalidEdge, triggerEdge));
        }

        sys.put(ID_SW_TRIGGER_EDGE,edge);
        sys.put(ID_SW_PULSE_WIDTH, Integer.parseInt(pulseWidth));
        sys.put(ID_DSP_ROTATION,   Integer.parseInt(rotation)  );
        sys.put(ID_DSP_BRIGHTNESS, Integer.parseInt(brightness));

    }   // initialise()

}   // class SysVar
