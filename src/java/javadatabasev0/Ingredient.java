/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadatabasev0;

/**
 *
 * @author Makz
 */
public class Ingredient {
   private String ingredient;
   private String id;
   
   Ingredient(String pIngredient, String pId)
   {
      ingredient = pIngredient;
      id = pId;
   }

   /**
    * @return the ingredient
    */
   public String getIngredient() {
      return ingredient;
   }

   /**
    * @return the id
    */
   public String getId() {
      return id;
   }
   
}
