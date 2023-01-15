package com.joutvhu.dynamic.velocity;

import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

class DirectiveTest {
    @ParameterizedTest
    @CsvSource({
            "'where', '#where () abcd #end', ' where abcd '",
            "'where', '#where and (abcd) or#end', ' where (abcd) '",
            "'where', '#where \nor\n   abcd  \nand\n #end', ' where abcd '",
            "'where', '#where \nOR\n   abcd  \nAND\n #end', ' where abcd '",
            "'where', '#where OR (a = ''0'' OR b = ''Y'') AND c is not null AND #end', ' where (a = ''0'' OR b = ''Y'') AND c is not null '",
    })
    void testWhereDirectives(String name, String source, String expected) throws ParseException {
        RuntimeInstance cfg = VelocityTemplateConfiguration
                .instanceWithDefault()
                .configuration();
        VelocityQueryTemplate template = new VelocityQueryTemplate(name, source, "UTF-8", cfg);
        String queryString = template.process(new HashMap<>());
        Assertions.assertEquals(expected, queryString);
    }

    @ParameterizedTest
    @CsvSource({
            "'set', '#set ,abcd, #end', ' set abcd '",
            "'set', '#set\n , abcd ,#end', ' set abcd '",
            "'set', '#set , a = 1 , b = 2, c = 3, #end', ' set a = 1 , b = 2, c = 3 '",
    })
    void testSetDirective(String name, String source, String expected) throws ParseException {
        RuntimeInstance cfg = VelocityTemplateConfiguration
                .instanceWithDefault()
                .configuration();
        VelocityQueryTemplate template = new VelocityQueryTemplate(name, source, "UTF-8", cfg);
        String queryString = template.process(new HashMap<>());
        Assertions.assertEquals(expected, queryString);
    }

    @ParameterizedTest
    @CsvSource({
            "'trim', '#trim (\"69\", [\"a\"], \"e\", [\"b\"]) a abcd b #end', ' 69 abcd e '",
            "'trim', '#trim (null, [\"s\"], \"e\", [\"b\"]) \ns abcd b\n#end', ' abcd e '",
            "'trim', '#trim (\"set\", [\",\"], null, [\",\"]), a = ''Y'', b = ''N'',  #end', ' set a = ''Y'', b = ''N'' '",
            "'trim', '#trim (\"where\", [\"and\", \"or\"], null, [\"and\", \"or\"]) and (a = ''0'' or b = ''Y'') and c is not null or #end', ' where (a = ''0'' or b = ''Y'') and c is not null '",
    })
    void testTrimDirective(String name, String source, String expected) throws ParseException {
        RuntimeInstance cfg = VelocityTemplateConfiguration
                .instanceWithDefault()
                .configuration();
        VelocityQueryTemplate template = new VelocityQueryTemplate(name, source, "UTF-8", cfg);
        String queryString = template.process(new HashMap<>());
        Assertions.assertEquals(expected, queryString);
    }
}
