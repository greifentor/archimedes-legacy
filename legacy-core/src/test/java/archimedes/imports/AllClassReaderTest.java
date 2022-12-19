package archimedes.imports;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.model.DiagrammModel;

@ExtendWith(MockitoExtension.class)
public class AllClassReaderTest {

	@InjectMocks
	private AllClassReader unitUnderTest;

	@Nested
	class TestsOfMethod_readAllClassesFromClassPath {

		@Test
		void returnsAListOfClassesContainingDiagrammModelClass() {
			assertTrue(List.of(unitUnderTest.readAllClassesFromClassPath()).contains(DiagrammModel.class));
		}

	}

}
