package sep_intro.model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import sep_intro.model.repository.RepositoryFactory;
import sep_intro.model.repository.UserRepository;

@Named
@SessionScoped
public class UserSession implements Serializable {
	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	
	public boolean isLoggedIn() {
		return this.user != null;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String logout() {
		this.setUser(null);
		return "login";
	}
	
	public void saveUser() {
		try(UserRepository repo = RepositoryFactory.resolve(UserRepository.class)) {
			repo.update(this.user);
		}
	}
}
