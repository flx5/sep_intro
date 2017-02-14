package sep_intro.jsf;

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

import sep_intro.model.UserSession;

public class AuthorizationFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		
		String reqURI = req.getRequestURI();
		UserSession user = (session != null) ? (UserSession) session.getAttribute("userSession") : null;

		
		if (reqURI.endsWith("/login.xhtml")
				|| user == null || user.isLoggedIn()) {
			chain.doFilter(request, response);
		} else {
			res.sendRedirect(req.getContextPath() + "/login.xhtml"); 
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
