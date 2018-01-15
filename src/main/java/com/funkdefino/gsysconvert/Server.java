package com.funkdefino.gsysconvert;

import com.funkdefino.common.util.xml.XmlDocument;
import com.funkdefino.gsysconvert.util.*;
import java.io.*;

/**
 * <p>
 * <code>$Id: $</code>
 * @author Funkdefino (David M. Lang)
 * @version $Revision: $
 */
public final class Server {

    //** ----------------------------------------------------------- Application

    /**
     * Application entry point.
     * @param args command-line arguments.
     */
    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println("Usage : Server [configuration]");
            return;
        }

        try {
            XmlDocument doc = new XmlDocument(new File(args[0]), false);
            new Server(new GSysConfig(doc.getRootElement()));
        }
        catch(Exception excp) {
            excp.printStackTrace();
        }

    }   // main()

    //** ---------------------------------------------------------- Construction

    /**
    * Ctor.
    * @param gsysconfig a Preset value object.
    * @throws Exception on error.
    */
    private Server(GSysConfig gsysconfig) throws Exception {

        System.out.println("**********************************");
        System.out.println("GSysSelect Conversion Utility ****");
        System.out.println("David Lang \u00A9 2018             ****");
        System.out.println("**********************************");

        for(Bank bank : gsysconfig.getBanks()) {
            System.out.println(bank);
        }

        FileOutputStream fos = new FileOutputStream(gsysconfig.getDestination());
        fos.write(Format.execute(gsysconfig));
        fos.flush();
        fos.close();

    }   // Server

}   // class Server
