package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
/**
 * 
 * DataBaseConfig is a class to use a data base 
 * 
 * 
 * @author OpenClassRoom,Ravizé Aymeric
 * @version V1.1
 *
 */
public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.1
	 * 
	 * @see this function opens database connection by calling a configuration document that countain the identification informations related to this one
	 * @return a data base connection                  
	 */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");  
        String url="";
        String username="";
        String password="";
        
        final Properties prop = new Properties();
        InputStream input = null;
        try {
			input = new FileInputStream("src/main/resources/Config.properties");
			prop.load(input);
	        url =prop.getProperty("db.url");
	        username =prop.getProperty("db.username");
	        password =prop.getProperty("db.password");
			
		} catch (IOException ex) {
			logger.error("Error file or file data",ex);
		}finally {
			if (input != null) {
				try {
					input.close();
				} catch (final IOException e) {
					logger.error("Error close file",e);
				}
			}
		}
        return DriverManager.getConnection(url,username,password);
        
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function closes Data Base connection
	 *                  
	 */
    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function closes a prepared request
	 *                  
	 */
    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }
	/**
	 * 
	 * @author OpenClassRoom
	 * @version V1.0
	 * @since V1.0
	 * 
	 * @see this function closes a result of request
	 *                  
	 */
    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
