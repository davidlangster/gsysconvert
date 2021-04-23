package com.funkdefino.gsysconvert.util.bpm;

import com.funkdefino.common.unittest.CTestCase;
import com.funkdefino.gsysconvert.util.tc.TimeCode;
import junit.framework.Test;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class TimeCodeUnitTest extends CTestCase {

    //** ------------------------------------------------------------- Constants

    private static int BPM  = 130;
    private static int PPQN = 24;
    private static int FPS  = 30;

    //** ---------------------------------------------------------- Construction

    public TimeCodeUnitTest(String sMethod) {
         super(sMethod);
     }

    //** ------------------------------------------------------------ Operations

    /**
     * Returns the suite of cases for testing.
     * @return the test suite.
     */
    public static Test suite() {
        return CTestCase.suite(TimeCodeUnitTest.class,
            "UnitTest.xml",
            "test#4");
    }

    //** ----------------------------------------------------------------- Tests

    public void test01() {
        TimeCode tc = new TimeCode(0,0,0,1);
        System.out.println(tc.toFrames());
        System.out.println(tc);
    }

    public void test02() {
        for(int i = 0; i <= 30; i++) {
            System.out.println(TimeCode.toMillis(i));
        }
    }

    public void test03() {
        System.out.println(BpmUtil.toPeriod(BPM));
    }

    public void test04() {

        //** ----
        // long frames = 30; // 1 second delta
        // double t = TimeCode.toMillis(frames); // frames delta => milliseconds
        // double period = BpmUtil.toPeriod(BPM);
        // double clocks = t / period;
        // System.out.println(clocks);
        //** ----

        //** ----
        for(int i = 0; i < 30; i++) {
            double clocks = toClocks(i);  // Compressed calculation method
            System.out.println(String.format("[s%02d] %9f; %02d", i, clocks, Math.round(clocks)));
        }
        //** ----

    }   // test04()

    public void test05() {

        toBars(new TimeCode( 0, 0,1,0));  // F:S:M:H
        toBars(new TimeCode(25,36,1,0));  // F:S:M:H
        toBars(new TimeCode(21,16,3,0));  // F:S:M:H
        toBars(new TimeCode(0,  5,0,1));  // F:S:M:H

    }   // test05()

    //** -------------------------------------------------------- Implementation

    private static double toClocks(double frames){
        return (frames * BPM * PPQN) / (60 * FPS);
    }

    private static void toBars(TimeCode tc) {

        long clocks = Math.round(toClocks(tc.toFrames()));

        int bars   = (int)(clocks / 96);
        clocks    -= (bars  * 96);
        int beats  = (int)(clocks / 24);
        clocks    -= (beats * 24);
        int frames = (int)clocks;

        System.out.println(String.format("[%02d:%02d:%02d]", bars+1, beats+1, frames));

    }   // toBars()

}   // class TimeCodeUnitTest
