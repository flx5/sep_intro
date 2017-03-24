package de.unipassau.prassefe.sepintro.model;

import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;

/**
 * Login bean.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
@ManagedBean
@RequestScoped
public class Login {

	@ManagedProperty(value = "#{appConfig}")
	private AbstractConfig config;

	/**
	 * The username.
	 */
	private String userName;

	/**
	 * The password.
	 */
	private String password;

	@ManagedProperty(value = "#{userSession}")
	private UserSession userSession;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

        /**
         * Set user session.
         * @param userSession the session to set.
         */
	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

        /**
         * Set active config.
         * @param config The new config.
         */
	public void setConfig(AbstractConfig config) {
		this.config = config;
	}

        /**
         * Try to login the user.
         * @return Redirect on success.
         */
	public String login() {
		Optional<User> user;

		try (UserRepository repo = config.getRepository(UserRepository.class)) {
			user = repo.getByUserName(this.getUserName());
		}

		boolean loggedIn = user.map(x -> x.verifyPassword(this.getPassword())).orElse(false);

		if (loggedIn) {
			user.ifPresent(this.userSession::setUser);
			return "profile";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!"));
			return null;
		}
	}
}
