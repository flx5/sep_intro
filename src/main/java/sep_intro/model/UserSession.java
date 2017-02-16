package sep_intro.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import sep_intro.model.repository.RepositoryFactory;
import sep_intro.model.repository.UserRepository;

@ManagedBean(eager = true)
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
