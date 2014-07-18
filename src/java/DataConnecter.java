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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeff
 */

public class DataConnecter {
    //This should be changed before we push this into usable locations
    private String apID = "appId=1432414160367156";
    private String apKey = "appKey=11726299147806c7c66873a7d25bd55b";
    private String connUrl = "http://www.klappo.com:8080/jesse/server/"
    +"suitability_inspection/";
    private List<FoodInfo> infoFoods; 
    
    public List<FoodInfo> getListOfFoods() {
        return infoFoods;
    }
    
    public DataConnecter()
    {
    }

    public Map<String, Object> requestBarCode(String name)
    {
        URL url;
        try {
            url = new URL(connUrl+"getProductFromIngredientName?"+apID+apKey+"="+name);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DataConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> options = mapper.readValue(url, Map.class); 
        
        for(String k : options.keySet())
        {
            System.out.println(k + "\n");
        }
        return options;
    }
    
    public Map<String, Object> requestInfo(String barCode)
    {
        URL url;
        try {
            url = new URL(connUrl+"getProductInfo?"+apID+apKey+"="+barCode);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DataConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(url, Map.class); 
        
        for(String k : result.keySet())
        {
            System.out.println(k + "\n");
        }
        return result;
    }
}
