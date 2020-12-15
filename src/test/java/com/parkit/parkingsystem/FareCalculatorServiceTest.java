package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Date;
/**
 * 
 * FareCalculatorServiceTest is a test class to check if the calculate price is true   
 * 
 * 
 * @author OpenClassRoom,Ravizé Aymeric
 * @version V1.1
 *
 */
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private  Ticket ticket;

	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function create object fareCalculatorService befor all test
	 *                  
	 */    
    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function create one object Ticket befor each test
	 *                  
	 */ 
    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function test if a price of tiket is corecte after 1hour for a car
	 *                  
	 */ 
    @Test
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function test if a price of tiket is corecte after 1hour for a bike
	 *                  
	 */
    @Test
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function test a eror if a type is null
	 *                  
	 */
    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function test a eror if a time d enter est superieur a la sorti
	 *                  
	 */
    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function test if a price of tiket is corecte after 45 mn for a bike
	 *                  
	 */
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function test if a price of tiket is corecte after 45 mn for a car
	 *                  
	 */
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        Date outTime=new Date(); 
        inTime.setTime(outTime.getTime() - (45 * 60 * 1000));
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function test if a price of tiket is corecte after 1day for a car
	 *                  
	 */
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
	/**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see this function test if a price of tiket is corecte if less than 30 minutes 
	 *                  
	 */
    @Test
    public void calculateFareCarWithLessThanThirtyMinutesParkingTime(){
        Date inTime = new Date();
        Date outTime=new Date(); 
        inTime.setTime(outTime.getTime() - (29 * 60 * 1000));
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice());
    }
	/**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see this function test if a price of tiket is corecte for car
	 *                  
	 */
	@ParameterizedTest(name = "{0} heure car test")
	@ValueSource(ints = { 29,60,4 * 60,24 * 60 })
    public void calculateFareCarTime(int time){
        Date inTime = new Date();
        Date outTime=new Date(); 
        inTime.setTime(outTime.getTime() - (time * 60 * 1000));
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((time/60 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }
	
	/**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see this function test if a price of tiket is corecte for bike
	 *                  
	 */
	@ParameterizedTest(name = "{0} heure bike test")
	@ValueSource(ints = { 29,60,4 * 60,24 * 60 })
    public void calculateFareBikeTime(int time){
        Date inTime = new Date();
        Date outTime=new Date(); 
        inTime.setTime(outTime.getTime() - (time * 60 * 1000));
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((time/60 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }


}
