package com.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class SaveServlet extends HttpServlet {

    private static final String query = "INSERT INTO books(bookname,bookedition,bookprice) VALUES(?,?,?)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookEdition");
        float bookPrice = Float.parseFloat(req.getParameter("bookPrice"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //loading dynamically the JDBC drivers
            try (Connection con = DriverManager.getConnection("jdbc:mysql:///library", "root", "root");
                 PreparedStatement ps = con.prepareStatement(query);) {
                ps.setString(1, bookName);
                ps.setString(2, bookEdition);
                ps.setFloat(3, bookPrice);

                int count = ps.executeUpdate();
                if (count == 1) {
                    // Display success popup and redirect to bookList or homepage
                    pw.println("<script>" +
                            "var confirmMsg = confirm('Book Registered Successfully! Do you want to view the book list?');" +
                            "if (confirmMsg) { window.location.href='bookList'; } else { window.location.href='Homepage.html'; }" +
                            "</script>");
                } else {
                    // Display error popup
                    pw.println("<script>alert('Book Not Registered!');</script>");
                }
            } catch (SQLException se) {
                se.printStackTrace();
                // Display error popup with message and redirect to homepage
                pw.println("<script>alert('Error: " + se.getMessage() + "'); window.location.href='Homepage.html';</script>");
            } catch (Exception e) {
                e.printStackTrace();
                // Display error popup with generic message and redirect to homepage
                pw.println("<script>alert('An error occurred.'); window.location.href='Homepage.html';</script>");
            }
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            // Display error popup with message and redirect to homepage
            pw.println("<script>alert('Error: JDBC driver not found.'); window.location.href='Homepage.html';</script>");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
