package archimedes.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;

/**
 * A checker for classes implementing a specific interface.
 * 
 * NOTE: It is using the guava stuff to manage the task.
 * 
 * @author ollie (10.12.2022)
 *
 */
public class ImplementationCheckerOLD {

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
		List<Class<?>> implementations = new ArrayList<>();
		try {
			ClassPath classPath = ClassPath.from(getClass().getClassLoader());
			Set<ClassInfo> classInfos = classPath.getAllClasses();
			int i = 0;
			int leni = classInfos.size();
			for (ClassInfo ci : classInfos) {
				try {
					Class<?> c = Class.forName(ci.getName(), false, getClass().getClassLoader());
					if (observer != null) {
						observer.classChecking(new ImplementationCheckerEvent(c, i, leni));
					}
					if (implementsInterface(c, implementedInterfaceName)) {
						implementations.add(c);
					}
					if (observer != null) {
						observer.classChecked(new ImplementationCheckerEvent(c, i, leni));
					}
				} catch (Throwable e) {
					// NOP
				}
				i++;
			}
		} catch (Throwable e) {
			// NOP
		}
		return implementations;
	}

	private boolean implementsInterface(Class<?> c, String interfaceName) {
		return List.of(c.getInterfaces()).stream().anyMatch(i -> i.getName().equals(interfaceName));
	}

}