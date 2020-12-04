package com.parkit.parkingsystem.service;

//import com.parkit.parkingsystem.cette;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	/**
	 * 
	 * @author Ravizé Aymeric
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see cette fontion permet de calculer le prix a payer en fonction du type de vehicule ,de la recurence de location de lutilisateur et du temps de stasionement
	 *                  
	 */
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        TicketDAO td = new TicketDAO();

        double duration = (ticket.getOutTime().getTime() - ticket.getInTime().getTime());// soustrai les 2 date  

        duration= duration/(1000*60*60); // converti le forma en heure

        // si plus de 30 minute
        if(duration>=0.5) {
        	if(td.getNbTiket(ticket.getVehicleRegNumber())>1) { // si recurent
        		duration=duration*0.95;// reduction de  5%
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