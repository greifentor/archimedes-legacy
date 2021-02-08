/*
 * MethodCode.java
 *
 * 28.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.acf;

import static corentx.util.Checks.*;

/**
 * A container for a method code.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 28.06.2016 - Added.
 */

public class MethodCode implements Comparable<MethodCode> {

    private String code = null;
    private String name = null;
    private boolean staticMethod = false;

    /**
     * Creates a new method code container with the passed parameters.
     *
     * @param name The name of the method.
     * @param code The code of the method.
     * @param staticMethod Set this flag if the method is a static one.
     *
     * @changed OLI 28.06.2016 - Added.
     */
    public MethodCode(String name, String code, boolean staticMethod) {
        super();
        ensure(code != null, "code cannot be null");
        ensure(!code.isEmpty(), "code cannot be empty");
        ensure(name != null, "name cannot be null");
        ensure(!name.isEmpty(), "name cannot be empty");
        this.code = code;
        this.name = name;
        this.staticMethod = staticMethod;
    }

    /**
     * @changed OLI 28.06.2016 - Added.
     */
    @Override public int compareTo(MethodCode o) {
        int c = new Boolean(this.isStaticMethod()).compareTo(new Boolean(o.isStaticMethod()));
        if (c == 0) {
            c = this.getName().compareTo(o.getName());
        }
        return c;
    }

    /**
     * Returns the code of the method.
     *
     * @return The code of the method.
     *
     * @changed OLI 28.06.2016 - Added.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the name of the method.
     *
     * @return The name of the method.
     *
     * @changed OLI 28.06.2016 - Added.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if the method is static.
     *
     * @return <CODE>true</CODE> if the method should be static.
     *
     * @changed OLI 28.06.2016 - Added.
     */
    public boolean isStaticMethod() {
        return this.staticMethod;
    }

}