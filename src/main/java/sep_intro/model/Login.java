package sep_intro.model;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import sep_intro.model.repository.RepositoryFactory;
import sep_intro.model.repository.UserRepository;

@Named
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

	@Inject
	private UserSession userSession;

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
		User user;
		
		try(UserRepository repo = RepositoryFactory.resolve(UserRepository.class)) {
			user = repo.getByUserName(this.getUserName());
		}	
		
		if(user != null &&
			user.verifyPassword(this.getPassword())) {
			this.userSession.setUser(user);
			return "profile";
		}
		
		FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Invalid Login!",
                "Please Try Again!"));
		return null;
	}
}
