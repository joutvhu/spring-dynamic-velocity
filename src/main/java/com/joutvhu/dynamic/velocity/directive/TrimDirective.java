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
import org.apache.velocity.runtime.parser.node.ASTObjectArray;
import org.apache.velocity.runtime.parser.node.ASTStringLiteral;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.ParserTreeConstants;

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
            Object result = ((ASTStringLiteral) argNode).value(context);
            return result.toString();
        }
        return null;
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
        }
        return result;
    }

    @Override
    public void checkArgs(ArrayList<Integer> argtypes, Token t, String templateName) throws ParseException {
        if (argtypes.size() < 4) {
            throw new MacroParseException("Too few arguments to the #trim directive", templateName, t);
        } else if (argtypes.get(0) != ParserTreeConstants.JJTSTRINGLITERAL) {
            throw new MacroParseException("Expected string 'in' at argument position 1 in #trim", templateName, t);
        } else if (argtypes.get(1) != ParserTreeConstants.JJTOBJECTARRAY) {
            throw new MacroParseException("Expected array 'in' at argument position 2 in #trim", templateName, t);
        } else if (argtypes.get(2) != ParserTreeConstants.JJTSTRINGLITERAL) {
            throw new MacroParseException("Expected string 'in' at argument position 3 in #trim", templateName, t);
        } else if (argtypes.get(3) != ParserTreeConstants.JJTOBJECTARRAY) {
            throw new MacroParseException("Expected array 'in' at argument position 4 in #trim", templateName, t);
        }
    }
}
