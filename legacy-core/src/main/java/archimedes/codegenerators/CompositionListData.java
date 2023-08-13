package archimedes.codegenerators;

import archimedes.model.ColumnModel;
import archimedes.model.TableModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CompositionListData {

	private TableModel memberTable;
	private ColumnModel backReferenceColumn;

}
