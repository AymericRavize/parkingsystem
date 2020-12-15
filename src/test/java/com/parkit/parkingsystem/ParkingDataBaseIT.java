package com.parkit.parkingsystem;


import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
/**
 * 
 * ParkingDataBaseIT is a test class allowing to check if the tickets informations are correctly saved in the database  
 * 
 * 
 * @author OpenClassRoom,Ravizé Aymeric
 * @version V1.1
 *
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;    
    @Mock
    private static InputReaderUtil inputReaderUtil;

	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function is called to initialize the variables used in the class
	 *                  
	 */
    
    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function is called before every test to simulate the returned values through the mock and clear the database between each test
	 *                  
	 */
    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    
    
    
	/**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see check that a ticket is actually saved in the database and Parking table is updated with availability
	 *                  
	 */
    @Test  
    public void testParkingACar(){

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertEquals("ABCDEF", ticket.getVehicleRegNumber());
        assertEquals(1, ticket.getParkingSpot().getId());
        assertEquals(ParkingType.CAR, ticket.getParkingSpot().getParkingType());
        assertEquals(false, ticket.getParkingSpot().isAvailable());
        assertEquals(0, ticket.getPrice());      
        assertEquals((long)parkingService.inTime.getTime()/1000, (long)ticket.getInTime().getTime()/1000,1);
        assertEquals(null, ticket.getOutTime());

    }
	/**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see check that the fare generated and out time are populated correctly in the database
	 *                  
	 */
    @Test
    public void testParkingLotExit() throws InterruptedException{
    	testParkingACar();    
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Thread.sleep(500); // give a time to the database to end its treatments 
        parkingService.processExitingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertEquals(0, ticket.getPrice());
        assertEquals((long)parkingService.outTime.getTime()/1000,(long)ticket.getOutTime().getTime()/1000,1);
    }
    
    /**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see check if the function is not damaged after it reached the maximum places number
	 *                  
	 */
    @Test
    public void testParkingACarFull(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        
        while(parkingSpot != null && parkingSpot.getId() > 0) {
        	
        	parkingSpot.setAvailable(false);
        	parkingSpotDAO.updateParking(parkingSpot);
        	parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        	
        }
        
       assertEquals(null,parkingSpot);    	            
        
    }

    

}
