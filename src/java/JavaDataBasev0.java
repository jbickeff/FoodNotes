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

   public static void main(String[] args) {
      Connection conn = null;
      Statement stmt = null;

      theUser = new User("Makz", "pass");
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
   }
}