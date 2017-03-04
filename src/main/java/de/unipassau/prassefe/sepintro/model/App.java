package de.unipassau.prassefe.sepintro.model;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import de.unipassau.prassefe.sepintro.model.config.Config;
import de.unipassau.prassefe.sepintro.model.migrations.MigrationIndex;

@ManagedBean(eager = true)
@ApplicationScoped
public class App {

	@PostConstruct
	public void init() {
		new MigrationIndex(new Config()).migrateToLatest();
	}
}
