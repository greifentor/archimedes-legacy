package archimedes.legacy.importer.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.model.DiagrammModel;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;

@ExtendWith(MockitoExtension.class)
public class DomainAdderTest {

	@InjectMocks
	private DomainAdder unitUnderTest;

	@DisplayName("Tests for method 'findOrCreateMatchingDomain(ColumnSO column, DiagrammModel)'.")
	@Nested
	class TestForMethod_findOrCreateMatchingDomain_ColumnSO_DiagrammModel {

	}

}