package de.unipassau.prassefe.sepintro.model;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import de.unipassau.prassefe.sepintro.migration.MigrationRunner;
import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;

@ManagedBean(eager = true)
@ApplicationScoped
public class App {

	@ManagedProperty(value = "#{appConfig}")
	private AbstractConfig config;
	
	public void setConfig(AbstractConfig config) {
		this.config = config;
	}
	
	@PostConstruct
	public void init() {
		new MigrationRunner(config).migrateToLatest();
	}
}
