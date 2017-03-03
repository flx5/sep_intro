package de.unipassau.prassefe.sepintro.model;

import java.util.Arrays;

public enum Page {
	HOME("Home", "/index.xhtml"),
	PROFILE("Profile", "/user/profile.xhtml"),
	LOGIN("Login", "/user/login.xhtml", false);

	private final String title;
	private final String viewId;
	private final boolean showInMenu;

	private Page(String title, String viewId) {
		this(title, viewId, true);
	}

	private Page(String title, String viewId, boolean showInMenu) {
		this.viewId = viewId;
		this.showInMenu = showInMenu;
		this.title = title;
	}

	public String getViewId() {
		return viewId;
	}

	public String getTitle() {
		return title;
	}

	public static Page[] getMenu() {
		return Arrays.stream(values()).filter(x -> x.showInMenu).toArray(Page[]::new);
	}
}
