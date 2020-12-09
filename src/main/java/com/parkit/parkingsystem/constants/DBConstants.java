package com.parkit.parkingsystem.constants;
/**
 * 
 * DBConstants is a class with the prepared requests ready to be used in the rest of the application 
 * 
 * 
 * @author OpenClassRoom,Ravizé Aymeric
 * @version V1.1
 *
 */
public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    public static final String GET_NB_TIKET ="select count(ID) from ticket  where VEHICLE_REG_NUMBER=? and IN_TIME>=(NOW() - INTERVAL 7 DAY)";// number of tickets for a plate during the last 7 days
}
