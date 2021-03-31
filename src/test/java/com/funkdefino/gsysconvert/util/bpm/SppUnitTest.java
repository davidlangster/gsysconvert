package com.funkdefino.gsysconvert.util.bpm;

import com.funkdefino.common.unittest.CTestCase;
import junit.framework.Test;

/**
 * <p>
 * <code>$Id: $</code>
 *
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class SppUnitTest extends CTestCase {

    //** ---------------------------------------------------------- Construction

    public SppUnitTest(String sMethod) {
         super(sMethod);
     }

    //** ------------------------------------------------------------ Operations

    /**
     * Returns the suite of cases for testing.
     * @return the test suite.
     */
    public static Test suite() {
        return CTestCase.suite(SppUnitTest.class,
            "UnitTest.xml",
            "test#3");
    }

    //** ----------------------------------------------------------------- Tests

    public void test01()
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

}   // class SppUnitTest
