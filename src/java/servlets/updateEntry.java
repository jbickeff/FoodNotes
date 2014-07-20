/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import com.owlike.genson.Genson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javadatabasev0.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gage
 */
@WebServlet(name = "updateEntry", urlPatterns = {"/api/updateEntry"})
public class updateEntry extends HttpServlet {



    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Genson jsonConverter = new Genson();
        Map<String, Object> maped;
        maped = jsonConverter.deserialize(new InputStreamReader(request.getInputStream()), Map.class);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        response.getWriter().write(maped.toString());
        List<Map<String, String>>ingMap = (List<Map<String, String>>) maped.get("ingredients");
        List<String> ing = new ArrayList<String>();
        for(Map m : ingMap)
        {
            ing.add((String) m.get("name"));
        }
        ingMap = (List<Map<String, String>>) maped.get("symptoms");
        System.out.println("ing = "+ing);
        List<String> syp = new ArrayList<String>();
        for(Map m : ingMap)
        {
            syp.add((String) m.get("name"));
        }
        String disc = (String) maped.get("desc");
        int entryId = (int) maped.get("enrtyId");
        System.out.println("syp = " +syp);
        String id = (String) request.getSession().getAttribute("id");
        if (id == null)
        {
            id = "1";
        }
        try {
            User me = new User(id);
            String time = dateFormat.format(cal.getTime());
            me.upDateEntry(ing, syp, disc, entryId);
        } 
        catch (Exception ex) {
            System.out.println(newEntry.class.getName());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
