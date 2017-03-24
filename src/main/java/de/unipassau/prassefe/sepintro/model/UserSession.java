package de.unipassau.prassefe.sepintro.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;

/**
 * A session of a user.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
@ManagedBean(eager = true)
@SessionScoped
public class UserSession {
	private User user;
	
	@ManagedProperty(value = "#{appConfig}")
	private AbstractConfig config;
	
        /**
         * Is this user logged in?
         * @return true if logged in.
         */
	public boolean isLoggedIn() {
		return this.user != null;
	}
	
        /**
         * Get the logged in user, if any.
         * @return 
         */
	public User getUser() {
		return user;
	}

        /**
         * Set the logged in user.
         * @param user The user.
         */
	public void setUser(User user) {
		this.user = user;
	}
	
        /**
         * Set the currently used config.
         * @param config The config.
         */
	public void setConfig(AbstractConfig config) {
		this.config = config;
	}

        /**
         * Logout the user.
         * @return Redirect to login page.
         */
	public String logout() {
		this.setUser(null);
		return "login";
	}
	
        /**
         * Save current user.
         */
	public void saveUser() {
		try(UserRepository repo = config.getRepository(UserRepository.class)) {
			repo.update(this.user);
		}
	}
	
        /**
         * Get the navigation for this user.
         * @return The navigation.
         */
	public Page[] getMenu() {
		return Page.getMenu(this);
	}
}
