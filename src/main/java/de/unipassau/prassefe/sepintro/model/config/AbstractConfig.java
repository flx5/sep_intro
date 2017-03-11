package de.unipassau.prassefe.sepintro.model.config;

import java.util.Optional;

import javax.sql.DataSource;

import de.unipassau.prassefe.sepintro.model.repository.Repository;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryNotFoundException;
import de.unipassau.prassefe.sepintro.util.EnumUtil;

public abstract class AbstractConfig {

	private Backend backend;
	private DataSource dataSource;

	public AbstractConfig() {
		this(true);
	}
	
	public AbstractConfig(boolean autoload) {
		if(autoload) {
			reload();
		}
	}

	protected void setBackend(String backendName) {
		Optional<Backend> newBackend = EnumUtil.lookup(Backend.class, backendName);
		
		if(!newBackend.isPresent()) {
			throw new InvalidConfiguration("Invalid backend " + backendName);
		}
		
		setBackend(newBackend.get());
	}
	
	protected void setBackend(Backend newBackend) {
		this.backend = newBackend;
	}
	
	public Backend getBackend() {
		return backend;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	
	protected void setDataSource(DataSource value) {
		this.dataSource = value;
	}

	@SuppressWarnings("unchecked")
	public <T extends Repository<?, ?>> T getRepository(Class<T> interfaceClass) {
		try {
			T repository = (T)Class.forName(this.backend.getRepositoryPrefix() + interfaceClass.getSimpleName()).newInstance();
			repository.setConfig(this);
			return repository;
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RepositoryNotFoundException(e);
		}
	}

	public abstract void reload();
}