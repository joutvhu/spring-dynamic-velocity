package com.joutvhu.dynamic.velocity.directive;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import com.joutvhu.dynamic.velocity.io.TrimRenderer;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.MacroParseException;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * The where directive knows to only insert "WHERE" if there is any content returned by the containing tags,
 * If that content begins or ends with "AND" or "OR", it knows to strip it off.
 * They are used in templates like {@code #where...#end}
 *
 * @author Giao Ho
 * @since 1.0.0
 */
public class WhereDirective extends Directive {
    private static final TrimSymbol symbol = new TrimSymbol(
            "where", TrimSymbol.getOverrides(true, "and", "or"),
            null, TrimSymbol.getOverrides(false, "and", "or"));

    @Override
    public String getName() {
        return "where";
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

    @Override
    public void checkArgs(ArrayList<Integer> argtypes, Token t, String templateName) throws ParseException {
        if (argtypes.size() > 0) {
            throw new MacroParseException("The #where directive requires no arguments", templateName, t);
        }
    }
}
