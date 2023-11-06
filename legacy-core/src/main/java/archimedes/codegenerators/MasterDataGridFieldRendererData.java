package archimedes.codegenerators;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class MasterDataGridFieldRendererData {

	private String attributeName;
	private String modelClassName;
	private String modelPackageName;

}
