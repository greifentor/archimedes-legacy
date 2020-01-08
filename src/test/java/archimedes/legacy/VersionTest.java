/*
 * VersionTest.java
 *
 * **.**.**** (automatisch generiert)
 *
 * (c) by ollie
 *
 */

package archimedes.legacy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Tests zur Klasse VersionTest.
 * 
 * @author ollie
 * 
 * @changed *** **.**.**** - Automatisch generiert.
 */

public class VersionTest {

	@Test
	/** Test auf bestehen der statischen Instanz. */
	public void testInstanceNotNull() {
		assertNotNull(Version.INSTANCE);
	}

	@Test
	/** Test der Methode <TT>getVersion()</TT>. */
	public void testGetVersion() {
		assertEquals("1.83.4", Version.INSTANCE.getVersion());
	}

	@Test
	/** Test der Methode <TT>toString()</TT>. */
	public void testToString() {
		assertEquals("1.83.4", Version.INSTANCE.toString());
	}

}
