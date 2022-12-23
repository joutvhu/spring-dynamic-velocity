package com.joutvhu.dynamic.velocity;

import com.joutvhu.dynamic.velocity.directive.SetDirective;
import com.joutvhu.dynamic.velocity.directive.TrimDirective;
import com.joutvhu.dynamic.velocity.directive.WhereDirective;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.directive.Directive;

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

    public VelocityTemplateConfiguration registerDirective(Directive directive) {
        ri.addDirective(directive);
        return this;
    }

    public RuntimeInstance configuration() {
        return ri;
    }
}
