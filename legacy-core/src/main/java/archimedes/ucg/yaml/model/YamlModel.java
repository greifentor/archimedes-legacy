package archimedes.ucg.yaml.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YamlModel {

	private String title;
	private List<YamlClassDefinition> classes;
	private List<YamlEnumDefinition> enums;
	private List<YamlProperty> properties;

}
