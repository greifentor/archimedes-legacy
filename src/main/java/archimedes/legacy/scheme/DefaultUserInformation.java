/*
 * DefaultUserInformation.java
 *
 * 24.02.2012
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.UserInformation;

/**
 * Eine Defaultimplementierung des Interfaces zur Nutzung in der
 * Archimedes-Applikation.
 * 
 * @author ollie
 * 
 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
 */

public class DefaultUserInformation implements UserInformation {

	private String userName = null;
	private String userToken = null;
	private String vendorName = null;

	/**
	 * Erzeugt ein neues UserInformation-Objekt anhand der &uuml;bergebenen
	 * Daten.
	 * 
	 * @param userName
	 *            Der Name des Benutzers.
	 * @param userToken
	 *            Das K&uuml;rzel zum Benutzer.
	 * @param vendorName
	 *            Der Name des Herstellers.
	 * @throws IllegalArgumentException
	 *             Falls einer der Parameter leer oder als <CODE>null</CODE>
	 *             -Pointer &uuml;bergeben wird.
	 * 
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	public DefaultUserInformation(String userName, String userToken, String vendorName) throws IllegalArgumentException {
		super();
		ensure(userName != null, "user name cannot be null.");
		ensure(!userName.isEmpty(), "user name cannot be empty.");
		ensure(userToken != null, "user token cannot be null.");
		ensure(!userToken.isEmpty(), "user token cannot be empty.");
		ensure(vendorName != null, "vendor name cannot be null.");
		ensure(!vendorName.isEmpty(), "vendor name cannot be empty.");
		this.userName = userName;
		this.userToken = userToken;
		this.vendorName = vendorName;
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Override
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Override
	public String getUserToken() {
		return this.userToken;
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Override
	public String getVendorName() {
		return this.vendorName;
	}

}