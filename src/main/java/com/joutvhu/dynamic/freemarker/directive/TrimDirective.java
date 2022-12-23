package com.joutvhu.dynamic.freemarker.directive;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * The trim directive knows to only insert the prefix and suffix if there is any content returned by the containing tags
 * And the trim directive will remove prefixOverrides and suffixOverrides in the content
 * They are used in templates like {@code <@trim prefix="where (" prefixOverrides=["and ", "or "] suffix=")" suffixOverrides=[" and", " or"]>...</@trim>}
 *
 * @author Giao Ho
 * @since 1.0.0
 */
public class TrimDirective extends Directive {
    @Override
    public String getName() {
        return "trim";
    }

    @Override
    public int getType() {
        return BLOCK;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        TrimWriter.of(writer, createSymbol(context)).render(context, node);
        return true;
    }

    private TrimSymbol createSymbol(InternalContextAdapter context) {
        return new TrimSymbol(
                context.get("prefix").toString(),
                (List<String>) context.get("prefixOverrides"),
                context.get("suffix").toString(),
                (List<String>) context.get("suffixOverrides")
        );
    }
}
