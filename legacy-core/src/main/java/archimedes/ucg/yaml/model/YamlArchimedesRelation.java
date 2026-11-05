package archimedes.ucg.yaml.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YamlArchimedesRelation {

	private String table;
	private String column;
	private String direction0;
	private int offset0;
	private String direction1;
	private int offset1;
	private List<YamlArchimedesPoint> points;

}
