/*
 * DataModelComparison.java
 *
 * 19.02.2016
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.comparision;

import static corentx.util.Checks.ensure;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.meta.MetaDataModel;
import archimedes.meta.MetaDataModelComparator;
import archimedes.meta.MetaDataReader;
import archimedes.meta.chops.AbstractChangeOperation;
import archimedes.model.DataModel;
import archimedes.util.NameGenerator;
import corent.db.DBExecMode;
import corent.files.ExtensionFileFilter;
import corent.files.Inifile;
import corent.files.StructuredTextFile;
import corent.gui.DefaultFrameTextViewerComponentFactory;
import corent.gui.FrameTextViewer;

/**
 * Starts a comparison process between a passed and a stored data model.
 * 
 * @author ollie
 * 
 * @changed OLI 19.02.2016 - Added.
 */

public class DataModelComparison {

	/**
	 * Starts a comparison between the passed and a stored data model.
	 * 
	 * @param dataModel
	 *            The data model which the stored one is to compare to.
	 * @param ini
	 *            An access to the inifile.
	 * 
	 * @changed OLI 19.02.2016 - Added.
	 */
	public DataModelComparison(DataModel dataModel, Inifile ini) {
		super();
		ensure(dataModel != null, "data model cannot be null.");
		ensure(ini != null, "ini file cannot be null.");
		DBExecMode dbMode = DBExecMode.STANDARDSQL;
		MetaDataReader mdr = new MetaDataReader();
		MetaDataModel toCompare = this.selectAndReadDataModel(mdr, ini, dbMode);
		if (toCompare != null) {
			MetaDataModel org = mdr.read(dataModel, dbMode);
			AbstractChangeOperation[] acos = new MetaDataModelComparator(new NameGenerator(dbMode)).compare(toCompare,
					org);
			this.showChangesToUpdateOrgToToCompare(acos, ini);
		}
	}

	private MetaDataModel selectAndReadDataModel(MetaDataReader mdr, Inifile ini, DBExecMode dbMode) {
		String path = ini.readStr("Comparison", "Path", ".");
		JFileChooser fc = new JFileChooser(path);
		fc.setAcceptAllFileFilterUsed(false);
		ExtensionFileFilter eff = new ExtensionFileFilter(new String[] { "ads" });
		fc.setFileFilter(eff);
		fc.setCurrentDirectory(new File(path));
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			DiagrammModel dataModel = new Diagramm();
			String fn = fc.getSelectedFile().getAbsolutePath().replace("\\", "/");
			StructuredTextFile stf = new StructuredTextFile(fn);
			try {
				ini.writeStr("Comparison", "Path", fn.substring(0, fn.lastIndexOf("/")));
				stf.setHTMLCoding(true);
				stf.load();
				dataModel = dataModel.createDiagramm(stf);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return mdr.read(dataModel, dbMode);
		}
		return null;
	}

	private void showChangesToUpdateOrgToToCompare(AbstractChangeOperation[] acos, Inifile ini) {
		String todo = "";
		for (AbstractChangeOperation aco : acos) {
			if (todo.length() > 0) {
				todo += "\n";
			}
			todo += aco.toString();
		}
		Vector v = new Vector();
		v.add(todo);
		new FrameTextViewer(v, DefaultFrameTextViewerComponentFactory.INSTANCE, ini, "Todos", ".");
	}

}