/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeff
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetPeeps 
{
    static final String user = "root";
    static final String password = "cangetin";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cs313";
    
    public List<Person> getInfo() 
    {
        List<Person> peeps = new ArrayList<Person>(); 
        Connection conn = null;
        Statement stmt = null;
       
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, user, password);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM person";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                Person temp = new Person(rs.getInt("id"), rs.getString("firstN"), 
                rs.getString("lastN"), rs.getString("birthday"));
                peeps.add(temp);
                System.out.println(temp.getfName());
            }
            rs.close();
            stmt.close();
            conn.close();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(GetPeeps.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("gagga");
        } 
        catch (SQLException ex) {
            Logger.getLogger(GetPeeps.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return peeps;
    }
    
}

