package sep_intro.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import sep_intro.jsf.JsfUtil;

@ManagedBean
@SessionScoped
public class UserSession {
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String logout() {
		JsfUtil.getSession().invalidate();
		return "login";
	}
}
