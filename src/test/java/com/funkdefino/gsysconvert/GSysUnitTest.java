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

}   // class ConfigurationUnitTest
