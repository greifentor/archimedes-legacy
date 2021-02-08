/*
 * AlternatePathContainer.java
 *
 * 12.07.2014
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.gui;


/**
 * A container for the alternate pathes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 12.07.2014 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class AlternatePathContainer implements Comparable<AlternatePathContainer> {

    private String alternatePath = null;
    private String token = null;

    /**
     * Creates a new alternate path container with the passed parameters.
     *
     * @param token The token for the alternate path container.
     * @param alternatePath The alternate path for the token.
     *
     * @changed OLI 12.07.2014 - Added.
     */
    public AlternatePathContainer(String token, String alternatePath) {
        super();
        this.alternatePath = alternatePath;
        this.token = token;
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public int compareTo(AlternatePathContainer o) {
        return this.getToken().compareTo(((AlternatePathContainer) o).getToken());
    }

    /**
     * Returns the alternate path.
     *
     * @return The alternate path.
     *
     * @changed OLI 12.07.2014 - Added.
     */
    public String getAlternatePath() {
        return this.alternatePath;
    }

    /**
     * Returns the token.
     *
     * @return The token.
     *
     * @changed OLI 12.07.2014 - Added.
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Sets a new alternate path.
     *
     * @param alternatePath The new alternate path.
     *
     * @changed OLI 12.07.2014 - Added.
     */
    public void setAlternatePath(String alternatePath) {
        this.alternatePath = alternatePath;
    }

}