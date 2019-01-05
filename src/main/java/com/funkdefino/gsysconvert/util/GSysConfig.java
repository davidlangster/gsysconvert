package com.funkdefino.gsysconvert.util;
import com.funkdefino.common.util.UtilException;
import com.funkdefino.common.util.xml.*;

import java.io.File;
import java.util.*;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Funkdefino (David M. Lang)
 * @version $Revision: $
 */
public final class GSysConfig {

    //** ------------------------------------------------------------- Constants

    private final static String ElmntPresets = "Presets";
    private final static String ElmntSysex   = "Sysex";
    private final static String ElmntSystem  = "System";
    private final static String AttrbDst     = "dst";

    //** ------------------------------------------------------------------ Data

    private List<Bank> banks = new ArrayList<Bank>(); // Preset banks
    private String sysexId;                           // SYSEX identifier
    private File dst;                                 // Destination file
    private SysVar sysVar;                            // System variables

    //** ---------------------------------------------------------- Construction

    /**
     * Ctor
     * @param config a configuration element.
     * @throws UtilException on error.
     */
    public GSysConfig(XmlElement config) throws UtilException {
        initialise(config);
    }

    //** ------------------------------------------------------------ Operations

    public File getDestination() {return    dst;}
    public List<Bank> getBanks() {return  banks;}
    public SysVar getSysVar()    {return sysVar;}

    /**
     * Returns the SYSEX identifier.
     * @return the identifier.
     */
    public byte[] getSysexId() {

        byte[] arr = new byte[sysexId.length() / 2];
        for(int i = 0, j = 0; i < arr.length; i++, j+=2) {
            String s = sysexId.substring(j, j+2);
            arr[i] = Byte.parseByte(s, 16);
        }

        return arr;

    }   // getSysexId()

    /**
     * Returns the total number of configured banks.
     * @return the total.
     */
    public int banks() {
        return banks.size();
    }

    /**
     * Returns the total number of configured bank presets.
     * @return the total.
     */
    public int presets() {

        int size = 0;
        for(Bank bank : banks) {
            size += bank.presets();
        }

        return size;

    }   // presets()

    //** --------------------------------------------------------- Implemenation

    /**
     * This performs startup initialisation.
     * @param config a configuration element.
     * @throws UtilException on error.
     */
    private void initialise(XmlElement config) throws UtilException {

        XmlElement presets = XmlValidate.getElement(config, ElmntPresets);
        Iterator<XmlElement> ii = presets.getChildren().iterator();
        while(ii.hasNext()) {
            banks.add(new Bank(ii.next()));
        }

        XmlElement system = XmlValidate.getElement(config, ElmntSystem);
        sysVar = new SysVar(system);

        sysexId = XmlValidate.getContent(config, ElmntSysex);
        dst = new File(XmlValidate.getAttribute(config, AttrbDst));

    }   // initialise()

}   // class GSysConfig
