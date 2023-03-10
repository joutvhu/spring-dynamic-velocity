package com.joutvhu.dynamic.velocity;

import com.joutvhu.dynamic.commons.DynamicQueryTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.ParseException;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@RequiredArgsConstructor
public class VelocityQueryTemplate implements DynamicQueryTemplate {
    private final Template template;

    public VelocityQueryTemplate(String name, String content, String encoding, RuntimeServices rs) throws ParseException {
        template = new Template();
        template.setName(name);
        template.setEncoding(encoding);
        template.setRuntimeServices(rs);
        template.setData(rs.parse(new StringReader(content), template));
        template.initDocument();
    }

    @SneakyThrows
    @Override
    public String process(Map<String, Object> params) {
        VelocityContext context = new VelocityContext(params);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
