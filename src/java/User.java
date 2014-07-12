/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadatabasev0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static javadatabasev0.JavaDataBasev0.DB_URL;

/**
 *
 * @author Makz
 */
public class User {

   private List<Entry> theEntries;
   String userId;
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/foodnotes";
   //  Database credentials
   static final String USER = "root";
   static final String PASS = "";

   User(String pUserName, String pUserPass) {
      //TODO fix this so it can be other users.
      userId = "2";
      createEntryList();
   }

   void createEntryList() {
      Statement stmt = null;
      Connection conn = null;

      theEntries = new ArrayList();
      
      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();
         String sql;
         sql = "SELECT id, date, userId, comments FROM entries WHERE userId=" + userId;
         ResultSet rs = stmt.executeQuery(sql);
         while (rs.next()) {
            String id = rs.getString("id");
            String date = rs.getString("date");
            String userId = rs.getString("userId");
            String comments = rs.getString("comments");

            Entry temp = new Entry(id, userId, date, comments);
            getTheEntries().add(temp);
         }
         rs.close();
         stmt.close();
         conn.close();
      } catch (Exception e) {
         System.out.println(e);

      } finally {
         try {
            if (stmt != null) {
               stmt.close();
            }
         } catch (SQLException se2) {
         }
         try {
            if (conn != null) {
               conn.close();
            }
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   public void addEntry()
   {
      
   }
   
   /**
    * @return the theEntries
    */
   public List<Entry> getTheEntries() {
      return theEntries;
   }
   
   
}
