package sep_intro.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class JsfUtil {
	private JsfUtil() {
	}

	public static HttpSession getSession() {
		return (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(false);
	}
}
