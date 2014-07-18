/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeff
 */
public class BarcodeConnection {

    private String barCode;
    private String description;

    public BarcodeConnection(String pBarCode, String pDiscription) {
        barCode = pBarCode;
        description = pDiscription;
    }

    public BarcodeConnection() {
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() 
    {
        return "{"+barCode+" ["+description +"]}";
    }
}
