package archimedes.imports;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.imports.ImplementationCheckerOLD.ImplementationCheckerObserver;

@ExtendWith(MockitoExtension.class)
public class ImplementationCheckerOLDTest {

	@InjectMocks
	private ImplementationCheckerOLD unitUnderTest;

	@Nested
	class TestsOfMethod_getClassesImplementingInterfaceWithName_String {

		@Test
		void returnsAnEmptyList_passingAInterfaceNameAsNull() {
			assertTrue(unitUnderTest.getClassesImplementingInterfaceWithName(null, null).isEmpty());
		}

		@Test
		void returnsAnEmptyList_passingANotExistingInterfaceName() {
			assertTrue(unitUnderTest.getClassesImplementingInterfaceWithName(";op", null).isEmpty());
		}

		@Test
		void returnsAListWithTheCorrectClassName_passingANotExistingInterfaceName() {
			assertTrue(
					unitUnderTest
							.getClassesImplementingInterfaceWithName("archimedes.model.DataModel", null)
							.stream()
							.anyMatch(c -> c.getSimpleName().equals("DiagrammModel")));
		}

		@Test
		void observerMethodClassCheckingIsCalledCorrectly() {
			ImplementationCheckerObserver observer = mock(ImplementationCheckerObserver.class);
					unitUnderTest
							.getClassesImplementingInterfaceWithName("archimedes.model.DataModel", observer)
							.size();
			verify(observer, atLeast(1)).classChecking(any(ImplementationCheckerOLD.ImplementationCheckerEvent.class));
		}

		@Test
		void observerMethodClassCheckedIsCalledCorrectly() {
			ImplementationCheckerObserver observer = mock(ImplementationCheckerObserver.class);
			unitUnderTest.getClassesImplementingInterfaceWithName("archimedes.model.DataModel", observer).size();
			verify(observer, atLeast(1)).classChecked(any(ImplementationCheckerOLD.ImplementationCheckerEvent.class));
		}

	}

}
