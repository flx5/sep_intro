package sep_intro.model.config;

import java.io.IOException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sun.faces.config.ConfigurationException;

import sep_intro.model.repository.Repository;
import sep_intro.util.EnumUtil;

@ManagedBean(eager = true, name = "appConfig")
@ApplicationScoped
public class Config {
	private Backend backend;
	private String repositoryPrefix;
	
	private DataSource dataSource;
	
	public Config() throws IOException {
		reload();
	}
	
	public Backend getBackend() {
		return backend;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void reload() throws IOException {
		ExternalContext context = FacesContext
			    .getCurrentInstance().getExternalContext();
		
		String backendName = context.getInitParameter("sep.BACKEND");

		this.backend = EnumUtil.lookup(Backend.class, backendName);
		
		if(this.backend == null) {
			throw new InvalidConfiguration("Invalid backend " + backendName);
		}
		
		this.repositoryPrefix = backend.getRepositoryPrefix();
		
		try {
			Context ctx = new InitialContext();
			this.dataSource = (DataSource)ctx.lookup("java:comp/env/jdbc/sep");
		} catch (NamingException e) {
			throw new ConfigurationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Repository<?, ?>> T getRepository(Class<T> interfaceClass) {
		try {
			T repository = (T)Class.forName(repositoryPrefix + interfaceClass.getSimpleName()).newInstance();
			repository.setConfig(this);
			return repository;
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
