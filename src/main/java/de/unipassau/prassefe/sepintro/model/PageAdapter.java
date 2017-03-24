package de.unipassau.prassefe.sepintro.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import de.unipassau.prassefe.sepintro.jsf.JsfEnumAdapter;

/**
 * JSF {@link Page} enumeration adapter.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
@ManagedBean
@ApplicationScoped
public class PageAdapter extends JsfEnumAdapter<Page> {

    /**
     * Create new page adapter.
     */
    public PageAdapter() {
        super(Page.class);
    }

}
