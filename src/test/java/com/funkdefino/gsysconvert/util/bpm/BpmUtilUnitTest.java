package com.funkdefino.gsysconvert.util.bpm;
import com.funkdefino.common.unittest.CTestCase;
import junit.framework.Test;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class BpmUtilUnitTest extends CTestCase {
    
    //** ---------------------------------------------------------- Construction

    public BpmUtilUnitTest(String sMethod) {
        super(sMethod);
    }

    //** ------------------------------------------------------------ Operations

    /**
     * Returns the suite of cases for testing.
     * @return the test suite.
     */
    public static Test suite() {
        return CTestCase.suite(BpmUtilUnitTest.class,
            "UnitTest.xml",
            "test#2");
    }

    //** ----------------------------------------------------------------- Tests

    public void test01() {
        System.out.printf("[bpm:030] [period:%.4f ms]\n", BpmUtil.toPeriod(30 ));
        System.out.printf("[bpm:120] [period:%.4f ms]\n", BpmUtil.toPeriod(120));
        System.out.printf("[bpm:250] [period:%.4f ms]\n", BpmUtil.toPeriod(250));
    }

    public void test02() {
        BpmUtil.bpmToOcr(30.0 );
        BpmUtil.bpmToOcr(120.0);
        BpmUtil.bpmToOcr(250.0);
    }

    public void test03() {
        System.out.printf("%.4f\n", BpmUtil.ocrToBpm(20832, 64)); // 30BPM
        System.out.printf("%.4f\n", BpmUtil.ocrToBpm(20824, 64)); // 30BPM with offset

        System.out.printf("%.4f\n", BpmUtil.ocrToBpm(41666, 8 )); // 120BPM
        System.out.printf("%.4f\n", BpmUtil.ocrToBpm(41658, 8 )); // 120BPM with offset

        System.out.printf("%.4f\n", BpmUtil.ocrToBpm(19999, 8 )); // 250BPM
        System.out.printf("%.4f\n", BpmUtil.ocrToBpm(19991, 8 )); // 250BPM with offset

    }

    public void test04()
    {
        byte lo = 0x55;
        byte hi = 0x03;
        int spp = lo | (hi << 0x07); // Sixteenth notes
        double bar = spp / 16.0;
        System.out.printf("SPP : %d, bar = %f\n", spp, bar);

        lo = (byte)(spp & 0x7F);
        hi = (byte)((spp - lo) >> 0x07);
        System.out.printf("lo : %02x, hi = %02x", lo, hi);

    }

}   // class BpmUtil
