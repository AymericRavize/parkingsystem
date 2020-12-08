package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
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

}
