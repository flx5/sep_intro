package de.unipassau.prassefe.sepintro.jsf;

import java.util.Map;
import java.util.Set;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

public class RedirectNavigationHandler extends ConfigurableNavigationHandler {

	private NavigationHandler parent;

    public RedirectNavigationHandler(NavigationHandler parent) {
        this.parent = parent;
    }
	
    @Override
	public void handleNavigation(FacesContext context, String from, String outcome) {
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
