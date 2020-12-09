package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
/**
 * 
 * InputReaderUtil contains the functions allowing to collect the data entered by the user
 * 
 * 
 * @author OpenClassRoom
 * @version V1.0
 *
 */
public class InputReaderUtil {

    private static Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function resend what the user has entered if it correspond to an integer 
	 * @return what the user has entered or -1 if it's incorrect                  
	 */
    public int readSelection() {
        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function resend to what the user has entered for his car plate 
	 * @return what the user has entered                   
	 */
    public String readVehicleRegistrationNumber() throws Exception {
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
