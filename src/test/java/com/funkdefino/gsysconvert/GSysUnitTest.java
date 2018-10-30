package com.funkdefino.gsysconvert;

import com.funkdefino.common.io.IOUtil;
import com.funkdefino.common.unittest.CTestCase;
import com.funkdefino.common.util.xml.XmlDocument;
import com.funkdefino.gsysconvert.util.convert.*;
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

    //** ----------------------------------------------------------------- Tests

    public void test04() throws Exception {

        assertEquals(0, convert("00000"));
        assertEquals(5, convert("00001"));
        assertEquals(4, convert("00010"));
        assertEquals(3, convert("00100"));
        assertEquals(2, convert("01000"));
        assertEquals(1, convert("10000"));

    }

    public void test05() throws Exception {

        assertEquals(0x00, VTrgConvert.convert("00000"));
        assertEquals(0x05, VTrgConvert.convert("00001"));
        assertEquals(0x04, VTrgConvert.convert("00010"));
        assertEquals(0x03, VTrgConvert.convert("00100"));
        assertEquals(0x02, VTrgConvert.convert("01000"));
        assertEquals(0x01, VTrgConvert.convert("10000"));

        assertEquals(0x00, STrgConvert.convert("00000.00000"));
        assertEquals(0x05, STrgConvert.convert("00000.00001"));
        assertEquals(0x04, STrgConvert.convert("00000.00010"));
        assertEquals(0x03, STrgConvert.convert("00000.00100"));
        assertEquals(0x02, STrgConvert.convert("00000.01000"));
        assertEquals(0x01, STrgConvert.convert("00000.10000"));
        assertEquals(0x50, STrgConvert.convert("00001.00000"));
        assertEquals(0x40, STrgConvert.convert("00010.00000"));
        assertEquals(0x30, STrgConvert.convert("00100.00000"));
        assertEquals(0x20, STrgConvert.convert("01000.00000"));
        assertEquals(0x10, STrgConvert.convert("10000.00000"));
        assertEquals(0x55, STrgConvert.convert("00001.00001"));
        assertEquals(0x44, STrgConvert.convert("00010.00010"));
        assertEquals(0x33, STrgConvert.convert("00100.00100"));
        assertEquals(0x22, STrgConvert.convert("01000.01000"));
        assertEquals(0x11, STrgConvert.convert("10000.10000"));

        assertEquals(0x00, LTrgConvert.convert("00000"));
        assertEquals(0x05, LTrgConvert.convert("00001"));
        assertEquals(0x09, LTrgConvert.convert("00011"));
        assertEquals(0x0C, LTrgConvert.convert("00111"));
        assertEquals(0x0E, LTrgConvert.convert("01111"));
        assertEquals(0x0F, LTrgConvert.convert("11111"));

    }   // test05()

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
