package de.unipassau.prassefe.sepintro.jsf;

import static org.junit.Assert.*;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


public class LocalDateConverterTest {

	private LocalDateConverter converter;
	private static final Locale LOCALE = Locale.ROOT;
	private DateTimeFormatter formatter;
	private FacesContext context;

	@Before
	public void setUp() {
		this.converter = new LocalDateConverter();
		this.formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(LOCALE);
		this.context = mock(FacesContext.class);

		UIViewRoot ui = mock(UIViewRoot.class);

		when(context.getViewRoot()).thenReturn(ui);
		when(ui.getLocale()).thenReturn(LOCALE);
	}

	@After
	public void tearDown() {
	}

	@Test
	public final void testGetAsObject() {
		LocalDate expected = LocalDate.now();

		LocalDate actual = (LocalDate) this.converter.getAsObject(context, null, expected.format(formatter));
		assertEquals(expected, actual);
	}

	@Test
	public final void testGetAsString() {
		LocalDate date = LocalDate.now();
		
		String expected = date.format(formatter);
		String actual = this.converter.getAsString(context, null, date);
		
		assertEquals(expected, actual);
	}

	@Test(expected=ConverterException.class)
	public final void testInvalidFormat() {
		this.converter.getAsObject(context, null, "NotADate");
	}
	
	@Test
	public final void testNull() {
		assertNull(this.converter.getAsObject(context, null, null));
		assertNull(this.converter.getAsString(context, null, null));
	}
}
