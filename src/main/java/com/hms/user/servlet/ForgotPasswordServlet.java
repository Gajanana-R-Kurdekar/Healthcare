package com.hms.user.servlet;

import java.io.IOException;

import com.hms.dao.UserDAO;
import com.hms.db.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        UserDAO dao = new UserDAO(DBConnection.getConn());

        boolean f = dao.resetPasswordByEmail(email, password);

        HttpSession session = req.getSession();

        if (f) {
            session.setAttribute("successMsg", "Password changed successfully");
            resp.sendRedirect("user_login.jsp");
        } else {
            session.setAttribute("errorMsg", "Invalid email");
            resp.sendRedirect("forgot-password.jsp");
        }
    }
}