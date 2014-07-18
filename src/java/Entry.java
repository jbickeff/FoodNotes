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

   private DB theDataBase;
   private String entryId;
   private String userId;
   private String date;
   private String comments;
   private List<Symptom> theSymptoms;
   private List<Ingredient> theIngredients;
   static final String USER = "root";
   static final String PASS = "";

   Entry(String pentryId, String puserId, String pdate, String pcomments) {
      theDataBase = new DB();
      entryId = pentryId;
      userId = puserId;
      date = pdate;
      comments = pcomments;

      theIngredients = new ArrayList();
      theSymptoms = new ArrayList();
      
      theIngredients = theDataBase.gatherIngredients(entryId);
      theSymptoms = theDataBase.gatherSymptoms(entryId);
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
