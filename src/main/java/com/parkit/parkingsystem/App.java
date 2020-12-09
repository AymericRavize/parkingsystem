package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 
 * App is a class to start the application   
 * 
 * 
 * @author OpenClassRoom
 * @version V1.0
 *
 */
public class App {
    private static final Logger logger = LogManager.getLogger("App");
    public static void main(String args[]){
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}

