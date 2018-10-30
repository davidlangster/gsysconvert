package com.funkdefino.gsysconvert.util.convert;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class LTrgConvert extends BaseConvert {

    /**
     * Trigger offset conversion - 5-bit wide bitmask to byte, with bit 5 as LSB
     * Bits are not mutually exclusive.
     * @param bitmask the string bitmask (XXXXX).
     * @return the byte offset, 0x00 - 0x0F.
     */
    public static byte convert(String bitmask) {

        byte conv = 0;
        byte mask = Byte.parseByte(bitmask, 2);
        for(int i = 0; i < 5; i++) {
            if(bitSet(mask, i)) {
                conv += (5-i);
            }
        }

        return conv;

    }   // convert()

}   // class LTrgConvert
