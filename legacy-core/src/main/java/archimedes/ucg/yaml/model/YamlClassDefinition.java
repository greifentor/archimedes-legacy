package archimedes.ucg.yaml.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YamlClassDefinition {

	private String name;
	private List<YamlAttribute> attributes;
	private List<YamlProperty> properties;

}
