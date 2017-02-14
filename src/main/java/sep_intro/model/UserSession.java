package sep_intro.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import sep_intro.model.repository.FakeUserRepository;
import sep_intro.model.repository.UserRepository;

@ManagedBean(eager = true)
@SessionScoped
public class UserSession {
	private User user;
	
	// TODO Should be injected
	private UserRepository userRepository = FakeUserRepository.getInstance();

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
		this.userRepository.update(this.user);
	}
}
