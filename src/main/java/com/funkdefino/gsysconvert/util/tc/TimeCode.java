package com.funkdefino.gsysconvert.util.tc;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class TimeCode {

    //** ------------------------------------------------------------- Constants

    public static long FPS = 30L;

    //** ------------------------------------------------------------------ Data

    public int frames;
    public int seconds;
    public int minutes;
    public int hours;

    //** ---------------------------------------------------------- Construction

    public TimeCode(int frames, int seconds, int minutes, int hours) {
        this.frames  = frames;
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours   = hours;
    }

    //** ------------------------------------------------------------ Operations

    /**
     * Converts the time code value to frames.
     * @return the number of frames.
     */
    public long toFrames() {
        int minutes = (this.hours * 60) + this.minutes;
        int seconds = (minutes * 60) + this.seconds;
        return (seconds * FPS) + this.frames;
    }

    /**
     * Converst a frame delta to milliseconds.
     * @param delta the delta
     * @return th enumber of milliseconds.
     */
    public static double toMillis(long delta) {
        return (1000.0 * delta) / FPS;
    }

    @Override
    public String toString() {
        return String.format("[%02d:%02d:%02d:%02d]", hours, minutes, seconds, frames);
    }

}   // class TimeCode
