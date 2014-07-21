/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.owlike.genson.Genson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "newEntry", urlPatterns = {"/api/newEntry"})
public class newEntry extends HttpServlet {

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
        //response.getWriter().write(maped.toString());
        List<Map<String, String>> ingMap = (List<Map<String, String>>) maped.get("ingredients");
        List<String> ing = new ArrayList<String>();
        for (Map m : ingMap) {
            if (m.get("name").equals("") == false) {
                ing.add((String) m.get("name"));
            }
        }
        ingMap = (List<Map<String, String>>) maped.get("symptoms");
        System.out.println(ing);
        List<String> syp = new ArrayList<>();
        for (Map m : ingMap) {
            if (m.get("name").equals("") == false) {
                syp.add((String) m.get("name"));
            }
        }
        String disc = (String) maped.get("desc");
        System.out.println(ingMap);
        String id = (String) request.getSession().getAttribute("id");
        if (id == null) {
            id = "1";
        }
        try {
            User me = new User(id);
            String time = dateFormat.format(cal.getTime());
            me.addEntry(ing, syp, time, disc);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            // stack trace as a string
            response.getWriter().write(sw.toString());
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
