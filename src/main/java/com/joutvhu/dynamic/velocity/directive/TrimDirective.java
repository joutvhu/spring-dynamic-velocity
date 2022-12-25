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
import org.apache.velocity.runtime.parser.node.*;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * The trim directive knows to only insert the prefix and suffix if there is any content returned by the containing tags
 * And the trim directive will remove prefixOverrides and suffixOverrides in the content
 * They are used in templates like {@code #trim ("where (", ["and ", "or "], ")", [" and", " or"])...#end}
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
        TrimRenderer.of(writer, createSymbol(context, node)).render(context, node, 4);
        return true;
    }

    private TrimSymbol createSymbol(InternalContextAdapter context, Node node) {
        return new TrimSymbol(
                getStringArgument(context, node, 0),
                getArrayArgument(context, node, 1),
                getStringArgument(context, node, 2),
                getArrayArgument(context, node, 3)
        );
    }

    private String getStringArgument(InternalContextAdapter context, Node node, int index) {
        Node argNode = node.jjtGetChild(index);
        if (argNode instanceof ASTStringLiteral) {
            Object result = argNode.value(context);
            return result.toString();
        } else if (argNode instanceof ASTReference) {
            Object result = argNode.value(context);
            return result.toString();
        } else if (argNode instanceof ASTWord) {
            if ("null".equals(argNode.getFirstTokenImage()))
                return null;
            throw new ParseErrorException("Expected argument " + (index + 1) + " of #trim to be a string");
        } else {
            throw new ParseErrorException("Expected argument " + (index + 1) + " of #trim to be a string");
        }
    }

    private List<String> getArrayArgument(InternalContextAdapter context, Node node, int index) {
        Node argNode = node.jjtGetChild(index);
        List<String> result = new ArrayList<>();
        if (argNode instanceof ASTObjectArray) {
            for (int i = 0, len = argNode.jjtGetNumChildren(); i < len; i++) {
                 String item = getStringArgument(context, argNode, i);
                 if (item != null) {
                     result.add(item);
                 }
            }
        } else {
            throw new ParseErrorException("Expected argument " + (index + 1) + " of #trim to be a array");
        }
        return result;
    }

    @Override
    public void checkArgs(ArrayList<Integer> argtypes, Token t, String templateName) throws ParseException {
        if (argtypes.size() < 4) {
            throw new MacroParseException("Too few arguments to the #trim directive", templateName, t);
        } else if (argtypes.get(0) != ParserTreeConstants.JJTSTRINGLITERAL &&
                argtypes.get(0) != ParserTreeConstants.JJTREFERENCE &&
                argtypes.get(0) != ParserTreeConstants.JJTWORD) {
            throw new MacroParseException("Expected argument 1 of #trim to be a string", templateName, t);
        } else if (argtypes.get(1) != ParserTreeConstants.JJTOBJECTARRAY &&
                argtypes.get(0) != ParserTreeConstants.JJTREFERENCE) {
            throw new MacroParseException("Expected argument 2 of #trim to be a array", templateName, t);
        } else if (argtypes.get(2) != ParserTreeConstants.JJTSTRINGLITERAL &&
                argtypes.get(0) != ParserTreeConstants.JJTREFERENCE &&
                argtypes.get(0) != ParserTreeConstants.JJTWORD) {
            throw new MacroParseException("Expected argument 3 of #trim to be a string", templateName, t);
        } else if (argtypes.get(3) != ParserTreeConstants.JJTOBJECTARRAY &&
                argtypes.get(0) != ParserTreeConstants.JJTREFERENCE) {
            throw new MacroParseException("Expected argument 4 of #trim to be a array", templateName, t);
        }
    }
}
