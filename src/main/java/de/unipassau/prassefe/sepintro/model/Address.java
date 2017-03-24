package de.unipassau.prassefe.sepintro.model;

/**
 * Address struct.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class Address {
	/**
	 * The street.
	 */
	private String street;
	
	/**
	 * The street number.
	 */
	private int streetNumber;
	
	/**
	 * The zip / postal code.
	 */
	private String zipcode;
	
	/**
	 * The city.
	 */
	private String city;
	
	/**
	 * The country.
	 */
	private String country;

	
	
	/**
	 * Create new Address
	 * @param street The street
	 * @param streetNumber the street number
	 * @param zipcode the zip code
	 * @param city the city
	 * @param country the country
	 */
	public Address(String street, int streetNumber, String zipcode, String city, String country) {
		this.street = street;
		this.streetNumber = streetNumber;
		this.zipcode = zipcode;
		this.city = city;
		this.country = country;
	}
	
        /**
         * Create new empty address.
         */
	public Address() {
		this("", 0, "", "", "");
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the streetNumber
	 */
	public int getStreetNumber() {
		return streetNumber;
	}

	/**
	 * @param streetNumber the streetNumber to set
	 */
	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
}
