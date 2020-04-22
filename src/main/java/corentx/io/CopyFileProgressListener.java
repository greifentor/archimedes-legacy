/*
 * CopyFileProgressListener.java
 *
 * 06.06.2017
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.io;


/**
 * An interface to listen to the progress of a file copy.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.06.2017 - Added.
 */

public interface CopyFileProgressListener {

    /**
     * This method will be called if the file copy process is finished.
     *
     * @param bytesCopied The number of bytes actually copied.
     *
     * @changed OLI 06.06.2017 - Added.
     */
    abstract public void fileCopyFinished(long bytesCopied);

    /**
     * This method will be called if the file copy process is in progress.
     *
     * @param bytesCopied The number of bytes actually copied.
     *
     * @changed OLI 06.06.2017 - Added.
     */
    abstract public void fileCopyProgressed(long bytesCopied);

    /**
     * This method will be called if the file copy process is started.
     *
     * @param bytesToCopy The number of bytes to copy.
     * @param fileNameSource The name of the file to copy.
     * @param fileNameTarget The name of the file which acts as target. 
     *
     * @changed OLI 06.06.2017 - Added.
     */
    abstract public void fileCopyStarted(long bytesToCopy, String fileNameSource,
            String fileNameTarget);

}