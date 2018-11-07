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
    private final static byte SYSEX_SECT  = (byte)0x7F;
    private final static byte SYSEX_END   = (byte)0xF7;
    private final static int  PRESETS     = 5;
    private final static int  BLOCK       = (PRESETS * 10);

    private final static byte SYSEX_CMND_USB   = 0x00;
    private final static byte SYSEX_CMND_PRST  = 0x01;
    private final static byte SYSEX_CMND_STORE = 0x02;
    private final static byte SYSEX_CMND_VTRG  = 0x03;
    private final static byte SYSEX_CMND_STRG  = 0x04;
    private final static byte SYSEX_CMND_LTRG  = 0x05;

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

            // Presets ---------------------------------------------------------
            baos.write(SYSEX_SECT);
            baos.write(SYSEX_CMND_PRST);
            baos.write(gsysconfig.presets());

            for(Bank bank : gsysconfig.getBanks()) {
                byte[] arr = formatBank(bank);
                baos.write(arr,0, arr.length);
            }

            // Vtrg ------------------------------------------------------------
            baos.write(SYSEX_SECT);
            baos.write(SYSEX_CMND_VTRG);
            baos.write(gsysconfig.banks());

            for(Bank bank : gsysconfig.getBanks()) {
                byte[] arr = formatTrigger(bank, Trigger.Vtrg);
                baos.write(arr,0,arr.length);
            }

            // Strg ------------------------------------------------------------
            baos.write(SYSEX_SECT);
            baos.write(SYSEX_CMND_STRG);
            baos.write(gsysconfig.banks());

            for(Bank bank : gsysconfig.getBanks()) {
                byte[] arr = formatTrigger(bank, Trigger.Strg);
                baos.write(arr,0,arr.length);
            }

            // Ltrg ------------------------------------------------------------
            baos.write(SYSEX_SECT);
            baos.write(SYSEX_CMND_LTRG);
            baos.write(gsysconfig.banks());

            for(Bank bank : gsysconfig.getBanks()) {
                byte[] arr = formatTrigger(bank, Trigger.Ltrg);
                baos.write(arr,0,arr.length);
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

        byte[] arr = new byte[bank.presets() * 2];
        int base = getBase(bank);

        int i = 0;
        for(Map.Entry<Integer,Byte> entry : bank.getPresets().entrySet()) {
            arr[i++] = (byte)(entry.getKey() + base - 1); // 1-based preset key (1 to 5)
            arr[i++] = entry.getValue();
        }

        return arr;

    }   // formatBank()

    /**
     * Returns an array representing a preset bank trigger, consisting of
     * a bank base value and trigger mask.
     * @param bank the bank.
     * @return the array.
     */
    private static byte[] formatTrigger(Bank bank, Trigger trg){
        return new byte[] {getBase(bank), bank.getTrigger(trg)};
    }

    /**
     * Returns the bank base value.
     * @param bank the bank.
     * @return the base.
     */
    private static byte getBase(Bank bank) {

        int base = bank.getNumber() * PRESETS;
        while(base - BLOCK >= BLOCK) {
            base -= BLOCK;
        }

        return (byte)base;

    }   // getBase()

}   // class Format
