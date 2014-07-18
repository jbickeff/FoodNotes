/*
 * This class deals with connecting to Food Essentials api and returns the 
 * list of food info collected from the api to provide to the user.
 * Currently this class will only keep track of one result, which will be the 
 * most recent search.
 */

/**
 *
 * @author Jeff
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


/**
 *
 * @author Jeff
 */

public class DataConnecter {
    //This should be changed before we push this into usable locations
    private String apID = "appId=8de0c306";
    private String apKey = "appKey=a3f1f94cec025bf74360f05b50fd6cb8";
    private String connUrl = "http://www.klappo.com:8080/jesse/server/"
    +"suitability_inspection/";
    private List<FoodInfo> infoFoods; 
    
    public List<FoodInfo> getListOfFoods() {
        return infoFoods;
    }
    
    public DataConnecter()
    {
    }

    public List<BarcodeConnection> requestBarCode(String name)
    {
        URL url;
        ObjectMapper mapper = new ObjectMapper();
        List<BarcodeConnection> options;
        try {
            url = new URL(connUrl+"getProductsFromIngredientName?"+apID+"&"+apKey+"&name="+name);
            System.out.println(url);
            options = mapper.readValue(url, new TypeReference<List<BarcodeConnection>>(){});
            System.out.println("Spot 2");
        } catch (Exception ex) {
            System.out.println("Exception throwen = "+ ex);
            options = null;
        }
       System.out.println("Spot3");
      
        return options;
    }
    
    
    public List<FoodInfo> requestInfo(String barCode)
    {
        ObjectMapper mapper = new ObjectMapper();
        List<FoodInfo> result = null;
        URL url;
        try {
            url = new URL(connUrl+"getProductInfo?"+apID+"&"+apKey+"&barcode="+barCode);
            
            result = mapper.readValue(url, new TypeReference<List<FoodInfo>>(){}); 
        
             return result;
        } 
        catch (MalformedURLException ex) {
            Logger.getLogger(DataConnecter.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        } catch (IOException ex) {
            Logger.getLogger(DataConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }
               
        return result;
    }

    
}
