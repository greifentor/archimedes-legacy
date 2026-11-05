package archimedes.ucg.yaml.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YamlAttribute {

	private String name;
	private YamlType type;
	private String defaultValue;
	private boolean enumType;
	private List<YamlProperty> properties;
	private boolean reference;

}
