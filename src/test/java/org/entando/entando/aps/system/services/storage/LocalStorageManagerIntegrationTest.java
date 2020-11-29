/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.apache.commons.lang3.Functions;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.ent.exception.EntRuntimeException;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.mockito.Mockito;

/**
 * @author S.Loru - E.Santoboni
 */
public class LocalStorageManagerIntegrationTest extends BaseTestCase {

    private static final EntLogger logger = EntLogFactory.getSanitizedLogger(LocalStorageManagerIntegrationTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testInitialize() {
        assertNotNull(localStorageManager);
    }

    public void testStorageFileList() throws Throwable {
        String[] filenames = localStorageManager.listFile("", false);
        assertEquals(1, filenames.length);
        assertEquals("entando_logo.jpg", filenames[0]);
        filenames = localStorageManager.listFile("conf" + File.separator, false);
        assertEquals(3, filenames.length);
        for (int i = 0; i < filenames.length; i++) {
            String filename = filenames[i];
            assertTrue(filename.equals("contextTestParams.properties") || filename.equals("systemParams.properties")
                    || filename.equals("security.properties"));
        }
    }

    public void testStorageDirectoryList() throws Throwable {
        String[] directoryNames = localStorageManager.listDirectory("", false);
        assertTrue(directoryNames.length >= 1);
        List<String> list = Arrays.asList(directoryNames);
        assertTrue(list.contains("conf"));

        directoryNames = localStorageManager.listDirectory("conf" + File.separator, false);
        assertEquals(0, directoryNames.length);
    }

    public void testListAttributes() throws Throwable {
        BasicFileAttributeView[] fileAttributes = localStorageManager.listAttributes("", false);
        boolean containsConf = false;
        boolean prevDirectory = true;
        String prevName = null;
        for (int i = 0; i < fileAttributes.length; i++) {
            BasicFileAttributeView bfav = fileAttributes[i];
            if (!prevDirectory && bfav.isDirectory()) {
                fail();
            }
            if (bfav.isDirectory() && bfav.getName().equals("conf")) {
                containsConf = true;
            }
            if ((bfav.isDirectory() == prevDirectory) && null != prevName) {
                assertTrue(bfav.getName().compareTo(prevName) > 0);
            }
            prevName = bfav.getName();
            prevDirectory = bfav.isDirectory();
        }
        assertTrue(containsConf);
    }

    public void testListAttributes_2() throws Throwable {
        BasicFileAttributeView[] fileAttributes = localStorageManager.listAttributes("conf" + File.separator, false);
        assertEquals(3, fileAttributes.length);
        int dirCounter = 0;
        int fileCounter = 0;
        for (int i = 0; i < fileAttributes.length; i++) {
            BasicFileAttributeView bfav = fileAttributes[i];
            if (bfav.isDirectory()) {
                dirCounter++;
            } else {
                fileCounter++;
            }
        }
        assertEquals(0, dirCounter);
        assertEquals(3, fileCounter);
    }

    public void testListAttributes_3() throws Throwable {
        // Non existent directory
        BasicFileAttributeView[] fileAttributes =
                localStorageManager.listAttributes("non-existent" + File.separator, false);
        assertEquals(0, fileAttributes.length);

        // Non-directory directory
        fileAttributes =
                localStorageManager.listAttributes("conf/security.properties" + File.separator, false);
        assertEquals(0, fileAttributes.length);
    }

    public void testGetStream_ShouldBlockPathTraversal() throws Throwable {
        String testFilePath = "../testfolder/test.txt";

        try {
            localStorageManager.getStream(testFilePath, false);
        } catch (EntRuntimeException e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.startsWith("Path validation failed"));
        } catch (Throwable t) {
            fail("Shouldn't reach this point");
        }
    }

    public void testSaveEditDeleteFile() throws Throwable {
        String testFilePath = "testfolder/test.txt";
        InputStream stream = localStorageManager.getStream(testFilePath, false);
        assertNull(stream);
        try {
            String content = "Content of new text file";
            localStorageManager.saveFile(testFilePath, false, new ByteArrayInputStream(content.getBytes()));
            stream = localStorageManager.getStream(testFilePath, false);
            assertNotNull(stream);
            String extractedString = IOUtils.toString(stream, "UTF-8");
            stream.close();
            assertEquals(content, extractedString);
            String newContent = "This is the new content of text file";
            localStorageManager.editFile(testFilePath, false, new ByteArrayInputStream(newContent.getBytes()));
            stream = localStorageManager.getStream(testFilePath, false);
            String extractedNewString = IOUtils.toString(stream, "UTF-8");
            stream.close();
            assertEquals(newContent, extractedNewString);
            String readfileAfterWriteBackup = localStorageManager.readFile(testFilePath, false);
            assertEquals(extractedNewString, readfileAfterWriteBackup);
            boolean deleted = localStorageManager.deleteFile(testFilePath, false);
            assertTrue(deleted);
            stream = localStorageManager.getStream(testFilePath, false);
            assertNull(stream);
        } catch (Throwable t) {
            throw t;
        } finally {
            if (null != stream) {
                stream.close();
            }
            localStorageManager.deleteDirectory("testfolder/", false);
            InputStream streamBis = localStorageManager.getStream(testFilePath, false);
            assertNull(streamBis);
        }
        // file not found case
        assertFalse(
                localStorageManager.deleteFile("non-existent", false)
        );
    }

    public void testCreateDeleteFile_ShouldBlockPathTraversals() throws Throwable {
        String testFilePath = "../../testfolder/test.txt";
        String content = "Content of new text file";

        try {
            localStorageManager.saveFile(testFilePath, false, new ByteArrayInputStream(content.getBytes()));
        } catch (EntRuntimeException e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.startsWith("Path validation failed"));
        } catch (Throwable t) {
            fail("Shouldn't reach this point");
        }

        try {
            localStorageManager.deleteFile(testFilePath, false);
            fail("Shouldn't reach this point");
        } catch (EntRuntimeException e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.startsWith("Path validation failed"));
        } catch (Throwable t) {
            fail("Shouldn't reach this point");
        }
    }

    public void testCreateDeleteDir() throws EntException {
        String directoryName = "testfolder";
        String subDirectoryName = "subfolder";
        assertFalse(localStorageManager.exists(directoryName, false));
        try {
            localStorageManager.createDirectory(directoryName + File.separator + subDirectoryName, false);
            assertTrue(localStorageManager.exists(directoryName, false));
            String[] listDirectory = localStorageManager.listDirectory(directoryName, false);
            assertEquals(1, listDirectory.length);
            listDirectory = localStorageManager.listDirectory(directoryName + File.separator + subDirectoryName, false);
            assertEquals(0, listDirectory.length);
        } finally {
            localStorageManager.deleteDirectory(directoryName, false);
            assertFalse(localStorageManager.exists(directoryName, false));
        }
    }

    public void testCreateDeleteDir_ShouldHandleFailureCases() throws EntException {
        String baseFolder = "non-existent";
        String endingFolder = "dir-to-create";
        String fullPath = baseFolder + File.separator + endingFolder;
        Functions.FailableBiConsumer<String, Boolean, EntException> assertExists = (p, exp) ->
                assertEquals(exp, (Boolean) localStorageManager.exists(p, false));

        // Simple fail by duplication or not found
        try {
            localStorageManager.createDirectory(fullPath, false);
            assertExists.accept(fullPath, true);
            localStorageManager.createDirectory(fullPath, false);
            assertExists.accept(fullPath, true);
            localStorageManager.deleteDirectory(fullPath, false);
            assertExists.accept(fullPath, false);
        } finally {
            localStorageManager.deleteDirectory(baseFolder, false);
            assertFalse(localStorageManager.exists(baseFolder, false));
        }
    }

    public void testCreateDeleteDirectory_ShouldBlockPathTraversals() throws Throwable {
        try {
            localStorageManager.createDirectory("/../../../dev/mydir", false);
            fail("Shouldn't reach this point");
        } catch (EntRuntimeException e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.startsWith("Path validation failed"));
        } catch (Throwable t) {
            fail("Shouldn't reach this point");
        }

        try {
            localStorageManager.deleteDirectory("/../../../dev/mydir", false);
        } catch (EntRuntimeException e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.startsWith("Path validation failed"));
        } catch (Throwable t) {
            fail("Shouldn't reach this point");
        }

        try {
            localStorageManager.createDirectory("target/mydir", false);
        } catch (Throwable t) {
            fail("Shouldn't reach this point");
        } finally {
            localStorageManager.deleteDirectory("target/mydir", false);
            localStorageManager.deleteDirectory("target", false);
        }
    }


    private void init() throws Exception {
        try {
            localStorageManager = (IStorageManager) this.getApplicationContext().getBean(SystemConstants.STORAGE_MANAGER);
        } catch (Throwable t) {
            logger.error("error on init", t);
        }
    }

    private IStorageManager localStorageManager;

}
