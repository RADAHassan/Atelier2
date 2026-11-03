package com.atelier.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Base commune : tout passe par doGet, et les vues (JSP) sont cachées dans /WEB-INF/jsp (MVC 2). */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }

    protected void forward(HttpServletRequest req, HttpServletResponse resp, String vue)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/" + vue).forward(req, resp);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String chemin) throws IOException {
        resp.sendRedirect(req.getContextPath() + chemin);
    }

    protected static Long toLong(String s) {
        try { return (s == null || s.trim().isEmpty()) ? null : Long.valueOf(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    protected static int toInt(String s, int defaut) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return defaut; }
    }

    protected static double toDouble(String s, double defaut) {
        try { return Double.parseDouble(s.trim().replace(',', '.')); } catch (Exception e) { return defaut; }
    }
}
