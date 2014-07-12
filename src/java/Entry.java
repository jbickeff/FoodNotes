package javadatabasev0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static javadatabasev0.JavaDataBasev0.DB_URL;

public class Entry {

   private String entryId;
   private String userId;
   private String date;
   private String comments;
   private List<Symptom> theSymptoms;
   private List<Ingredient> theIngredients;
   static final String USER = "root";
   static final String PASS = "";

   Entry(String pentryId, String puserId, String pdate, String pcomments) {
      entryId = pentryId;
      userId = puserId;
      date = pdate;
      comments = pcomments;

      gatherIngredients();
      gatherSymptoms();
   }

   void gatherIngredients() {
      theIngredients = new ArrayList();
      List<String> ingredId = new ArrayList();
      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);

         stmt = conn.createStatement();
         String sql;

         sql = "SELECT ingredientId FROM ingredientId WHERE entryId=" + entryId;
         ResultSet rs = stmt.executeQuery(sql);

         while (rs.next()) {
            ingredId.add(rs.getString("ingredientId"));
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
      gatherIngredientName(ingredId);
   }

   void gatherIngredientName(List<String> ingredId) {
      Ingredient temp = null;
      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);

         stmt = conn.createStatement();
         String sql;

         for (String theId : ingredId) {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "SELECT Name FROM ingredients WHERE id=" + theId;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            temp = new Ingredient(rs.getString("Name"), theId);
            theIngredients.add(temp);

            rs.close();
            stmt.close();
            conn.close();

         }
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

   void gatherSymptoms() {
      theSymptoms = new ArrayList();
      List<String> symptomId = new ArrayList();
      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);

         stmt = conn.createStatement();
         String sql;

         sql = "SELECT symptomId FROM symptomentry WHERE entryId=" + entryId;
         ResultSet rs = stmt.executeQuery(sql);

         while (rs.next()) {
            symptomId.add(rs.getString("symptomId"));
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
      gatherSymptomNames(symptomId);
   }

   void gatherSymptomNames(List<String> symptomId) {
      Symptom temp = null;
      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);

         stmt = conn.createStatement();
         String sql;

         for (String theId : symptomId) {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "SELECT Name FROM symptoms WHERE id=" + theId;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            temp = new Symptom(rs.getString("Name"), theId);
            theSymptoms.add(temp);
            rs.close();
            stmt.close();
            conn.close();

         }
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

   /**
    * @return the entryId
    */
   public String getEntryId() {
      return entryId;
   }

   /**
    * @return the userId
    */
   public String getUserId() {
      return userId;
   }

   /**
    * @return the date
    */
   public String getDate() {
      return date;
   }

   /**
    * @return the comments
    */
   public String getComments() {
      return comments;
   }

   /**
    * @return the theSymptoms
    */
   public List<Symptom> getTheSymptoms() {
      return theSymptoms;
   }

   /**
    * @return the theIngredients
    */
   public List<Ingredient> getTheIngredients() {
      return theIngredients;
   }
}
