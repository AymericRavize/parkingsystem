package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
/**
 * 
 * this class allows to calculate the price of the tickets 
 * 
 * @author OpenClassRoom ,Ravizé Aymeric
 * @version V1.1
 *
 */
public class FareCalculatorService {
	/**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see this function allows to calculate the price to pay in function of the vehicule type, the recurrence of use of the service and the parking time of the user
	 *                  
	 */
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        TicketDAO td = new TicketDAO();

        double duration = (ticket.getOutTime().getTime() - ticket.getInTime().getTime());// subtract both dates  

        duration= duration/(1000*60*60); // convert the format in hours

        // if more than 30 minutes
        if(duration>=0.5) {
        	if(td.getNbTiket(ticket.getVehicleRegNumber())>1) { // if recurrent
        		duration=duration*0.95;// discount of  5%
        	}
        	System.out.println(ticket.getParkingSpot().getParkingType());
	            switch (ticket.getParkingSpot().getParkingType()){
	            case CAR: {
	                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
	                break;
	            }
	            case BIKE: {
	                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
	                break;
	            }
	            default: throw new IllegalArgumentException("Unkown Parking Type");
	        }
        }
        else {
        	ticket.setPrice(0);
        }

    }
}