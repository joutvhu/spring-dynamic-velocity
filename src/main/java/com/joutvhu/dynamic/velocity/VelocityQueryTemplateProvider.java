package com.joutvhu.dynamic.velocity;

import com.joutvhu.dynamic.commons.DynamicQueryTemplate;
import com.joutvhu.dynamic.commons.DynamicQueryTemplateProvider;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Velocity dynamic query template provider.
 *
 * @author Giao Ho
 * @since 1.0.0
 */
@NoArgsConstructor
public class VelocityQueryTemplateProvider extends DynamicQueryTemplateProvider {
    private static final Log log = LogFactory.getLog(VelocityQueryTemplateProvider.class);

    private final Map<String, DynamicQueryTemplate> templateCache = new HashMap<>();
    private VelocityTemplateConfiguration configuration;
    private RuntimeInstance cfg;

    public void setConfiguration(VelocityTemplateConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public DynamicQueryTemplate createTemplate(String name, String content) {
        try {
            VelocityQueryTemplate template = new VelocityQueryTemplate(name, content, encoding, cfg);
            return template;
        } catch (ParseException e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public DynamicQueryTemplate findTemplate(String name) {
        return templateCache.get(name);
    }

    @Override
    protected void putTemplate(String name, String content) {
        if (templateCache.containsKey(name))
            log.warn("Found duplicate template key, will replace the value, key: " + name);
        templateCache.put(name, createTemplate(name, content));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configuration == null)
            configuration = VelocityTemplateConfiguration.instanceWithDefault();
        cfg = configuration.configuration();
        super.afterPropertiesSet();
    }
}
