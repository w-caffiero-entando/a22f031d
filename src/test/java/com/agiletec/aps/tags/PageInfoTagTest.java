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

package com.agiletec.aps.tags;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.servlet.jsp.JspWriter;

import static org.hamcrest.CoreMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PageInfoTagTest {

    private static final String TEST_URL_1 = "http://www.example.com?param1=1&param2=xxx#a_fragment";
    private static final String TEST_URL_1_EXPECTED = "http://www.example.com?param1=1&amp;param2=xxx#a_fragment";
    private static final String TEST_URL_2 = "<script>something bad</script>";
    private static final String TEST_URL_2_EXPECTED = "&lt;script&gt;something bad&lt;/script&gt;";
    private static final String TEST_URL_3 = "\"<script>something bad</script>\"&'\"'<script>something</script>'";
    private static final String TEST_URL_3_EXPECTED =
            "\"&lt;script&gt;something bad&lt;/script&gt;\"&amp;'\"'&lt;script&gt;something&lt;/script&gt;'";
    private static final String TEST_URL_4 = "" +
            "http://www.example.com?\"&p1=<script>something bad</script>\"" +
            "&p2=\"<script>something bad</script>\"" +
            "&p3='script>something bad</script>'\"'";
    private static final String TEST_URL_4_EXPECTED = "" +
            "http://www.example.com?\"&amp;p1=&lt;script&gt;something bad&lt;/script&gt;\"&amp;p2=\"&lt;script&gt;" +
            "something bad&lt;/script&gt;\"&amp;p3='script&gt;something bad&lt;/script&gt;'\"'";
    private static final String TEST_URL_5 = "javascript:<script>something bad</script>";
    private static final String TEST_URL_5_EXPECTED = "javascript:&lt;script&gt;something bad&lt;/script&gt;";

    @Test
    public void testSantitizationOfUrisAsHtmlContent() {
        Assert.assertThat(
                PageInfoTag.mkSafeForHtmlContent(TEST_URL_1),
                equalTo(TEST_URL_1_EXPECTED)
        );

        Assert.assertThat(
                PageInfoTag.mkSafeForHtmlContent(TEST_URL_2),
                allOf(
                        not(containsString("<")),
                        not(containsString(">")),
                        equalTo(TEST_URL_2_EXPECTED)
                )
        );

        Assert.assertThat(
                PageInfoTag.mkSafeForHtmlContent(TEST_URL_3),
                allOf(
                        not(containsString("<")),
                        not(containsString(">")),
                        equalTo(TEST_URL_3_EXPECTED)
                )
        );

        Assert.assertThat(
                PageInfoTag.mkSafeForHtmlContent(TEST_URL_4),
                allOf(
                        not(containsString("<")),
                        not(containsString(">")),
                        equalTo(TEST_URL_4_EXPECTED)
                )
        );
    }
}