package com.funkdefino.gsysconvert.util.convert;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class STrgConvert extends BaseConvert {

    //** ------------------------------------------------------------- Constants

    private final static String InvalidFormat = "Invalid bitmask format : %s";
    private final static int MASK_LEN = 11;

    //** ------------------------------------------------------------ Operations

    /**
     * Trigger offset conversion - TWO 5-bit wide bitmasks to byte, with bit 5
     * as LSB. Bits are mutually exclusive per nibble.
     * @param bitmask the string bitmask (XXXXX.XXXXX).
     * @return the byte offsets, 0x00 - 0x05, in upper and lower nibbles.
     */
    public static byte convert(String bitmask) {

        int idx = 0;

        if(bitmask == null || bitmask.length() != MASK_LEN || (idx = bitmask.indexOf('.')) == -1) {
            throw new RuntimeException(String.format(InvalidFormat, bitmask));
        }

        String upper = bitmask.substring(0, idx);
        String lower = bitmask.substring(idx+1, bitmask.length());

        byte upper_ = convert(Byte.parseByte(upper, 2));
        byte lower_ = convert(Byte.parseByte(lower, 2));

        return (byte)(upper_ << 0x04 | lower_);

    }   // convert()

}   // class STrgConvert
