package de.unipassau.prassefe.sepintro.jsf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = LocalDate.class)
public class LocalDateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null) {
			return null;
		}

		try {
			return LocalDate.parse(value, getFormatter(context));
		} catch (DateTimeParseException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
	                "Invalid date format!",
	                "");
			throw new ConverterException(msg);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}

		LocalDate dateValue = (LocalDate) value;
		return dateValue.format(getFormatter(context));
	}

	private DateTimeFormatter getFormatter(FacesContext context) {
		Locale locale = context.getViewRoot().getLocale();
		return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale);
	}
}
