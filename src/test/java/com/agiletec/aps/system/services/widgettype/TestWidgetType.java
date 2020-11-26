package com.agiletec.aps.system.services.widgettype;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.ent.exception.EntException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class TestWidgetType extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testEqualsAndHashcode() throws EntException {
        String widgetTypeCode1 = "testWidgetType1";
        String widgetTypeCode2 = "testWidgetType2";
        String widgetCategory1 = "test";
        String widgetCategory2 = "category";
        ApsProperties titles = new ApsProperties();
        titles.put("it", "Titolo in ITA");
        titles.put("en", "Title in ENG");
        ApsProperties config = new ApsProperties();
        WidgetType widgetType1 = createWidgetType(widgetTypeCode1, titles, config, true, widgetCategory1);
        WidgetType widgetType2 = createWidgetType(widgetTypeCode1, titles, config, true, widgetCategory1);
        WidgetType widgetType3 = createWidgetType(widgetTypeCode2, titles, config, false, widgetCategory2);
        assertThat(widgetType1.equals(widgetType2), is(true));
        assertThat(widgetType2.equals(widgetType1), is(true));
        assertThat(widgetType1.equals(widgetType3), is(false));
        assertThat(widgetType3.equals(widgetType1), is(false));
        assertThat(widgetType1.hashCode(), is(not(widgetType3.hashCode())));
        assertThat(widgetType1.hashCode(), is(widgetType2.hashCode()));
    }

    private WidgetType createWidgetType(String code, ApsProperties titles, ApsProperties config, Boolean readonlyPageWidgetConfig, String widgetCategory) {
        WidgetType type = new WidgetType();
        type.setCode(code);
        type.setTitles(titles);
        type.setConfig(config);
        type.setWidgetCategory(widgetCategory);
        type.setReadonlyPageWidgetConfig(readonlyPageWidgetConfig);
        return type;
    }
}
