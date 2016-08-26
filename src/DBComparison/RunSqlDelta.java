/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBComparison;
import org.apache.log4j.Logger;

/**
 *
 * @author srivathg
 */
public class RunSqlDelta 
{
    public static String sqlDeltaToolLocation;
    public static String sdpFileLocation;
    static Logger log = Logger.getLogger(CompareDB.class.getName());
    
	// setData->To read the location of SQLDelta.exe file and sdp file which is the configuration file for SQLDelta.
    
    public void setData()
    {
        ReadProperties rp = new ReadProperties();
        sqlDeltaToolLocation="\""+rp.getProperty("SqlDeltaExe")+"\"";
        sdpFileLocation=rp.getProperty("SQlDeltaSdpFile");
    }
    
	// deltaCompare-> To run the given command in windows environment.
	
    public void deltaCompare(String inputCmd)
    {
        try
        {
            Runtime.getRuntime().exec(inputCmd);
        }
        catch(Exception e)
        {
            System.out.println("Problem in executing the sqldelta job");
            log.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
    
	/* executeSqlDelta-> This method is construct SQLDelta commandline executable.
		Results file should be part of sdp file
		Log file will be created in current folder */
	
  public void executeSqlDelta()
    {
        RunSqlDelta rsd = new RunSqlDelta();
        rsd.setData();
        String command = sqlDeltaToolLocation+" "+sdpFileLocation+" "+"/LOGNAME:myexecution.log";
        // results file should be part of .sdp (which is having xml like structure) file. sdp file's report section should have the result file name.
        try
        {
             rsd.deltaCompare(command);
        }
        catch(Exception e)
        {
            log.error(e.getMessage());
        }
    }
}
