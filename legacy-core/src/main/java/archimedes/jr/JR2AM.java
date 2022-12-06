package archimedes.jr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.ObjectFactory;
import archimedes.legacy.scheme.DefaultObjectFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.PanelModel;
import archimedes.model.RelationModel;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.scheme.xml.DiagramXMLBuilder;
import corent.base.Direction;
import corent.base.StrUtil;
import corent.gui.ExtendedColor;
import de.ollie.jrc.jrxml.DirectoryReader;
import de.ollie.jrc.jrxml.model.JasperReport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JR2AM {

	private static final ObjectFactory OBJECT_FACTORY = new DefaultObjectFactory();

	private final String jrProjectPath;

	private Map<String, JasperReport> jasperReports = new HashMap<>();
	private DiagrammModel dataModel = OBJECT_FACTORY.createDiagramm();

	public static void main(String[] args) {
		Archimedes.InitPalette();
		new JR2AM(args[0]).convertToArchimdesModel();
	}

	private void convertToArchimdesModel() {
		readAllJasperReportsOfProject();
		convertJasperReportsProjectToDataModel();
		writeDataModelToFile();
	}

	private void readAllJasperReportsOfProject() {
		try {
			jasperReports = new DirectoryReader(jrProjectPath).readAllReports();
		} catch (IOException | JAXBException e) {
			System.out.println("ERROR while reading the Jasperreports project: " + e.getMessage());
			System.exit(1);
		}
	}

	private void convertJasperReportsProjectToDataModel() {
		StereotypeModel stereotypeRoot = OBJECT_FACTORY.createStereotype("ROOT-DOCUMENT", ";op");
		ViewModel mainView = OBJECT_FACTORY.createMainView("main", "", true);
		dataModel.addView((GUIViewModel) mainView);
		DomainModel identDomain = OBJECT_FACTORY.createDomain("Ident", Types.BIGINT, 0, 0);
		dataModel.addDomain(identDomain);
		dataModel.addStereotype(stereotypeRoot);
		for (String key : jasperReports.keySet()) {
			JasperReport jasperReport = jasperReports.get(key);
			PanelModel panel = OBJECT_FACTORY.createPanel();
			TableModel table = OBJECT_FACTORY.createTabelle(dataModel.getViewByName("main"), 0, 0, dataModel, false);
			table.setName(reportName2TableName(key));
			table.setBackgroundColor(Archimedes.PALETTE.get(StrUtil.FromHTML("wei&szlig;")));
			table.setFontColor(Archimedes.PALETTE.get("schwarz"));
			table.setDraft(false);
			table.addPanel(panel);
//			jasperReport
//					.getFields()
//					.forEach(field -> table.addColumn(createColumn("field_" + field.getName(), field.getCls(), panel)));
//			jasperReport
//					.getParameters()
//					.forEach(
//							parameter -> table
//									.addColumn(
//											createColumn(
//													"parameter_" + parameter.getName(),
//													"java.lang.String",
//													panel)));
//			jasperReport
//					.getVariables()
//					.forEach(
//							variable -> table
//									.addColumn(
//											createColumn("variable_" + variable.getName(), "java.lang.String", panel)));
			ColumnModel idColumn = OBJECT_FACTORY.createTabellenspalte("id", identDomain, true);
			idColumn.setPanel(panel);
			table.addColumn(idColumn);
			if (jasperReport
					.getProperties()
					.stream()
					.filter(p -> p.getName().equals("com.jaspersoft.studio.report.description"))
					.anyMatch(p -> p.getValue().contains("(ROOT)"))) {
				table.addStereotype(stereotypeRoot);
			}
			dataModel.addTable(table);
		}
		for (String key : jasperReports.keySet()) {
			JasperReport jasperReport = jasperReports.get(key);
			key = reportName2TableName(key);
			TableModel table = dataModel.getTableByName(key);
			if ((table != null) && !jasperReport.findAllCalledReportsFrom().isEmpty()) {
				jasperReport.findAllCalledReportsFrom().forEach(reportName -> {
					String k = reportName2TableName(reportName);
					TableModel referencedTable = dataModel.getTableByName(k);
					if (table.getColumnByName(k) == null) {
						if (referencedTable == null) {
							System.out.println("ERROR: Referenced table '" + k + "' does not exists!");
							System.exit(3);
						}
						ColumnModel referencedColumn = referencedTable.getColumnByName("id");
						ColumnModel column = createColumn(k, "Ident", (PanelModel) table.getPanelAt(0));
						column.setNotNull(true);
						System.out.println("CREATE RELATION " + column + " -> " + referencedColumn);
						RelationModel relation = OBJECT_FACTORY
								.createRelation(mainView, column, Direction.UP, 0, referencedColumn, Direction.UP, 0);
						column.setRelation(relation);
						table.addColumn(column);
						System.out.println(table);
					}
				});
			}
		}
	}

	private String reportName2TableName(String name) {
		name = name.replace("\\", "/").replace(".jrxml", "");
		return name.substring(name.lastIndexOf("/") + 1);
	}

	private ColumnModel createColumn(String name, String type, PanelModel panel) {
		ColumnModel column = OBJECT_FACTORY.createTabellenspalte(name, getDomain(type));
		column.setPanel(panel);
		return column;
	}

	private DomainModel getDomain(String name) {
		DomainModel domain = dataModel.getDomainByName(name);
		if (domain == null) {
			domain = OBJECT_FACTORY.createDomain(name, Types.BLOB, 0, 0);
			dataModel.addDomain(domain);
		}
		return domain;
	}

	private void writeDataModelToFile() {
		try {
			FileWriter fw = new FileWriter("jr2am.xml", false);
			final BufferedWriter writer = new BufferedWriter(fw);
			writer
					.write(
							new DiagramXMLBuilder()
									.buildXMLContentForDiagram(
											dataModel,
											Archimedes.PALETTE.getColors().toArray(new ExtendedColor[0])));
			writer.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("ERROR while writing the Archimedes model: " + e.getMessage());
			System.exit(2);
		}
	}

}
