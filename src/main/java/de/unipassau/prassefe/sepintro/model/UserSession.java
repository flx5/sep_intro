package de.unipassau.prassefe.sepintro.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.unipassau.prassefe.sepintro.model.config.Config;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;

@ManagedBean(eager = true)
@SessionScoped
public class UserSession implements Serializable {
	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	
	@ManagedProperty(value = "#{appConfig}")
	private Config config;
	
	public boolean isLoggedIn() {
		return this.user != null;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setConfig(Config config) {
		this.config = config;
	}

	public String logout() {
		this.setUser(null);
		return "login";
	}
	
	public void saveUser() {
		try(UserRepository repo = config.getRepository(UserRepository.class)) {
			repo.update(this.user);
		}
	}
	
	public Page[] getMenu() {
		return Page.getMenu(this);
	}
}
