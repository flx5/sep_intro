package de.unipassau.prassefe.sepintro.model.config;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ManagedBean(eager = true, name = "appConfig")
@ApplicationScoped
public class Config extends AbstractConfig {
	@Override
	public void reload() {
		ExternalContext context = FacesContext
			    .getCurrentInstance().getExternalContext();
		
		setBackend(context.getInitParameter("sep.BACKEND"));
		
		try {
			Context ctx = new InitialContext();
			setDataSource((DataSource)ctx.lookup("java:comp/env/jdbc/sep"));
		} catch (NamingException e) {
			throw new InvalidConfiguration(e);
		}
	}
}
