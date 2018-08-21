package com.funkdefino.gsysconvert.util;
import com.funkdefino.common.io.IOUtil;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Funkdefino (David M. Lang)
 * @version $Revision: $
 */
public final class Format {

    //** ------------------------------------------------------------- Constants

    private final static byte SYSEX_START = (byte)0xF0;
    private final static byte SYSEX_SECT  = (byte)0xF6;
    private final static byte SYSEX_END   = (byte)0xF7;
    private final static int  PRESETS     = 5;
    private final static int  BLOCK       = (PRESETS * 10);

    //** ------------------------------------------------------------ Operations

    /**
     * Formats the SYSEX message.
     * @param gsysconfig the configuration object.
     * @return the SYSEX byte array.
     */
    public static byte[] execute(GSysConfig gsysconfig) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] sysex = null;

        try {
           byte[] sysexId = gsysconfig.getSysexId();

            baos.write(SYSEX_START);
            baos.write(sysexId,0,sysexId.length);
            baos.write(gsysconfig.presets());

            // Presets
            for(Bank bank : gsysconfig.getBanks()) {
                byte[] arr = formatBank(bank);
                baos.write(arr,0, arr.length);
            }

            baos.write(SYSEX_SECT);
            baos.write(gsysconfig.getBanks().size());

            // Vtrg
            for(Bank bank : gsysconfig.getBanks()) {
                byte[] arr = formatVtrg(bank);
                baos.write(arr,0, arr.length);
            }

            baos.write(SYSEX_END);
            sysex = baos.toByteArray();

        }
        finally {
            IOUtil.close(baos);
        }

        return sysex;

    }   // execute()

    //** -------------------------------------------------------- Implementation

    /**
     * Returns an array representing one or more bank preset(s), where each
     * preset is represented by an identifier plus configuration mask.
     * @param bank the bank.
     * @return the array.
     */
    private static byte[] formatBank(Bank bank) {

        byte[] arr = new byte[bank.size() * 2];

        int base = bank.getNumber() * PRESETS;
        while(base - BLOCK >= BLOCK) {
            base -= BLOCK;
        }

        int i = 0;
        for(Map.Entry<Integer,Byte> entry : bank.getPresets().entrySet()) {
            arr[i++] = (byte)(entry.getKey() + base - 1);
            arr[i++] = entry.getValue();
        }

        return arr;

    }   // formatBank()

    /**
     * Returns an array representing a preset bank vtrg, consisting of
     * a base preset identifier (first in the bank) and vtrg mask.
     * @param bank the bank.
     * @return the array.
     */
    private static byte[] formatVtrg(Bank bank) {

        byte[] arr = new byte[2];

        int base = bank.getNumber() * PRESETS;
        while(base - BLOCK >= BLOCK) {
            base -= BLOCK;
        }

        arr[0] = (byte)(base - 1);
        arr[1] = bank.getVtrg();

        return arr;

    }   // formatVtrg()

}   // class Convert
