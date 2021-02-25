package archimedes.legacy.updater;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.updater.UpdateReportAction.Status;

@ExtendWith(MockitoExtension.class)
public class UpdateReportTest {

	@InjectMocks
	private UpdateReport unitUnderTest;

	@Nested
	class TestsOfMethod_hasAtLeastOneActionInStatus_UpdateReportActionStatus {

		@ParameterizedTest
		@EnumSource(Status.class)
		void calledOnAnEmptyUpdateReport_ReturnsFalse(Status status) {
			assertFalse(unitUnderTest.hasAtLeastOneActionInStatus(status));
		}

		@Test
		void calledOnAnUpdateReportWithActionsOfAnotherStatus_ReturnsFalse() {
			// Prepare
			unitUnderTest.addUpdateReportAction(new UpdateReportAction().setStatus(Status.FAILED));
			unitUnderTest.addUpdateReportAction(new UpdateReportAction().setStatus(Status.MANUAL));
			// Check
			assertFalse(unitUnderTest.hasAtLeastOneActionInStatus(Status.DONE));
		}

		@Test
		void calledOnAnUpdateReportWithActionsOfThePassedStatus_ReturnsFalse() {
			// Prepare
			unitUnderTest.addUpdateReportAction(new UpdateReportAction().setStatus(Status.FAILED));
			unitUnderTest.addUpdateReportAction(new UpdateReportAction().setStatus(Status.DONE));
			// Check
			assertTrue(unitUnderTest.hasAtLeastOneActionInStatus(Status.DONE));
		}

	}

}