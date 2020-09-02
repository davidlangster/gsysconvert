package com.funkdefino.gsysconvert.util.bpm;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class BpmUtil {

    //** ------------------------------------------------------------- Constants

    private final static int  PPQN   = 24;
    private final static long F_CPU  = 16000000;
    private final static long MAXOCR = 0xFFFF;
    private final static int  preScaler[] = {1,8,64,256,1024};   // 16-bit timer

    //** ------------------------------------------------------------ Operations

    public static double toPeriod(double bpm) {
        return 60000.0 / (PPQN * bpm);
    }

    public static double toBpm(double period) {
        return 60000.0 / (PPQN * period);
    }

    public static double bpmToOcr(double bpm) {

        double ocr = 0;
        double period = seconds(toPeriod(bpm));
        for(int i = 0; i < preScaler.length; i++) {
             ocr = period * F_CPU / preScaler[i];
             if(ocr <= MAXOCR) {
                System.out.println(String.format("[bpm:%03d] [ocr:%.4f] [prescaler:%d]", (int)bpm, (ocr - 1), preScaler[i]));
                break;
             }
        }

        return ocr;

    }   // bpmToOcr()

    public static double ocrToBpm(int ocr, int prescalar) {
        double period = (double)ocr * prescalar / F_CPU;
        return toBpm(millis(period));
    }

    //** -------------------------------------------------------- Implementation

    private static double seconds(double period) {
        return period / 1000;
    }

    private static double millis(double period) {
        return period * 1000;
    }

}   // class BpmUtil
