package archimedes.codegenerators;

import java.util.List;

import archimedes.codegenerators.ImportDeclarations.ImportDeclaration;

public interface ImportDeclarationProvider {

	List<ImportDeclaration> getImportDeclarations();

}