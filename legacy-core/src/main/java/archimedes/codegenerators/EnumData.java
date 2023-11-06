package archimedes.codegenerators;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class EnumData {

	private String enumAttributeName;
	private String enumAttributeNameCamelCase;
	private String enumClassName;
	private String enumPackageName;
	private String itemLabelGeneratorClassName;
	private String itemLabelGeneratorPackageName;

}
