/*
 * UseCaseOpenComplexIndicesFrameAndCloseItTest.java
 *
 * 15.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.jemmy.operators.JFrameOperator;

import archimedes.legacy.scheme.Diagramm;

/**
 * &Ouml;ffnet den Frame zur Wartung von komplexen Indices und schliesst ihn wieder.
 *
 * @author ollie
 *
 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
 */

public class UseCaseOpenComplexIndicesFrameAndCloseItTest {

	private JFrameOperator frame = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		Diagramm d = new Diagramm();
		this.frame = ComplexIndicesAdministrationFrameUtil.openComplexIndicesAdministrationFrame(d);
	}

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@AfterEach
	public void tearDown() throws Exception {
		ComplexIndicesAdministrationFrameUtil.disposeComplexIndicesAdministrationFrame();
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testFrameIsOpenedAfterCreatingANewFrame() throws Exception {
		assertTrue(this.frame.isVisible());
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testFrameIsClosedAfterPressingTheCloseButton() throws Exception {
		ComplexIndicesAdministrationFrameUtil.pressCloseButton();
		Thread.sleep(100);
		assertFalse(this.frame.isVisible());
	}

}