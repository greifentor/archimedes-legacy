package archimedes.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class AllClassReader {

	private static AllClassReader INSTANCE = null;
	private static List<Class<?>> classes = new ArrayList<>();

	public synchronized static AllClassReader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AllClassReader();
		}
		return INSTANCE;
	}

	private AllClassReader() {
		try {
			ClassPath classPath = ClassPath.from(getClass().getClassLoader());
			Set<ClassInfo> classInfos = classPath.getAllClasses();
			for (ClassInfo ci : classInfos) {
				try {
					Class<?> c = Class.forName(ci.getName(), false, getClass().getClassLoader());
					if (c != null) {
					classes.add(c);
					}
				} catch (Throwable e) {
					// NOP
				}
			}
		} catch (Throwable e) {
			// NOP
		}
	}

	public Class<?>[] readAllClassesFromClassPath() {
		return classes.toArray(new Class<?>[classes.size()]);
	}

}
