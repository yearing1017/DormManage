package com.lero.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BlankServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		if("admin".equals((String)currentUserType)) {
			request.setAttribute("mainPage", "admin/blank.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
		} else if("dormManager".equals((String)currentUserType)) {
			request.setAttribute("mainPage", "dormManager/blank.jsp");
			request.getRequestDispatcher("mainManager.jsp").forward(request, response);
		} else if("student".equals((String)currentUserType)) {
			request.setAttribute("mainPage", "student/blank.jsp");
			request.getRequestDispatcher("mainStudent.jsp").forward(request, response);
		}
	}

	
}
