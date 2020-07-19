/*
 * EnumInfo.java
 *
 * 06.04.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf;

import static corentx.util.Checks.*;


/**
 * A container with enum type infos.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.04.2016 - Added.
 */

public class EnumInfo {

    private String enumTypeName = null;
    private String packageName = null;

    /**
     * Creates a new enum type info with the passed parameters.
     *
     * @param enumTypeName The name of the enum class.
     * @param packageName The name of the package which contains the enum type. Set
     *         <CODE>null</CODE> or an empty string for no package.
     * @throws IllegalArgumentException Passing a null pointer or an empty String as name.
     *
     * @changed OLI 06.04.2016 - Added.
     */
    public EnumInfo(String enumTypeName, String packageName) {
        super();
        ensure(enumTypeName != null, "enum type name cannot be null.");
        ensure(!enumTypeName.isEmpty(), "enum type name cannot by empty.");
        this.enumTypeName = enumTypeName;
        this.packageName = (packageName == null ? "" : packageName);
    }

    /**
     * Returns the enum type name.
     *
     * @return The enum type name.
     *
     * @changed OLI 06.04.2016 - Added.
     */
    public String getEnumTypeName() {
        return this.enumTypeName;
    }

    /**
     * Returns the package name.
     *
     * @return The package name.
     *
     * @changed OLI 06.04.2016 - Added.
     */
    public String getPackageName() {
        return this.packageName;
    }

}