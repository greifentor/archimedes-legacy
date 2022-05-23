package archimedes.codegenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.experimental.Accessors;

public class FieldDeclarations {

	public interface FieldDeclaration {

		String getAttributeName();

		String getTypeName();

	}

	@Accessors(chain = true)
	@Data
	public static class FieldDeclarationContainer implements FieldDeclaration {

		private String attributeName;
		private String typeName;

	}

	public static boolean equals(FieldDeclaration fDec0, FieldDeclaration fDec1) {
		return (fDec0 != null) && (fDec1 != null) && fDec0.getTypeName().equals(fDec1.getTypeName())
				&& fDec0.getAttributeName().equals(fDec1.getAttributeName());
	}

	private List<FieldDeclaration> fieldDeclarations = new ArrayList<>();

	public FieldDeclarations add(String typeName, String attributeName) {
		return add(new FieldDeclarationContainer().setAttributeName(attributeName).setTypeName(typeName));
	}

	public FieldDeclarations add(FieldDeclaration fDec) {
		if (!fieldDeclarations.contains(fDec)) {
			fieldDeclarations.add(fDec);
		}
		return this;
	}

	public boolean contains(FieldDeclaration fDec) {
		return fieldDeclarations.stream().anyMatch(fDec0 -> equals(fDec, fDec0));
	}

	public List<FieldDeclaration> toList() {
		return fieldDeclarations
				.stream()
				.sorted((i0, i1) -> i0.getAttributeName().toLowerCase().compareTo(i1.getAttributeName().toLowerCase()))
				.collect(Collectors.toList());
	}

}