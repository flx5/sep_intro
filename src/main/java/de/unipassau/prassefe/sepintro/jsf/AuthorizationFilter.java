package de.unipassau.prassefe.sepintro.jsf;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.unipassau.prassefe.sepintro.model.UserSession;

public class AuthorizationFilter implements Filter {
// TODO Refer to pages enum
	private static final String LOGIN_URL = "/login.xhtml";
	private static final String PROFILE_URL = "/profile.xhtml";
	
	@Override
	public void destroy() {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		
		UserSession user = (session != null) ? (UserSession) session.getAttribute("userSession") : null;

		boolean loggedIn = user != null && user.isLoggedIn();
		
		if (!isOnLogin(req) && !loggedIn) {
			redirect(req, res, LOGIN_URL);
		} else if (loggedIn && isOnLogin(req)) {
			redirect(req, res, PROFILE_URL);
		} else {
			chain.doFilter(request, response);
		}
	}
	
	private boolean isOnLogin(HttpServletRequest req) {
		String reqURI = req.getRequestURI();
		return reqURI.endsWith(LOGIN_URL);
	}

	private void redirect(HttpServletRequest req, HttpServletResponse res, String url) throws IOException {
		res.sendRedirect(req.getContextPath() + url);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

}
