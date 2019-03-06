package com.lero.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter{

	public void destroy() {
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		HttpSession session = request.getSession();
		Object object = session.getAttribute("currentUser");
		String path = request.getServletPath();
		if(object == null&&path.indexOf("login")<0&&path.indexOf("bootstrap")<0&&path.indexOf("images")<0) {
			response.sendRedirect("login.jsp");
		}else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
