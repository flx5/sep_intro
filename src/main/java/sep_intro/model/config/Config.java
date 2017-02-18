package sep_intro.model.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.jboss.weld.resources.spi.ResourceLoadingException;

import sep_intro.model.repository.Repository;
import sep_intro.util.EnumUtil;

@ManagedBean(eager = true)
@ApplicationScoped
public class Config {
	private static final String CONFIG_FILE = "config.properties";
	
	private DatabaseConfig dbConfig;
	private Backend backend;
	private String repositoryPrefix;
	
	public Config() throws IOException {
		reload();
	}
	
	public DatabaseConfig getDbConfig() {
		return dbConfig;
	}
	
	public Backend getBackend() {
		return backend;
	}
	
	public void reload() throws IOException {
		Properties properties = new Properties();
		
		// TODO Should this be loaded from disk instead of Resources?
		try(InputStream stream = Config.class.getResourceAsStream(CONFIG_FILE)) {
			if(stream == null) {
				throw new ResourceLoadingException("Config file missing!");
			}
			
			properties.load(stream);
		}
		
		String backendName = properties.getProperty("backend", "fake");
		this.backend = EnumUtil.lookup(Backend.class, backendName);
		
		if(this.backend == null) {
			throw new InvalidConfiguration("Invalid backend " + backendName);
		}
		
		this.repositoryPrefix = backend.getRepositoryPrefix();
		
		this.dbConfig = new DatabaseConfig(properties);
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
