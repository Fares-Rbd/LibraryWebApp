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

@WebServlet("/editUrl")
public class EditServlet extends HttpServlet {
    private static final String query = "UPDATE books SET bookname=?, bookedition=?, bookprice=? WHERE id=?";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        // Getting book ID
        int id = Integer.parseInt(req.getParameter("id"));

        // Get the new values from the request
        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookEdition");
        float bookPrice = Float.parseFloat(req.getParameter("bookPrice"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql:///library", "root", "root");
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, bookName);
                ps.setString(2, bookEdition);
                ps.setFloat(3, bookPrice);
                ps.setInt(4, id);

                // Executing the update & verifying its status
                int count = ps.executeUpdate();
                if (count == 1) {
                    // Display success message and redirect
                    pw.println("<script>" +
                            "var confirmMsg = confirm('Book Updated Successfully! Do you want to view the book list?');" +
                            "if (confirmMsg) { window.location.href='bookList'; } else { window.location.href='Homepage.html'; }" +
                            "</script>");
                } else {
                    // Display error message
                    pw.println("<script>alert('Book Not Updated!');</script>");
                }

            } catch (SQLException se) {
                pw.println("<h1>Error: " + se.getMessage() + "</h1>");
                se.printStackTrace();
            }
        } catch (ClassNotFoundException cnf) {
            pw.println("<h1>Error: " + cnf.getMessage() + "</h1>");
            cnf.printStackTrace();
        } finally {
            pw.println("</tbody>\n" +
                    "</table>\n" +
                    "<a href='Homepage.html' class='btn btn-secondary'>Go to Homepage</a>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>");
        }
    }

}
