package de.unipassau.prassefe.sepintro.jsf;

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

import de.unipassau.prassefe.sepintro.model.Page;
import de.unipassau.prassefe.sepintro.model.UserSession;

/**
 * Authorization filter for redirecting users based upon login status.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public final class AuthorizationFilter implements Filter {

    /**
     * {@inheritDoc }
     */
    @Override
    public void destroy() {
        // nothing to do
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        UserSession user = null;

        if (session != null) {
            user = (UserSession) session.getAttribute("userSession");
        }

        boolean loggedIn = user != null && user.isLoggedIn();

        if (!isOnLogin(req)
                && !loggedIn) {
            redirect(req, res, Page.LOGIN.getViewId());
        } else if (loggedIn
                && isOnLogin(req)) {
            redirect(req, res, Page.PROFILE.getViewId());
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isOnLogin(final HttpServletRequest req) {
        String reqURI = req.getRequestURI();
        return reqURI.endsWith(Page.LOGIN.getViewId());
    }

    private void redirect(final HttpServletRequest req,
            final HttpServletResponse res,
            final String url) throws IOException {
        res.sendRedirect(req.getContextPath() + url);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

}
