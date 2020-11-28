/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
 *
 */

package org.entando.entando.aps.system.services.storage;

import junit.framework.TestCase;

import java.io.IOException;

public class LocalStorageManagerTest extends TestCase {

    public void testDoesPathContainsPath() throws IOException {
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc/", "/etc/x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc//x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etcx/x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc /x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/./x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc//x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/zz/../x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/zz/./../x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/../etc/x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/../etc /x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/../etc/ x")
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/...")   // "..." is a proper fil/dir name
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/.../etc/x")   // "..." is a proper fil/dir name
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/../../etc/x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/../../z/etc/x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etc/../..//etc/x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etx/x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/etc", "/etx/../x")
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/a/b/c", "/a/b/c", false)
        );
        assertFalse(
                LocalStorageManager.doesPathContainsPath("/a/b/c/", "/a/b/c", false)
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/a/b/c", "/a/b/c", true)
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/a/b/c/", "/a/b/c", true)
        );
        assertTrue(
                LocalStorageManager.doesPathContainsPath("/a/b/../b/c/", "/a/b/c", true)
        );
    }

    public void testIsSamePath() {
        assertTrue(
            LocalStorageManager.isSamePath("a/b/c","a/b/c/")
        );
        assertTrue(
            LocalStorageManager.isSamePath("a/b/../b/c","a/b/c/")
        );
    }

    public void testIsSamePathString() {
        assertTrue(
            LocalStorageManager.isSamePathString("a/b/c","a/b/c/")
        );
        assertFalse(
            LocalStorageManager.isSamePathString("a/b/../b/c","a/b/c/")
        );
    }
}