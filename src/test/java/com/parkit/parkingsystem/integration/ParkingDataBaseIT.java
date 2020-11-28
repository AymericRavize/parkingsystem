package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
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

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;    
    private static Date dateTestIn;
    private static Date dateTestOut;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    //private static Ticket ticket;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        //dateTestIn = new Date(2020, 10, 5, 2, 22, 33);
        //dateTestOut = new Date(2020, 10, 5, 3, 22, 33);
    }

    
    //@ParameterizedTest(name = "imatriculation {0}")
	//@ValueSource(strings = { "ABCDEF","ABCDEQ","ABCDGF" })
    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);// exemple de mock
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");// le decaler dans les fonction pour faire des apel diff ?
        //final Date dt = Mockito.mock(Date.class);
        //when(dt.getDate()).thenReturn(dateTestIn);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){
    	
    }
    /*
    @AfterEach
    private static void tearDownPerTest(){
    	
    }*/
            //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    @Test  //tester avec place disponible et sans place disponible / faire  2 fonction de test
    public void testParkingACar(){
    	//when(new Date()).thenReturn(dateTestIn);// permer d'avoir une date specifique  une alternative ?
    	//expect(getDate()).toEqual(new Date(2020, 10, 5, 2, 22, 33));
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        parkingService.processIncomingVehicle();// mock prend en charge les foction ""
        Ticket ticket = ticketDAO.getTicket("ABCDEF");//plaque imatriculation identique attention ?
        //assertThat(inTime,dateTestIn);
        //assertThat(new Date(), dateTestIn);
        // voir comment utiliser @spy
        //System.out.println(ticket);
        //System.out.println("testddde");
        //verify (val).mafonction(v1,v2,v..);
        /*
        Connection con = null;
        Ticket ticket = null;
        
        try {
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,"ABCDEF");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber("ABCDEF");
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
            }
            dataBaseTestConfig.closeResultSet(rs);
            dataBaseTestConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            System.out.println("Error");
        }finally {
        	dataBaseTestConfig.closeConnection(con);
        }
        */
        //System.out.println("teste");
        assertEquals("ABCDEF", ticket.getVehicleRegNumber());
        assertEquals(1, ticket.getParkingSpot().getId());
        assertEquals(ParkingType.CAR, ticket.getParkingSpot().getParkingType());
        assertEquals(false, ticket.getParkingSpot().isAvailable());//verif la logique
        assertEquals(0, ticket.getPrice());
        assertEquals(parkingService.inTime.getDate(), ticket.getInTime().getDate());//in time ???
        assertEquals(null, ticket.getOutTime());
        
        
        // verifier que tt les info rentrer son bonne par raport au vehicul simuler
       //faire un apel a la bd comme avec la class tiketdao
        //asertequal("ABCDEF", parkingService.);
    }
    
    @Test
    public void testParkingLotExit() throws InterruptedException{
    	testParkingACar();
    	//when(new Date()).thenReturn(dateTestOut);// permer d'avoir une date specifique       
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Thread.sleep(500);
        parkingService.processExitingVehicle();
        
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        //TODO: check that the fare generated and out time are populated correctly in the database
        
        assertEquals(0, ticket.getPrice());// metre la valeur atendu apre avoir reussi a focer les heure
        assertEquals(parkingService.outTime.getDate(), ticket.getOutTime().getDate());// Erreur
    }
    
  //tester avec place disponible et sans place disponible / faire  2 fonction de test
    //pb lier a la liberation des placce surement a verif
    @Test
    public void testParkingACarFull(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();// Erreur a enlever ?
        //"select count(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = CAR";
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        while(parkingSpot != null && parkingSpot.getId() > 0) {
        	parkingSpot.setAvailable(false);
        	System.out.println("///////////////////////////////////");
        	System.out.println("id :" + parkingSpot.getId());
        	System.out.println("type :" +parkingSpot.getParkingType());
        	System.out.println("valable ->" + parkingSpot.isAvailable());
        	System.out.println("///////////////////////////////////");
        	
        	parkingSpotDAO.updateParking(parkingSpot);
        	parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        }
        /*
        ParkingSpot ps1 = new ParkingSpot(1,ParkingType.CAR,false);
        ParkingSpot ps2 = new ParkingSpot(2,ParkingType.CAR,false);
        ParkingSpot ps3 = new ParkingSpot(3,ParkingType.CAR,false);
        parkingSpotDAO.updateParking(ps1);
        parkingSpotDAO.updateParking(ps2);
        parkingSpotDAO.updateParking(ps3);
        // juste remplir a false les place
        
        System.out.println("///////////////////////////////////");
        System.out.println(parkingSpot);
        System.out.println("///////////////////////////////////");
        ps1.setAvailable(true);
        ps2.setAvailable(true);
        ps3.setAvailable(true);
        */
       assertEquals(null,parkingSpot);
    	
             
        
    }

    
//TODO faire une foction qui teste les calcule 
}
