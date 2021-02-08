/*
 * BaccaraCodeGeneratorListFactory.java
 *
 * 16.09.2015
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf;


import baccara.acf.entities.*;
import baccara.acf.gui.*;
import baccara.acf.persistence.*;
import baccara.acf.resources.*;
import archimedes.acf.*;

import java.util.*;


/**
 * A list factory for the code generators called for Baccara.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.09.2015 - Added.
 */

public class BaccaraCodeGeneratorListFactory implements CodeGeneratorListFactory {

    /**
     * @changed OLI 16.09.2015 - Added.
     */
    @Override public List<CodeGenerator> getCodeGenerators() {
        List<CodeGenerator> l = new Vector<CodeGenerator>();
        // archgen classes
        l.add(new GeneratedPOJOAttributeIdCodeGenerator());
        l.add(new GeneratedPOJOChangeListenerCodeGenerator());
        l.add(new GeneratedPOJOChangeListenerEventCodeGenerator());
        l.add(new GeneratedPOJOCodeGenerator());
        l.add(new GeneratedPrimaryKeyCodeGenerator());
        // business classes
        l.add(new ApplicationDataManagerCodeGenerator());
        l.add(new ListManagerInterfaceCodeGenerator());
        l.add(new POJOCodeGenerator());
        l.add(new PrimaryKeyCodeGenerator());
        // gui classes
        l.add(new ComboBoxCellRendererCodeGenerator());
        l.add(new GeneratedBaccaraEditorPanelCodeGenerator());
        l.add(new BaccaraEditorPanelCodeGenerator());
        l.add(new GeneratedListMaintenanceTableModelCodeGenerator());
        l.add(new ListMaintenanceTableModelCodeGenerator());
        l.add(new GeneratedListMaintenanceEditorListManagerCodeGenerator());
        l.add(new ListMaintenanceEditorListManagerCodeGenerator());
        l.add(new GeneratedListMaintenancePanelCodeGenerator());
        l.add(new ListMaintenancePanelCodeGenerator());
        l.add(new GeneratedBaccaraEditorInternalFrameCodeGenerator());
        l.add(new BaccaraEditorInternalFrameCodeGenerator());
        l.add(new BaccaraListCellRendererCodeGenerator());
        l.add(new EditorListManagerCodeGenerator());
        l.add(new BaccaraListEditorInternalFrameCodeGenerator());
        // persistence
        l.add(new AbstractApplicationDataWriterCodeGenerator());
        l.add(new DataWriterCodeGenerator());
        return l;
    }

    /**
     * @changed OLI 06.07.2016 - Added.
     */
    @Override public ResourceUpdater getResourceUpdater() {
        return new BaccaraResourceUpdater();
    }

}