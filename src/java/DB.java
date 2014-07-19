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
import static javadatabasev0.Entry.USER;
import static javadatabasev0.User.DB_URL;

public class DB {

   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static String DB_URL = "jdbc:mysql://localhost/foodnotes";
   //  Database credentials
   static String USER = "root";
   static String PASS = "";

   public DB()
   {
        String path = System.getenv("OPENSHIFT_DATA_DIR");
        
        // if it's null it's NOT on openshift
        if (path != null) { 
           USER = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME"); 
           PASS = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
           
           String host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
           String port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
           String name = "foodnotes";
           
           //DB_URL = "jdbc:" + System.getenv("OPENSHIFT_MYSQL_DB_URL");
           DB_URL = "jdbc:mysql://" + host + ":" + port + "/" + name;
        }    
   
   }
   
   String getUserId(String userName, String userPass) throws SQLException, Exception {
      String userId = "";
      Statement stmt = null;
      Connection conn = null;
      

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();
         String sql;

         sql = "SELECT id FROM users WHERE userName='"
                 + userName + "' && password='" + userPass + "'";
         ResultSet rs = stmt.executeQuery(sql);
         rs.next();
         userId = rs.getString("id");
      } catch (SQLException e) {
         
         throw e;

      } finally {
         if (stmt != null) {
            stmt.close();
         }

         if (conn != null) {
            conn.close();
         }
      }
      return userId;
   }

   List<Entry> getEntries(String userId) {
      List<Entry> theEntries = new ArrayList();

      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();
         String sql;
         sql = "SELECT id, date, comments FROM entries WHERE userId=" + userId;
         ResultSet rs = stmt.executeQuery(sql);
         while (rs.next()) {
            String id = rs.getString("id");
            String date = rs.getString("date");
            String comments = rs.getString("comments");

            Entry temp = new Entry(id, userId, date, comments);

            theEntries.add(temp);
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

      return theEntries;
   }

   Boolean addEntry(List<String> pIngredients,
           List<String> pSymptoms, String pDate,
           String pComments, String userId) {
      Boolean additionComplete = false;
      Boolean entryAdded = false;

      Statement stmt = null;
      Connection conn = null;
      String entryId = "";

      try {
         Class.forName("com.mysql.jdbc.Driver");
         String sql;
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();

         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         java.util.Date preDate = formatter.parse(pDate);
         java.sql.Date theDate = new java.sql.Date(preDate.getTime());

         sql = "INSERT INTO entries (date, userId, comments) VALUES ('"
                 + theDate + "', " + userId + ", '" + pComments + "')";

         stmt.executeUpdate(sql);

         sql = "SELECT id FROM entries WHERE date='" + theDate + "'"
                 + " && comments='" + pComments + "' && userId=" + userId;

         ResultSet rs = stmt.executeQuery(sql);
         rs.next();
         entryId = rs.getString("id");
         stmt.close();
         conn.close();
         entryAdded = true;

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

      if (addIngredients(pIngredients, entryId)
              && addSymptoms(pSymptoms, entryId) && entryAdded) {
         additionComplete = true;
      }

      return additionComplete;
   }

   Boolean addIngredients(List<String> pIngredients, String entryId) {
      Boolean ingredientsAdded = false;
      Statement stmt = null;
      Connection conn = null;
      List<String> ingredientIds = new ArrayList();

      try {
         Class.forName("com.mysql.jdbc.Driver");
         String sql;
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();

         for (String ingredient : pIngredients) {
            ingredient = ingredient.toLowerCase();

            sql = "SELECT id, name FROM ingredients WHERE Name='"
                    + ingredient + "'";

            ResultSet rs = stmt.executeQuery(sql);

            //if no ingredient is returned then it's not in the list.
            if (!rs.next()) {
               sql = "INSERT INTO ingredients (NAME) VALUES ('"
                       + ingredient + "')";
               stmt.executeUpdate(sql);

               sql = "SELECT id FROM ingredients WHERE Name='" + ingredient + "'";
               rs = stmt.executeQuery(sql);
               rs.next();
               //ingredientIds.add(rs.getString("id"));
            }

            ingredientIds.add(rs.getString("id"));

            updateIngredientId(ingredientIds, entryId);
            stmt.close();
            conn.close();
            ingredientsAdded = true;
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
      return ingredientsAdded;
   }

   void updateIngredientId(List<String> pIds, String entryId) {
      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         String sql;
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();

         for (String i : pIds) {
            //System.out.println("Ingredient id: " + i);

            sql = "INSERT INTO ingredientid (ingredientId, entryId)"
                    + "VALUES (" + i + "," + entryId + ")";
            stmt.executeUpdate(sql);
         }
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

   Boolean addSymptoms(List<String> pSymptoms, String entryId) {
      Boolean symptomsAdded = false;
      Statement stmt = null;
      Connection conn = null;
      List<String> symptomIds = new ArrayList();

      try {
         Class.forName("com.mysql.jdbc.Driver");
         String sql;
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();

         for (String symptom : pSymptoms) {
            symptom = symptom.toLowerCase();
            sql = "SELECT id, name FROM symptoms WHERE name='"
                    + symptom + "'";

            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
               sql = "INSERT INTO symptoms (name) VALUES ('"
                       + symptom + "')";
               stmt.executeUpdate(sql);

               sql = "SELECT id FROM symptoms WHERE name='" + symptom + "'";

               rs = stmt.executeQuery(sql);
               rs.next();
            }
            symptomIds.add(rs.getString("id"));
         }
         updateSymptomEntry(symptomIds, entryId);
         stmt.close();
         conn.close();
         symptomsAdded = true;

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

      return symptomsAdded;
   }

   void updateSymptomEntry(List<String> pIds, String pEntryId) {
      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         String sql;
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();

         for (String i : pIds) {
            //System.out.println("Ingredient id: " + i);

            sql = "INSERT INTO symptomentry (symptomId, entryId)"
                    + "VALUES (" + i + "," + pEntryId + ")";
            stmt.executeUpdate(sql);
         }
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

   List<Ingredient> gatherIngredients(String entryId) {
      List<String> ingredId = new ArrayList();
      List<Ingredient> theIngredients = new ArrayList();
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
      theIngredients = gatherIngredientName(ingredId);
      return theIngredients;
   }

   List<Ingredient> gatherIngredientName(List<String> ingredId) {
      List<Ingredient> theIngredients = new ArrayList();
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

            //rs.close();
            stmt.close();
            conn.close();

         }

         return theIngredients;

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
      return theIngredients;
   }

   List<Symptom> gatherSymptoms(String entryId) {
      List<Symptom> theSymptoms = new ArrayList();
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
            //System.out.println("id: " + rs.getString("symptomId"));
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

      theSymptoms = gatherSymptomNames(symptomId);
      return theSymptoms;
   }

   List<Symptom> gatherSymptomNames(List<String> symptomId) {
      Symptom temp = null;
      Statement stmt = null;
      Connection conn = null;
      List<Symptom> theSymptoms = new ArrayList();

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
         return theSymptoms;
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
      return theSymptoms;
   }

   Boolean addUser(String userName, String pass) {
      Boolean userAdded = false;

      Symptom temp = null;
      Statement stmt = null;
      Connection conn = null;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);

         stmt = conn.createStatement();
         String sql;

         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();

         sql = "INSERT INTO users (userName, password) VALUES ('" + userName
                 + "', '" + pass + "')";
         stmt.executeUpdate(sql);

         stmt.close();
         conn.close();
         userAdded = true;

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

      return userAdded;
   }
}