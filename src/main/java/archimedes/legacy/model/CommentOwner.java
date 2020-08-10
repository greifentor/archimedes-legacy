/*
 * CommentOwner.java
 *
 * 11.03.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An interface to implement by classes whose objects own a comment.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.03.2016 - Added.
 */

public interface CommentOwner {

    /**
     * Returns the comment for the object.
     *
     * @return The comment for the object.
     *
     * @changed OLI 11.03.2016 - Added.
     */
    abstract public String getComment();

    /**
     * Sets the passed value as new comment for the object.
     *
     * @param comment The new comment for the object.
     *
     * @changed OLI 11.03.2016 - Added.
     */
    abstract public void setComment(String comment);

}