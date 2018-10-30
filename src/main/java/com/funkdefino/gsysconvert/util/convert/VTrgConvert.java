package com.funkdefino.gsysconvert.util.convert;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class VTrgConvert extends BaseConvert {

    /**
     * Trigger offset conversion - 5-bit wide bitmask to byte, with bit 5 as LSB
     * Bits are mutually exclusive.
     * @param bitmask the string bitmask (XXXXX).
     * @return the byte offset, 0x00 - 0x05.
     */
    public static byte convert(String bitmask) {
        return convert(Byte.parseByte(bitmask, 2));
    }

}   // class VTrgConvert
