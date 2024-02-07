package com.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/editScreen")
public class EditScreenServlet extends HttpServlet {
    private static final String query = "SELECT  bookname, bookedition, bookprice FROM books WHERE id=?";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        // Getting book ID
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql:///library", "root", "root");
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    generateEditForm(pw, rs, id);
                } else {
                    pw.println("<h1>Book not found!</h1>");
                }

            } catch (SQLException se) {
                pw.println("<h1>Error: " + se.getMessage() + "</h1>");
                se.printStackTrace();
            }
        } catch (ClassNotFoundException cnf) {
            pw.println("<h1>Error: " + cnf.getMessage() + "</h1>");
            cnf.printStackTrace();
        }
    }


    private void generateEditForm(PrintWriter pw, ResultSet rs, int id) throws SQLException {
        pw.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Edit Book</title>\n" +
                "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class='container mt-5'>\n" +
                "<div class='row justify-content-center'>\n" +
                "<div class='col-md-6'>\n" +
                "<div class='card'>\n" +
                "<div class='card-body'>\n" +
                "<h2 class='card-title text-center mb-4'>Edit Book</h2>\n" +
                "<form action='editUrl?id=" + id + "' method='post'>\n" +
                "<table class='table'>\n" +
                "<tr><td>Book Name:</td><td><input type='text' class='form-control' name='bookName' value='" + rs.getString(1) + "'></td></tr>\n" +
                "<tr><td>Book Edition:</td><td><input type='text' class='form-control' name='bookEdition' value='" + rs.getString(2) + "'></td></tr>\n" +
                "<tr><td>Book Price:</td><td><input type='text' class='form-control' name='bookPrice' value='" + rs.getString(3) + "'></td></tr>\n" +
                "<tr><td colspan='2'>\n" +
                "<div class='text-center'>\n" +
                "<input type='submit' class='btn btn-primary mr-2' value='Save Edit'>\n" +
                "<input type='reset' class='btn btn-secondary mr-2' value='Reset Edit'>\n" +
                "<a href='bookList' class='btn btn-danger'>Cancel Edit</a>\n" +
                "</div>\n" +
                "</td></tr>\n" +
                "</table>\n" +
                "</form>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
