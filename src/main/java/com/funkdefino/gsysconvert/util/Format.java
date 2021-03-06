package com.funkdefino.gsysconvert.util;
import com.funkdefino.common.io.IOUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private final static int  USERBASE    = (PRESETS * 20);

    private final static byte SYSEX_CMND_STORE     = 0x00;
    private final static byte SYSEX_CMND_PRST      = 0x01;
    private final static byte SYSEX_CMND_PRSTNAMES = 0x02;
    private final static byte SYSEX_CMND_VTRG      = 0x03;
    private final static byte SYSEX_CMND_STRG      = 0x04;
    private final static byte SYSEX_CMND_SYSVAR    = 0x05;
    private final static byte SYSEX_CMND_USB       = 0x06;

    // +-----------------+------------------+---------------------+
    // | Footswitch Bank | MIDI Bank Select | MIDI Program Change |
    // +-----------------+------------------+---------------------+
    // | A0 thru B9      |        0         |        1-100        |
    // | 00 thru 19      |        1         |        1-100        |
    // | 20 thru 39      |        2         |        1-100        |
    // +-----------------+------------------+---------------------+

    // GSysSelect uses User Bank 2 (20 thru 39). Bank base calculation
    // as follows : (Bank * 5) - 100.
    // e.g. Bank 30 has base value = (30 * 5) - 100 = 50

    //** ------------------------------------------------------------ Operations

    /**
     * Formats the SYSEX message.
     * @param gsysconfig the configuration object.
     * @return the SYSEX byte array.
     */
    public static byte[] execute(GSysConfig gsysconfig) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream osnames = new ByteArrayOutputStream();
        byte[] sysex = null;

        try {
            byte[] sysexId = gsysconfig.getSysexId();

            baos.write(SYSEX_START);
            baos.write(sysexId,0,sysexId.length);

            if(gsysconfig.getBanks().size() > 0) {
                // Presets -----------------------------------------------------
                baos.write(SYSEX_SECT);
                baos.write(SYSEX_CMND_PRST);
                baos.write(encode(gsysconfig.presets()));

                for(Bank bank : gsysconfig.getBanks()) {
                    byte[] arr = formatBank(bank);
                    baos.write(arr,0, arr.length);
                }

                // Presets names -----------------------------------------------
                baos.write(SYSEX_SECT);
                baos.write(SYSEX_CMND_PRSTNAMES);

                for(Bank bank : gsysconfig.getBanks())
                    osnames.write(encodeBank(bank));
                byte[] names = osnames.toByteArray();

                baos.write(encode(names.length+1));
                baos.write(gsysconfig.banks());
                baos.write(names);

                // Vtrg --------------------------------------------------------
                baos.write(SYSEX_SECT);
                baos.write(SYSEX_CMND_VTRG);
                baos.write(encode(gsysconfig.banks()));

                for(Bank bank : gsysconfig.getBanks()) {
                    byte[] arr = formatTrigger(bank, Trigger.Vtrg);
                    baos.write(arr,0,arr.length);
                }

                // Strg --------------------------------------------------------
                baos.write(SYSEX_SECT);
                baos.write(SYSEX_CMND_STRG);
                baos.write(encode(gsysconfig.banks()));

                for(Bank bank : gsysconfig.getBanks()) {
                    byte[] arr = formatTrigger(bank, Trigger.Strg);
                    baos.write(arr,0,arr.length);
                }
            }

            // SysVar ----------------------------------------------------------
            baos.write(SYSEX_SECT);
            baos.write(SYSEX_CMND_SYSVAR);
            baos.write(encode(gsysconfig.getSysVar().size()));
            byte[] arr = gsysconfig.getSysVar().format();
            baos.write(arr,0,arr.length);

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
        return (byte)(base - USERBASE);

    }   // getBase()

    /**
     * Encodes a bank name as a byte array.
     * @param bank the bank.
     * @return the array.
     */
    private static byte[] encodeBank(Bank bank) {

        int i = 0;
        String name = bank.getName();
        byte[] arr = new byte[name.length()+2];
        arr[i++] = getBase(bank);
        arr[i++] = (byte)(name.length());
        for(byte b : name.getBytes()) {
            arr[i++] = b;
        }

        return arr;

    }   // encode()

    /**
     * Encodes a value as a 3-byte array.
     * @param val the value.
     * @return the array.
     */
    private static byte[] encode(int val) {

        int i = 0;
        byte[] arr = new byte[3];
        arr[i++] = (byte)((val >> 12) & 0x000F);
        arr[i++] = (byte)((val >> 6 ) & 0x003F);
        arr[i++] = (byte)(val & 0x003F);

        return arr;

    }   // encode()

}   // class Format
