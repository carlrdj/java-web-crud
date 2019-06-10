/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.google.gson.Gson;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import dto.Message;
import dto.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author carlr
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       doGet(request, response);
    }

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
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        sb.delete(0, sb.length());
        String action = request.getPathInfo();
        UserDao userDao = new UserDaoImpl();
        if (action == null) {
            sb.append(gson.toJson(userDao.getUsers()));
        } else {
            action = action.substring(1);
            sb.append(gson.toJson(userDao.getUser(Integer.parseInt(action))));
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(sb.toString());
            out.flush();
        }
    }

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
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        sb.delete(0, sb.length());
        User user = gson.fromJson(request.getReader(), User.class);
        UserDao userDao = new UserDaoImpl();
        Message message = new Message();
        if (userDao.createUser(user)) {
            message.setStatus(true);
            message.setMessage("Se registro correctamente.");
        } else {
            message.setStatus(false);
            message.setMessage("No se pudo registrar.");
        }
        sb.append(gson.toJson(message));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(sb.toString());
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        sb.delete(0, sb.length());
        User user = gson.fromJson(req.getReader(), User.class);
        UserDao userDao = new UserDaoImpl();
        Message message = new Message();
        if (userDao.updateUser(user)) {
            message.setStatus(true);
            message.setMessage("Se actualizo correctamente.");
        } else {
            message.setStatus(false);
            message.setMessage("No se pudo actualizar.");
        }
        sb.append(gson.toJson(message));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print(sb.toString());
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        sb.delete(0, sb.length());
        User user = gson.fromJson(req.getReader(), User.class);
        ArrayList<Integer> ids = new ArrayList<>();
        for (Object id : req.getPathInfo().substring(1).split(",")) {
            ids.add(Integer.parseInt(id.toString()));
        }
        UserDao userDao = new UserDaoImpl();
        Message message = new Message();
        if (userDao.removeUsers(ids)) {
            message.setStatus(true);
            message.setMessage("Se eliminó correctamente.");
        } else {
            message.setStatus(false);
            message.setMessage("No se pudo eliminar.");
        }
        sb.append(gson.toJson(message));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print(sb.toString());
            out.flush();
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
