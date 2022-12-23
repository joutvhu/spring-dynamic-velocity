package com.joutvhu.dynamic.freemarker;

import com.joutvhu.dynamic.freemarker.directive.WhereDirective;
import org.apache.velocity.runtime.RuntimeInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

class DirectiveTest {
    @ParameterizedTest
    @CsvSource({
            "'where', '#where \nand (abcd) or\n#end', ' where (abcd) '",
            "'where', '#where \nor\n   abcd  \nand\n \n#end', ' where abcd '",
            "'where', '#where \n\nOR\n   abcd  \nAND\n \n#end', ' where abcd '",
            "'set', '#set \n,abcd, \n#end', ' set abcd '",
            "'set', '#set \n\n , abcd ,\n#end', ' set abcd '",
            "'trim', '#trim (prefix=\"69\" prefixOverrides=[\"a\"] suffix=\"e\" suffixOverrides=[\"b\"]) \na abcd b\n#end', ' 69 abcd e '",
    })
    void testDirectives(String name, String source, String expected) throws Exception {
        RuntimeInstance cfg = VelocityTemplateConfiguration
                .instanceWithDefault()
                .configuration();
        VelocityQueryTemplate template = new VelocityQueryTemplate(name, source, cfg);
        String queryString = template.process(new HashMap<>());
        Assertions.assertEquals(expected, queryString);
    }

    @ParameterizedTest
    @CsvSource({
            "'where', '#where ()\nabcd\n#end', ' where abcd '",
    })
    void testWhereDirectives(String name, String source, String expected) throws Exception {
        RuntimeInstance cfg = VelocityTemplateConfiguration
                .instance()
                .registerDirective(new WhereDirective())
                .configuration();
        VelocityQueryTemplate template = new VelocityQueryTemplate(name, source, cfg);
        String queryString = template.process(new HashMap<>());
        Assertions.assertEquals(expected, queryString);
    }
}
