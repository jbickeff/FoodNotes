package javadatabasev0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class User {

   DB theDataBase;
   private List<Entry> theEntries;
   String userId;
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/foodnotes";
   //  Database credentials
   static final String USER = "root";
   static final String PASS = "";
   String userName;
   
   public User(String pUserId) throws Exception
   {
      userId = pUserId;      
      theDataBase = new DB();
      theEntries = new ArrayList();
      userName = theDataBase.getUserName(userId);
      createEntryList();
   }
   
   public String getUserName()
   {
      return userName;
   }
   
   public User(String pUserName, String pUserPass) throws Exception {
      userName = pUserName;
      theDataBase = new DB();
      theEntries = new ArrayList();
      
      userId = theDataBase.getUserId(pUserName, pUserPass);
      createEntryList();
   }
   
   public String getId()
   {
       return userId;
   }
   @Override
   public String toString() {
      String userStr;
      System.out.println(userId);
      userStr = "here userId: " + userId;
      return userStr;
   }

   void createEntryList() throws Exception {
      theEntries = theDataBase.getEntries(userId);
   }

   public void addEntry(List<String> pIngredients,
           List<String> pSymptoms, String pDate,
           String pComments) throws Exception {

      theDataBase.addEntry(pIngredients, pSymptoms, pDate,
              pComments, userId);
   }

   /**
    * @return the theEntries
    */
   public List<Entry> getTheEntries() {
      return theEntries;
   }
   
   public void upDateEntry(List<String> pIngredients,
           List<String> pSymptoms, String pComments, int entryID) throws Exception 
   {
       theDataBase.upDateEntry(pIngredients, pSymptoms,
              pComments, entryID, userId);
   }
   
}
