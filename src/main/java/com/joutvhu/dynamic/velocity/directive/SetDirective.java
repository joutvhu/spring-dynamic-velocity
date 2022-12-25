package com.joutvhu.dynamic.velocity.directive;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import com.joutvhu.dynamic.velocity.io.TrimRenderer;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * The set directive knows to only insert "SET" if there is any content returned by the containing tags,
 * If that content begins or ends with ",", it knows to strip it off.
 * They are used in templates like {@code #set...#end}
 *
 * @author Giao Ho
 * @since 1.0.0
 */
public class SetDirective extends Directive {
    private static final TrimSymbol symbol = new TrimSymbol("set", null, ",");

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public int getType() {
        return BLOCK;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        TrimRenderer.of(writer, symbol).render(context, node);
        return true;
    }
}
