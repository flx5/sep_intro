package de.unipassau.prassefe.sepintro.model;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Page {
	HOME("Home", "/index.xhtml"),
	PROFILE("Profile", "/user/profile.xhtml", UserSession::isLoggedIn),
	LOGIN("Login", "/user/login.xhtml", x -> false);

	private final String title;
	private final String viewId;
	private final Predicate<UserSession> shown;

	private Page(String title, String viewId) {
		this(title, viewId, x -> true);
	}

	private Page(String title, String viewId, Predicate<UserSession> shown) {
		this.viewId = viewId;
		this.shown = shown;
		this.title = title;
	}

	public String getViewId() {
		return viewId;
	}

	public String getTitle() {
		return title;
	}

	public static Page[] getMenu(UserSession session) {
		return Arrays.stream(values()).filter(x -> x.shown.test(session)).toArray(Page[]::new);
	}
}
