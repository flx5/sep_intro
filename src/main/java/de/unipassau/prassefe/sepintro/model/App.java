package de.unipassau.prassefe.sepintro.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(eager = true)
@ApplicationScoped
public class App {
	private final Page[] menu;
	
	public App() {
		this.menu = Page.getMenu();
	}

	public Page[] getMenu() {
		return menu;
	}
}
