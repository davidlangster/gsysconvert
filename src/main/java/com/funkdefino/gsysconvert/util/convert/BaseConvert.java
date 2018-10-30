package com.funkdefino.gsysconvert.util.convert;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public abstract class BaseConvert {

    //** ------------------------------------------------------------ Operations

    /**
     * 5-bit wide bitmask conversion, each bit being mutually exclusive,
     * with bit 5 as LSB.
     *
     *      00000 = 0
     *      00001 = 5
     *      00010 = 4
     *      00100 = 3
     *      01000 = 2
     *      10000 = 1
     *
     * @param mask the bitmask.
     * @return the byte conversion as described.
     */
    protected static byte convert(byte mask) {

        if(bitSet(mask,0)) return 5;
        if(bitSet(mask,1)) return 4;
        if(bitSet(mask,2)) return 3;
        if(bitSet(mask,3)) return 2;
        if(bitSet(mask,4)) return 1;

        return 0;

    }  // convert()

    /**
     * Bit test.
     * @param b the byte
     * @param n the bit (0 - 7).
     * @return true if set; otherwise false.
     */
    protected static boolean bitSet(byte b, int n) {
        return (((b >> n) & 0x01) == 0x01);
    }

}   // class BaseConvert
