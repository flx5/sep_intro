package de.unipassau.prassefe.sepintro.jsf;

import java.util.Map;
import java.util.Set;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

/**
 * Redirect handler forces redirects to be sent to the browser.
 * 
 * Without this the default would be to simply present
 * the browser with the view redirected to.
 * 
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class RedirectNavigationHandler extends ConfigurableNavigationHandler {

    private NavigationHandler parent;

    /**
     * Create a new redirection handler.
     * @param parent The parent handler, if any.
     */
    public RedirectNavigationHandler(NavigationHandler parent) {
        this.parent = parent;
    }

    @Override
    public void handleNavigation(final FacesContext context, final String from, final String outcome) {
        String actualUrl = outcome;

        if (outcome != null && !outcome.endsWith("?faces-redirect=true")) {
            actualUrl += "?faces-redirect=true";
        }

        parent.handleNavigation(context, from, actualUrl);
    }

    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
        if (parent instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) parent).getNavigationCase(context, fromAction, outcome);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
        if (parent instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) parent).getNavigationCases();
        } else {
            return null;
        }
    }
}
