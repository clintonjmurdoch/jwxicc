package com.jwxicc.cricket.servlet;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwxicc.cricket.mail.MailSender;

/**
 * Servlet implementation class SuggestionServlet
 */
@WebServlet(urlPatterns = "/suggestion-servlet")
public class SuggestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SuggestionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String suggestionText = request.getParameter("suggestion");
		try {
			new MailSender().sendEmail(email, "support@jwxicc.com", "Suggestion from " + name,
					suggestionText);
			response.getWriter().append(
					"Successfully submitted your suggestion, we will try to respond to it soon.");
		} catch (MessagingException e) {
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			response.getWriter().append("Failed to submit suggestion: " + e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Servlet is deployed here - get");
	}

}
