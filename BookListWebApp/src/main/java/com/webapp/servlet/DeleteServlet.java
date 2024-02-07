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

@WebServlet("/deleteEntry")
public class DeleteServlet extends HttpServlet {

	private static final String query = "DELETE FROM books  WHERE id=?";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		
		// Getting book ID
        int id = Integer.parseInt(req.getParameter("id"));
        
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection con = DriverManager.getConnection("jdbc:mysql:///library", "root", "root");
					PreparedStatement ps = con.prepareStatement(query);) {
				
				ps.setInt(1, id);
				
				int count = ps.executeUpdate();
                if (count == 1) {
                    // Display success popup and redirect to bookList or homepage
                    pw.println("<script>" +
                            "var confirmMsg = confirm('Book Deleted Successfully! Do you want to view the book list?');" +
                            "if (confirmMsg) { window.location.href='bookList'; } else { window.location.href='Homepage.html'; }" +
                            "</script>");
                } else {
                    // Display error popup
                    pw.println("<script>alert('ERROR: Book Cannot Be Deleted!');</script>");
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
}
