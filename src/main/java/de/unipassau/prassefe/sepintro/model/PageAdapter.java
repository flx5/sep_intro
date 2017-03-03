package de.unipassau.prassefe.sepintro.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import de.unipassau.prassefe.sepintro.jsf.JsfEnumAdapter;

@ManagedBean
@ApplicationScoped
public class PageAdapter extends JsfEnumAdapter<Page> {

	public PageAdapter() {
		super(Page.class);
	}

}
