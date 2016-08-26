/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Log4j.PropConfigurator;
import ReadPropertiespkg.ReadProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author $hpedservice
 */
public class openkwysConnection {

    public Connection connection_kwys = null;
    public Statement statement_kwys;

    static Logger log = Logger.getLogger(openkwysConnection.class.getName());

    public void openDatabaseConnection() throws ClassNotFoundException {
        PropConfigurator.configure();
        
        ReadProperties rp = new ReadProperties();
        log.info(" Opening Database Connection");
        Class.forName("org.sqlite.JDBC");
        try {
            connection_kwys = DriverManager.getConnection("jdbc:sqlite:" + rp.getProperty("KWYS_Location"));
                 //System.out.println("After get property");
            //connection_kwys = DriverManager.getConnection("jdbc:sqlite:"+ "C:\\CoreAutomation\\DS_KWYS.dat");
            statement_kwys = connection_kwys.createStatement();
            statement_kwys.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {
            log.error(" Database Connection Unsuccessfull"); //Edited by Avin 
            log.error(e.getMessage());
        }

    }

}
