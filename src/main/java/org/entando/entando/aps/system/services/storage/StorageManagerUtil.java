/*
 * Copyright 2016-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.storage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.entando.entando.ent.util.EntLogging.EntLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.*;
import org.entando.entando.ent.exception.EntRuntimeException;
import org.entando.entando.ent.util.EntSanitization;

public final class StorageManagerUtil {

    private StorageManagerUtil() {
        // Utility class, not to be instantiated
    }

    private static final EntLogger logger = EntLogFactory.getSanitizedLogger(StorageManagerUtil.class);

    private static final String REGEXP_FILE_EXTENSION = "([\\w|\\-]+?$)";
    private static final String REGEXP_FILE_BASENAME = "\\A(?!(?:COM[0-9]|CON|LPT[0-9]|NUL|PRN|AUX|com[0-9]|con|lpt[0-9]|nul|prn|aux)|[\\s\\.])[^\\\\/:*\"?<>|]{1,254}\\z";
    private static final String REGEXP_DIR = "(^[\\w|\\.|\\-|\\_|/| ]+?)";

    public static String mustBeValidFilename(String fullname) {
        if (isValidFilename(fullname)) {
            return fullname;
        } else {
            throw new EntRuntimeException("Invalid filename detected: \"" +
                    EntSanitization.fixJavaSecS5145(fullname) + "\"");
        }
    }

    public static String mustBeValidDirName(String fullname) {
        if (isValidDirName(fullname)) {
            return fullname;
        } else {
            throw new EntRuntimeException("Invalid directory name detected: \"" +
                    EntSanitization.fixJavaSecS5145(fullname) + "\"");
        }
    }

    public static boolean isValidFilename(String fullname) {
        if (StringUtils.isBlank(fullname)) {
            return false;
        }
        if (fullname.endsWith(".")) {
            return false;
        }
        if (!isValidPath(fullname)) {
            return false;
        }
        String basename = FilenameUtils.getBaseName(fullname);
        String extension = FilenameUtils.getExtension(fullname);
        return isValidFilename(basename, extension);
    }

    public static boolean isValidFilename(String basename, String extension) {
        if (StringUtils.isBlank(basename)) {
            return false;
        }
        if (!isValidPath(basename)) {
            return false;
        }
        if (!isValidFilenameNoExtension(basename)) {
            return false;
        }
        if (StringUtils.isNotBlank(extension)) {
            return isValidExtension(extension);
        }
        return true;
    }

    public static boolean isValidFilenameNoExtension(String basename) {
        if (StringUtils.isBlank(basename)) {
            return false;
        }
        if (!isValidPath(basename)) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEXP_FILE_BASENAME);
        Matcher matcher = pattern.matcher(basename);
        return matcher.matches();
    }

    public static boolean isValidDirName(String basename) {
        if (StringUtils.isBlank(basename)) {
            return true;
        }
        if (!isValidPath(basename)) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEXP_DIR);
        Matcher matcher = pattern.matcher(basename);
        boolean check = matcher.matches();
        if (!check) {
            return false;
        }
        return endWithParentDir(basename);
    }

    public static boolean isValidExtension(String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEXP_FILE_EXTENSION);
        Matcher matcher = pattern.matcher(extension);
        return matcher.matches();
    }

    private static boolean isValidPath(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }
        if (path.contains("../")
                || path.contains("%2e%2e%2f")
                || path.contains("..%2f")
                || path.contains(".." + File.separator)
                || path.contains("%2e%2e/")
                || path.contains("%2e%2e" + File.separator)) {
            logger.info("Attack avoided - requested path {}", path);
            return false;
        }
        return true;
    }

    private static boolean endWithParentDir(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }
        if (path.endsWith("..")
                || path.endsWith("%2e%2e")) {
            logger.info("Attack avoided - requested path {}", path);
            return false;
        }
        return true;
    }

}
