package archimedes.ucg.yaml.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YamlArchimedesSection {

	private String title;
	private List<YamlArchimedesView> views;
	private List<YamlArchimedesTable> tables;
	private List<YamlArchimedesColor> colors;

}
