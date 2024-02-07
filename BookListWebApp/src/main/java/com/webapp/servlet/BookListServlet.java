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

@WebServlet("/bookList")
public class BookListServlet extends HttpServlet {

    private static final String query = "SELECT id, bookname, bookedition, bookprice FROM books";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql:///library", "root", "root");
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery();) {

                generateTable(pw, rs);

            } catch (SQLException se) {
                pw.println("<h1>Error: " + se.getMessage() + "</h1>");
                se.printStackTrace();
            }
        } catch (ClassNotFoundException cnf) {
            pw.println("<h1>Error: " + cnf.getMessage() + "</h1>");
            cnf.printStackTrace();
        }
    }

    private void generateTable(PrintWriter pw, ResultSet rs) throws SQLException {
        pw.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Book List</title>\n" +
                "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">\n" +
                "<style>\n" +
                ".small-input { max-width: 800px; }\n" +
                ".form-group.row { margin-bottom: 10px; }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container d-flex justify-content-center align-items-center\" style=\"height: 100vh;\">\n" +
                "<div class=\"card p-4\" style=\"width: 800px;\">\n" +
                "<h1 class=\"text-center mb-4\">Book List</h1>\n" +
                "<table class=\"table\">\n" +
                "<thead class=\"thead-light\">\n" +
                "<tr><th>Book ID</th><th>Book Name</th><th>Book Edition</th><th>Book Price</th><th>Edit Entry</th><th>Delete Entry</th></tr>\n" +
                "</thead>\n" +
                "<tbody>");

        while (rs.next()) {
            pw.println("<tr>" +
                    "<td>" + rs.getInt(1) + "</td>" +
                    "<td>" + rs.getString(2) + "</td>" +
                    "<td>" + rs.getString(3) + "</td>" +
                    "<td>" + rs.getFloat(4) + "$</td>" +
                    "<td><a href='editScreen?id=" + rs.getInt(1) + "'>Edit</a></td>" +
                    "<td><a href='deleteEntry?id=" + rs.getInt(1) + "' style='color: red;'>Delete</a></td>" +
                    "</tr>");
        }

        pw.println("</tbody>\n" +
                "</table>\n" +
                "<a href='Homepage.html' class='btn btn-secondary'>Go to Homepage</a>\n" +
                "</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");
    }


}
