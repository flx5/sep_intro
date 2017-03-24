package de.unipassau.prassefe.sepintro.model;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Enumeration of existing pages. Should be used instead of hardcoding
 * references.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public enum Page {
    /**
     * Index.
     */
    HOME("Home", "/index.xhtml"),
    /**
     * Profile.
     */
    PROFILE("Profile", "/user/profile.xhtml", UserSession::isLoggedIn),
    
    /**
     * Login.
     */
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

    /**
     * Get the view id.
     * @return The view id.
     */
    public String getViewId() {
        return viewId;
    }

    /**
     * Get page title.
     * @return page title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get pages shown in menu.
     * @param session Active user session.
     * @return shown pages.
     */
    public static Page[] getMenu(UserSession session) {
        return Arrays.stream(values()).filter(x -> x.shown.test(session)).toArray(Page[]::new);
    }
}
