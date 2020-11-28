package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        TicketDAO td = new TicketDAO();
        //double inHour = ticket.getInTime().getHours() + (ticket.getInTime().getMinutes()/60);// a modif ?
        //double outHour = ticket.getOutTime().getHours() + (ticket.getOutTime().getHours()/60);
        //getHour ne recupere que l heure  / modifier pour calculer en minute ?
        //TODO: Some tests are failing here. Need to check if this logic is correct
        //double duration = outHour - inHour;

        double duration = (ticket.getOutTime().getTime() - ticket.getInTime().getTime());// soustrai les 2 date etconverti le forma en heure

        duration= duration/(1000*60*60);

     
        if(duration>=0.5) {
        	if(td.getNbTiket(ticket.getVehicleRegNumber())>1) {
        		duration=duration*0.95;
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