/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.util.crypto;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.entando.entando.TestEntandoJndiUtils;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath*:spring/testpropertyPlaceholder.xml",
    "classpath*:spring/baseSystemConfig.xml",
    "classpath*:spring/aps/**/**.xml",
    "classpath*:spring/plugins/**/aps/**/**.xml",
    "classpath*:spring/web/**.xml"
})
@WebAppConfiguration(value = "")
public class CompatiblePasswordEncoderTest {

    @BeforeClass
    public static void setup() throws Exception {
        TestEntandoJndiUtils.setupJndi();
    }

    private static final String SECRET = "my secret";

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    private Argon2PasswordEncoder argon2Encoder;

    @Autowired
    private CompatiblePasswordEncoder passwordEncoder;

    @Test
    public void testBCrypt() {
        testMatches("{bcrypt}" + bcryptEncoder.encode(SECRET), SECRET);
    }

    @Test
    public void testBCryptCommonTestPwd() {
        // Test the passwords inserted via SQL (look for "{DIRECT USERS INSERT SQL}" in code)
        testMatches("{bcrypt}$2a$10$TMRaAmZE4w5LEeELdmpJguuSuJc2D9hUelMGmsJyK35K3PBiePqXu", "adminadmin");
        testMatches("{bcrypt}$2a$10$CkUsRinB3JkFlRE4M.FOg.XrUpYX5HySBxpEasdex7L5bh05RnX.G", "editoreditor");
        testMatches("{bcrypt}$2a$10$0Idom7PIOI4YuKzyhqDJpe3Z/0N0M0FQEvKtrSOjgF71Hkx5mKhlq", "approverapprover");
    }

    @Test
    public void testArgon2() throws Exception {
        testMatches(argon2Encoder.encode(SECRET), SECRET);
    }

    private void testMatches(String encodedPwd, String plainPwd) {
        assertThat(encodedPwd).isNotEqualTo(plainPwd);
        assertTrue(passwordEncoder.matches(plainPwd, encodedPwd));
    }
}
