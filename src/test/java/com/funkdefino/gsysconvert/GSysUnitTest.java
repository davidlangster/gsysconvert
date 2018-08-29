package com.funkdefino.gsysconvert;

import com.funkdefino.common.io.IOUtil;
import com.funkdefino.common.unittest.CTestCase;
import com.funkdefino.common.util.xml.XmlDocument;
import com.funkdefino.gsysconvert.util.*;
import junit.framework.Test;

/**
 * <p/>
 * <code>$Id: $</code>
 * @author Funkdefino (David M. Lang)
 * @version $Revision: $
 */
public final class GSysUnitTest extends CTestCase {

    //** ---------------------------------------------------------- Construction

    public GSysUnitTest(String sMethod) {
        super(sMethod);
    }

    //** ------------------------------------------------------------ Operations

    /**
     * Returns the suite of cases for testing.
     * @return the test suite.
     */
    public static Test suite() {
        return CTestCase.suite(GSysUnitTest.class,
                                  "UnitTest.xml",
                                      "test#1");
    }

    //** ----------------------------------------------------------------- Tests

    public void test01() throws Exception {
        byte[] arr = IOUtil.loadResource(getClass(), "GSysConvert.syx");
        for(byte b : arr)
            System.out.print(String.format("%02X ", b));
        System.out.println();
    }

    public void test02() throws Exception {
        XmlDocument doc = XmlDocument.fromResource(getClass(), "GSysConvert.xml");
        GSysConfig gsysconfig = new GSysConfig(doc.getRootElement());
        for(Bank bank : gsysconfig.getBanks()) {
            System.out.println(bank);
        }
    }

    public void test03() throws Exception {

        XmlDocument doc = XmlDocument.fromResource(getClass(), "GSysConvert.xml");
        GSysConfig gsysconfig = new GSysConfig(doc.getRootElement());
        byte arr[] = Format.execute(gsysconfig);
        StringBuffer sb = new StringBuffer();
        for(byte b : arr) {
            sb.append(String.format("%02X ", b));
        }

        System.out.println(sb.toString());

    }

    public void test04() throws Exception {

        assertEquals(0, convert("00000"));
        assertEquals(5, convert("00001"));
        assertEquals(4, convert("00010"));
        assertEquals(3, convert("00100"));
        assertEquals(2, convert("01000"));
        assertEquals(1, convert("10000"));

    }

    //** -------------------------------------------------------- Implementation

    /**
     * Trigger offset conversion - 5-bit wide bitmask to byte, with bit 5 as LSB
     * @param vt the string bitmask (XXXXX).
     * @return the byte offset, 0 - 5.
     */
    private static byte convert(String vt) {

        byte vtrg = Byte.parseByte(vt, 2);

        if(bitSet(vtrg,0)) return 5;
        if(bitSet(vtrg,1)) return 4;
        if(bitSet(vtrg,2)) return 3;
        if(bitSet(vtrg,3)) return 2;
        if(bitSet(vtrg,4)) return 1;

        return 0;

    }   // convert()

    /**
     * Bit test.
     * @param b the byte
     * @param n the bit (0 - 7).
     * @return true if set; otherwise false.
     */
    private static boolean bitSet(byte b, int n) {
        return (((b >> n) & 0x01) == 0x01);
    }

}   // class GSysUnitTest
