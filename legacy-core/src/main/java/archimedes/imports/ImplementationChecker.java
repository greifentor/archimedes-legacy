package archimedes.imports;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;

public class ImplementationChecker {

	static final AllClassReader allClassInfoReader = AllClassReader.getInstance();

	public static interface ImplementationCheckerObserver {
		void classChecking(ImplementationCheckerEvent event);

		void classChecked(ImplementationCheckerEvent event);
	}

	@AllArgsConstructor
	@Getter
	@EqualsAndHashCode
	@Generated
	public static class ImplementationCheckerEvent {
		Class<?> cls;
		int index;
		int totalClassNumber;
	}

	public List<Class<?>> getClassesImplementingInterfaceWithName(String implementedInterfaceName,
			ImplementationCheckerObserver observer) {
		return null;
	}

}
