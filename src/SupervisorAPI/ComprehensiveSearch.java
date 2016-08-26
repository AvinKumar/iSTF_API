/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SupervisorAPI;

import ReadPropertiespkg.ReadProperties;
import Database.databaseConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author sayedmo
 */
public class ComprehensiveSearch {

    public Connection conn = null;
    public Statement statement = null;
    public static ComprehensiveSearch c = new ComprehensiveSearch();
    databaseConnection sqldb=new databaseConnection();
    ReadProperties rp = new ReadProperties();
    
    public static void main(String[] args) throws Exception {
        
        //c.dbConnect("jdbc:sqlserver://s6db-white;databaseName=s6white1;", "sa", "p@ssw0rd");
        //c.dbConnect("jdbc:sqlserver://16.150.56.142;databaseName=S9_QA_RAP;", "sa", "p@ssw0rd");
        //c.CompSearch("001","jdbc:sqlserver:"+rp.getProperty("sqlhostanddb"), rp.getProperty("sqlusername"), rp.getProperty("sqlpassword"));
        //c.CompSearch("001");
        
        //c.CloseConnection();
    }

    public void CompSearch(String testcasegid, String SearchID) throws Exception {
        ResultSet rs1, rs2, rs3, rs4;
        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            //System.out.println(""+db_password);
//            //System.out.println(""+db_connect_string);
//            conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
//
//            System.out.print("connected");
            sqldb.dbConnect("jdbc:sqlserver:"+rp.getProperty("sqlhostanddb"), rp.getProperty("sqlusername"), rp.getProperty("sqlpassword"));

            String searchtemptable, searchtemptable1, from_table, status, results, statusSearch, statusFrom, gidofsearchtable, finaltestdatatable;
            int count = 0;

            //Statement sta = conn.createStatement();
            String Sql = "SELECT table_name, status, num_hits FROM dbo.COMP_SEARCH_CACHE where search_id='"+SearchID+"'";
            ResultSet rs = sqldb.statement.executeQuery(Sql);

            while (rs.next()) {
                searchtemptable = rs.getString("table_name");
                finaltestdatatable = searchtemptable + "Test";
                status = rs.getString("status");
                results = rs.getString("num_hits");
                System.out.println(searchtemptable);
                searchtemptable1 = searchtemptable + ".Singlecast_ID";
                System.out.println(searchtemptable1);

                if (results != null) {
                    //Check if table exist, if not create it
                    DatabaseMetaData meta = sqldb.conn.getMetaData();
                    rs1 = meta.getTables(null, null, "automationSearchRef", null);
                    if (!rs1.next()) {
                        //table does not exist. 
                        System.out.println("Table does not exist, hence created the table and inserted the record");
                        sqldb.statement.executeUpdate("CREATE TABLE automationSearchRef ("
                                + "GID int IDENTITY(1,1) PRIMARY KEY,"
                                + "TESTCASEID int NOT NULL,"
                                + "TABLENAME varchar(255),"
                                + "STATUS int,"
                                + "NUM_HITS int,"
                                + "FINALDATATABLE varchar(255))");
                        sqldb.statement.executeUpdate("INSERT INTO dbo.automationSearchRef (TESTCASEID,TABLENAME,STATUS,NUM_HITS, FINALDATATABLE) VALUES ('" + testcasegid + "','" + searchtemptable + "','" + status + "','" + results + "','" + finaltestdatatable + "')");
                    } else {
                        //table exists. 
                        System.out.println("Table exist, hence inserted the record");
                        sqldb.statement.executeUpdate("INSERT INTO dbo.automationSearchRef (TESTCASEID,TABLENAME,STATUS,NUM_HITS, FINALDATATABLE) VALUES ('" + testcasegid + "','" + searchtemptable + "','" + status + "','" + results + "','" + finaltestdatatable + "')");
                    }

                    String Sql1 = "select top 1 gid, status from dbo.automationSearchRef order by GID desc;";
                    rs2 = sqldb.statement.executeQuery(Sql1);
                    rs2.next();
                    statusSearch = rs2.getString("status");
                    gidofsearchtable = rs2.getString("gid");
                    while (!"4".equals(statusSearch) || !"7".equals(statusSearch)) {
                        rs3 = sqldb.statement.executeQuery("select status from dbo.COMP_SEARCH_CACHE where table_name='" + searchtemptable + "'");
                        rs3.next();

                        statusFrom = rs3.getString("status");
                        sqldb.statement.executeUpdate("update dbo.automationSearchRef set STATUS='" + statusFrom + "' where GID='" + gidofsearchtable + "'");

                        String Sql2 = "select top 1 gid, status from dbo.automationSearchRef order by GID desc;";
                        rs2 = sqldb.statement.executeQuery(Sql2);
                        rs2.next();
                        statusSearch = rs2.getString("status");

                        count++;
                        if (count == 100) {
                            break;
                        }
                    }
                    if (statusSearch.equals("4") || statusSearch.equals("7")) {

                        String sql3 = "SELECT  dbo.Singlecast.Subject,dbo.Singlecast.BaseSubjectHash,dbo.Singlecast.ContentType,dbo.Singlecast.Direction,dbo.Singlecast.EmailActionID,dbo.Singlecast.IsExported,dbo.Singlecast.SenderDisplayName,dbo.Singlecast.RecipientDisplayName,dbo.Singlecast.RecipientEmailAddress,dbo.Singlecast.ISFLAGGED,dbo.Singlecast.DATEREAD,dbo.Singlecast.HasUndisclRecip,dbo.Singlecast.LabelID,dbo.Singlecast.ReviewStatus,dbo.Singlecast.MessageSize,dbo.Singlecast.IsEncrypted,dbo.Singlecast.NumAttachments into " + finaltestdatatable + " FROM dbo.Singlecast INNER JOIN " + searchtemptable + " ON dbo.Singlecast.ID = " + searchtemptable1 + "";
                        //System.out.println(sql3);
                        System.out.println("Created Final Data Table");
                        sqldb.statement.executeUpdate(sql3);
                    }
                } else {
                    System.out.println("Query does not return any results or not processed");
                    sqldb.statement.executeUpdate("INSERT INTO dbo.automationSearchRef (TESTCASEID,TABLENAME,STATUS,NUM_HITS) VALUES ('" + testcasegid + "','" + searchtemptable + "','" + status + "','0')");
                }
            }

        } catch (SQLException e) {
            System.out.print("I am in comp search"+e.getMessage());
            
            // log.error(" Error in Connecting Database" + e.getMessage());
        }
    }

    public void CloseConnection() {
        try {

            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
        }//log.error("Error in closing Connection and statement" + e.getMessage());}
    }

}
