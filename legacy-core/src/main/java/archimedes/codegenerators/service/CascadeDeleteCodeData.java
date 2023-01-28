package archimedes.codegenerators.service;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CascadeDeleteCodeData {

	private String findMethodName;
	private String persistencePortAttributeName;
	private String persistencePortInterfaceName;
	private String persistencePortPackageName;

}
