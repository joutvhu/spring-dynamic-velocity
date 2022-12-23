package com.joutvhu.dynamic.freemarker;

import com.joutvhu.dynamic.freemarker.directive.SetDirective;
import com.joutvhu.dynamic.freemarker.directive.TrimDirective;
import com.joutvhu.dynamic.freemarker.directive.WhereDirective;
import org.apache.velocity.runtime.RuntimeInstance;

/**
 * Freemarker configuration builder.
 *
 * @author Giao Ho
 * @since 1.0.0
 */
public class VelocityTemplateConfiguration {
    public RuntimeInstance ri;

    protected VelocityTemplateConfiguration() {
        this.ri = new RuntimeInstance();
    }

    public static VelocityTemplateConfiguration instance() {
        return new VelocityTemplateConfiguration();
    }

    public static VelocityTemplateConfiguration instanceWithDefault() {
        return instance().applyDefault();
    }

    public VelocityTemplateConfiguration applyDefault() {
        ri.addDirective(new TrimDirective());
        ri.addDirective(new SetDirective());
        ri.addDirective(new WhereDirective());

        return this;
    }

    public RuntimeInstance configuration() {
        return ri;
    }
}
