package sep_intro;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import sep_intro.model.repository.FakeUserRepository;
import sep_intro.model.repository.UserRepository;

@ManagedBean
@RequestScoped
public class Login {
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

	// TODO Should be injected
	private UserRepository userRepository = new FakeUserRepository();
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}
	
	public String login() {
		User user = userRepository
				.getByUserName(this.getUserName());
		
		if(user != null &&
			user.verifyPassword(this.getPassword())) {
			this.userSession.setUser(user);
			return "profile.xhtml?faces-redirect=true";
		}
		
		// TODO message!
		return null;
	}
}
