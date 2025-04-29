package ru.khav.ProjectNIC.utill;

import java.io.File;

public class FileFilter implements java.io.FileFilter {

    private String[] acceptedExtensions;

    FileFilter(String exts) {
        acceptedExtensions = exts.split(",");
    }

    @Override
    public boolean accept(File pathname) {
        if (!pathname.isFile())
            return false;
        String extension = getExtension(pathname);
        for (String e : acceptedExtensions) {
            if (e.equalsIgnoreCase(extension))
                return true;
        }
        return false;
    }

    private String getExtension(File pathname) {
        String filename = pathname.getPath();
        int i = filename.lastIndexOf('.');
        if ((i > 0) && (i < filename.length() - 1)) {
            return filename.substring(i + 1).toLowerCase();
        }
        return null;
    }
}
