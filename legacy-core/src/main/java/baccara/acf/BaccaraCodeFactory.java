/*
 * BaccaraCodeFactory.java
 *
 * 16.09.2015
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf;

import archimedes.acf.*;
import archimedes.acf.checker.*;
import archimedes.acf.param.*;
import archimedes.acf.util.*;
import archimedes.model.*;
import baccara.*;
import baccara.acf.checkers.*;
import baccara.gui.*;


/**
 * An implementation of the Archimedes code factory interface. This is the main class of the
 * project.
 *
 * <TABLE BORDER=1 WIDTH="100%">
 *     <TR>
 *         <TH>Version</TH>
 *         <TH>Changes</TH>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.11</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Implementation of a data writer generation suppress option
 *                    (NO_DATA_WRITER).
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.10</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Extended the by the logic for application data manager listeners.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Extended the interface <CODE>CodeGenerator</CODE> by the method
 *                    <CODE>setIndividualPreferences(IndividualPreferences)</CODE>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Basic functionallity.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 * </TABLE>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.09.2015 - Added based on the <CODE>Isis</CODE> implementation.
 */

public class BaccaraCodeFactory extends AbstractCodeFactory
        implements PredeterminedOptionProvider {

    /** Stereo type marker for table which should be excluded from generation process. */
    public static final String EXCLUSION_STEREO_TYPE_NAME = "!!! CODE NOT GENERATED !!!";
    /** A name constant for the factory. */
    public static final String NAME = "BaccaraACF";

    private ParamIdGetter parameterIdGetter = new ParamIdGetter();

    /**
     * Creates a new Isis code factory.
     *
     * @changed OLI 16.09.2015 - Added.
     */
    public BaccaraCodeFactory() {
        super();
    }

    /**
     * @changed OLI 31.03.2016 - Added.
     */
    @Override public CodeGeneratorListFactory createCodeGeneratorListFactory() {
        return new BaccaraCodeGeneratorListFactory();
    }

    /**
     * @changed OLI 31.03.2016 - Added.
     */
    @Override public String getExclusionStereoTypeName() {
        return EXCLUSION_STEREO_TYPE_NAME;
    }

    /**
     * @changed OLI 08.12.2016 - Added.
     */
    @Override public GUIBundle getGUIBundle() {
        return this.guiBundle;
    }

    /**
     * @changed OLI 31.03.2016 - Added.
     */
    @Override public ModelChecker[] getModelCheckers() {
        return new ModelChecker[] {
                new ModelCheckerCodeGeneratorOptionFieldNotEmpty(this.guiBundle),
                new ModelCheckerEditorMemberHasNoLabelText(this.guiBundle),
                new ModelCheckerNReferencesAreSetProperly(this.guiBundle),
                new ModelCheckerDomainSetForAllColumns(this.guiBundle),
                new ModelCheckerTableNameDoesNotContainSpecialCharacters(this.guiBundle),
                new BaccaraModelCheckerPanelTitleValidation(this.guiBundle),
                new BaccaraModelCheckerCodePathForTables(this.guiBundle),
                new BaccaraModelCheckerParameterTableReferences(this.guiBundle),
                new BaccaraModelCheckerSelectionMemberPrintExpressionValid(this.guiBundle)
        };
    }

    /**
     * @changed OLI 31.03.2016 - Added.
     */
    @Override public String getVersion() {
        return Version.INSTANCE.getVersion();
    }

    /**
     * @changed OLI 10.06.2016 - Added.
     */
    @Override public String[] getResourceBundleNames() {
        return new String[] {"archimedes", "baccaraacf"};
    }

    /**
     * @changed OLI 28.04.2016 - Added.
     */
    @Override public String[] getSelectableOptions(OptionType optionType) {
        if (optionType == OptionType.COLUMN) {
            return this.parameterIdGetter.getPublicStaticFinalStringsForClass(ColParamIds.class
                    );
        } else if (optionType == OptionType.DOMAIN) {
            return this.parameterIdGetter.getPublicStaticFinalStringsForClass(
                    DomainParamIds.class);
        } else if (optionType == OptionType.MODEL) {
            return this.parameterIdGetter.getPublicStaticFinalStringsForClass(
                    ModelParamIds.class);
        } else if (optionType == OptionType.PANEL) {
            return this.parameterIdGetter.getPublicStaticFinalStringsForClass(
                    PanelParamIds.class);
        } else if (optionType == OptionType.TABLE) {
            return this.parameterIdGetter.getPublicStaticFinalStringsForClass(
                    TableParamIds.class);
        }
        return new String[0];
    }

    /**
     * @changed OLI 18.04.2017 - Added.
     */
    @Override public String getName() {
        return NAME;
    }

}