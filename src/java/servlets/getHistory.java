/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.owlike.genson.Genson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javadatabasev0.Entry;
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
@WebServlet(name = "getHistory", urlPatterns = {"/api/getHistory"})
public class getHistory extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = (String) request.getSession().getAttribute("id");
            if (id == null)
            {
                //response.getWriter().write("failed to have a log in so random test id = 1");
                //id = "1";
            }
            Map<String, List<Entry>> info = new HashMap();
            User me = new User(id);
            List<Entry> history = me.getTheEntries();
            System.out.println(history);
            info.put("history", history);
            Genson jsonConverter = new Genson();
            String json = jsonConverter.serialize(info);
            response.getWriter().write(json);
        } 
        catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
            response.getWriter().write(ex.toString());
            System.out.println(getHistory.class.getName());
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
    }// </editor-fold>

}
