package archimedes.ucg.yaml.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YamlArchimedesView {

	private String name;
	private String description;
	private boolean mainView;
	private boolean showReferencedColumns;
	private boolean hideTechnicalFields;
	private boolean hideTransientFields;
	private List<YamlArchimedesTablePosition> tables;
	private List<YamlArchimedesRelation> relations;

}
