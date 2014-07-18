package javadatabasev0;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class JavaDataBasev0 {

   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/foodnotes";
   //  Database credentials
   static final String USER = "root";
   static final String PASS = "";
   static String userId = "2";
   static List<Entry> theEntries = new ArrayList();
   static User theUser;
   static private DB theDataBase;
   

   public static void main(String[] args) {
      Connection conn = null;
      Statement stmt = null;
      theDataBase = new DB();
      
      theUser = new User("Makz", "pass");
      
      List<String> itestString = new ArrayList();
      itestString.add("testing ingredient");

      List<String> stestString2 = new ArrayList();
      stestString2.add("test problem");
      
      theDataBase.addUser("Jeff", "ponies");
      //theUser.addEntry(itestString, stestString2, 
      //        "2014-09-15 05:30:00", "test comment");
      outputEntries();
   }

   static void outputEntries() {

      for (Entry e : theUser.getTheEntries()) {
         System.out.print("Entry ID: " + e.getEntryId() + "\n");
         System.out.print("Date: " + e.getDate() + "\n");
         System.out.print("userId: " + e.getUserId() + "\n");
         System.out.print("Comments: " + e.getComments() + "\n");

         System.out.println("Items Consumed:");
         for (Ingredient i : e.getTheIngredients()) {
            System.out.println("     " + i.getIngredient());
         }

         System.out.println("Symptoms Experienced:");
         e.getTheSymptoms().size();
         for (Symptom s : e.getTheSymptoms()) {
            System.out.println("     " + s.getName());
         }
      }
      
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println();
   }
}