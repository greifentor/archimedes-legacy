package archimedes.codegenerators;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

public class DataModelReader {

	public static final String EXAMPLE_XMLS = "src/test/resources/examples/dm/";
	public static final String TEST_XMLS = "src/test/resources/dm/codegenerators/";

	public static DataModel readDataModel(String fileName) {
		return readDataModel(fileName, null);
	}

	public static DataModel readDataModel(String fileName, String path) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read((path == null ? TEST_XMLS : path) + fileName);
	}

}
