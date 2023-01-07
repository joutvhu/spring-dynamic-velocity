package com.joutvhu.dynamic.velocity;

import com.joutvhu.dynamic.commons.DynamicQueryTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VelocityQueryTemplateProviderTest {
    private VelocityQueryTemplateProvider provider = new VelocityQueryTemplateProvider();

    @BeforeAll
    public void setup() throws Exception {
        provider.setConfiguration(null);
        provider.setResourceLoader(new DefaultResourceLoader());
        provider.afterPropertiesSet();
    }

    @Test
    void testCreateTemplate() {
        DynamicQueryTemplate template = provider.createTemplate("test", "select t from User t\n" +
                "#where\n" +
                "  #if($firstName != $null && $firstName != '')\n" +
                "    and t.firstName = :firstName\n" +
                "  #end\n" +
                "  #if($lastName != $null && $lastName != '')\n" +
                "    and t.lastName = :lastName\n" +
                "  #end\n" +
                "#end");
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", "A");
        String result = template.process(params);
        Assertions.assertEquals("select t from User t\n" +
                " where t.firstName = :firstName ", result);
    }

    @Test
    void testCreateTemplate_Error() {
        DynamicQueryTemplate template = provider.createTemplate("test", "select t from User t\n" +
                "#where\n" +
                "  #if($firstName != $null && $firstName != '')\n" +
                "    and t.firstName = :firstName\n" +
                "#end");
        Assertions.assertNull(template);
    }

    @Test
    void testFindTemplate_NotFound() {
        DynamicQueryTemplate template = provider.findTemplate("test");
        Assertions.assertNull(template);
    }

    @Test
    void testFindTemplate_Found() {
        DynamicQueryTemplate template = provider.findTemplate("User:findByGroup");
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> group = new HashMap<>();
        group.put("name", "GitHub");
        params.put("group", group);
        String result = template.process(params);
        Assertions.assertEquals("select t from User t  where t.groupId = :#{#group.id}  ", result);
    }
}
