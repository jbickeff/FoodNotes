/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadatabasev0;

/**
 *
 * @author Makz
 */
public class Symptom {
   private String id;
   private String name;
   
   Symptom(String pName, String pId)
   {
      id = pId;
      name = pName;
   }

   /**
    * @return the id
    */
   public String getId() {
      return id;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }
}
