package com.joutvhu.dynamic.velocity.io;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class TrimRenderer {
    private final Writer out;
    private final TrimSymbol symbol;
    private final StringWriter writer = new StringWriter();

    public static TrimRenderer of(Writer out, TrimSymbol symbols) {
        return new TrimRenderer(out, symbols);
    }

    public TrimRenderer(Writer out, TrimSymbol symbol) {
        this.out = out;
        this.symbol = symbol;
    }

    public void afterWrite() throws IOException {
        String content = writer.toString();
        content = symbol.process(content);
        out.write(content);
    }

    public void render(InternalContextAdapter context, Node block) throws IOException {
        render(context, block, 0);
    }

    public void render(InternalContextAdapter context, Node block, int from) throws IOException {
        for (int i = from, len = block.jjtGetNumChildren(); i < len; i++) {
            block.jjtGetChild(i).render(context, writer);
        }
        afterWrite();
    }
}
