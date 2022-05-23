package archimedes.codegenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.experimental.Accessors;

public class ImportDeclarations {

	public interface ImportDeclaration {

		String getTypeName();

		String getPackageName();

		default String getSingleImportDeclaration() {
			return getPackageName() + "." + getTypeName();
		}

	}

	@Accessors(chain = true)
	@Data
	public static class ImportDeclarationContainer implements ImportDeclaration {

		private String packageName;
		private String typeName;

	}

	public static boolean equals(ImportDeclaration impDec1, ImportDeclaration impdec1) {
		return (impDec1 != null) && (impdec1 != null) && impDec1.getTypeName().equals(impdec1.getTypeName())
				&& impDec1.getPackageName().equals(impdec1.getPackageName());
	}

	private List<ImportDeclaration> importDeclarations = new ArrayList<>();

	public ImportDeclarations add(String packageName, String typeName) {
		return add(new ImportDeclarationContainer().setPackageName(packageName).setTypeName(typeName));
	}

	public ImportDeclarations add(ImportDeclaration impDec) {
		if (!importDeclarations.contains(impDec)) {
			importDeclarations.add(impDec);
		}
		return this;
	}

	public ImportDeclarations add(ImportDeclarationProvider idp) {
		for (ImportDeclaration impDec : idp.getImportDeclarations()) {
			if (!importDeclarations.contains(impDec)) {
				importDeclarations.add(impDec);
			}
		}
		return this;
	}

	public boolean contains(ImportDeclaration impDec) {
		return importDeclarations.stream().anyMatch(impDec0 -> equals(impDec, impDec0));
	}

	public List<ImportDeclaration> toList() {
		return importDeclarations
				.stream()
				.sorted(
						(i0, i1) -> i0
								.getSingleImportDeclaration()
								.toLowerCase()
								.compareTo(i1.getSingleImportDeclaration().toLowerCase()))
				.collect(Collectors.toList());
	}

}