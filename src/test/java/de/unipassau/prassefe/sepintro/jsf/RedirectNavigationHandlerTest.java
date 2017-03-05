/**
 * 
 */
package de.unipassau.prassefe.sepintro.jsf;

import static org.junit.Assert.*;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author felix
 *
 */
public class RedirectNavigationHandlerTest {
	private ConfigurableNavigationHandler parent;
	private ConfigurableNavigationHandler handler;
	private ConfigurableNavigationHandler otherHandler;
	
	@Before
	public void setUp() {
		parent = mock(ConfigurableNavigationHandler.class);
		handler = new RedirectNavigationHandler(parent);
		otherHandler = new RedirectNavigationHandler(mock(NavigationHandler.class));
	}

	@After
	public void tearDown() {
	}
	
	/**
	 * Test method for {@link de.unipassau.prassefe.sepintro.jsf.RedirectNavigationHandler#handleNavigation(javax.faces.context.FacesContext, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testHandleNavigation() {
		handler.handleNavigation(null, null, "to");
		verify(parent).handleNavigation(null, null, "to?faces-redirect=true");
	}

	/**
	 * Test method for {@link de.unipassau.prassefe.sepintro.jsf.RedirectNavigationHandler#getNavigationCase(javax.faces.context.FacesContext, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetNavigationCase() {
		handler.getNavigationCase(null, null, null);
		verify(parent).getNavigationCase(null, null, null);
	}

	/**
	 * Test method for {@link de.unipassau.prassefe.sepintro.jsf.RedirectNavigationHandler#getNavigationCases()}.
	 */
	@Test
	public final void testGetNavigationCases() {
		handler.getNavigationCases();
		verify(parent).getNavigationCases();
	}
	
	@Test
	public final void testGetNavigationCasesOther() {
		assertNull(otherHandler.getNavigationCases());
	}

	@Test
	public final void testGetNavigationCaseOther() {
		assertNull(otherHandler.getNavigationCase(null, null, null));
	}
}
