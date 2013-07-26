
package org.sourcebin.digitalprime.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * FileUtils utilities.
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class FileUtils {

    /**
     * Moves a file.
     * @param sourceFile
     * @param destFile
     * @return true if successful, false otherwise.
     */
    public static boolean moveFile(String sourceFile, String destFile) {
        java.io.File source = new java.io.File(sourceFile);
        java.io.File dest = new java.io.File(destFile);
        try {
            copyFile(source, dest);
            source.delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copies a file
     * @param sourceFile
     * @param destFile
     * @return true if successful, false otherwise.
     */
    public static boolean copyFile(String sourceFile, String destFile) {
        java.io.File source = new java.io.File(sourceFile);
        java.io.File dest = new java.io.File(destFile);
        try {
            copyFile(source, dest);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copies a file via java.io.FileUtils types.
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(java.io.File sourceFile, java.io.File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
