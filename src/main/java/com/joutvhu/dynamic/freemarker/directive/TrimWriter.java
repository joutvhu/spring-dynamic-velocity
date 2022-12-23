package com.joutvhu.dynamic.freemarker.directive;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

public class TrimWriter extends Writer {
    private final Writer out;
    private final TrimSymbol symbol;
    private final StringBuilder contentBuilder = new StringBuilder();

    public static TrimWriter of(Writer out, TrimSymbol symbols) {
        return new TrimWriter(out, symbols);
    }

    public TrimWriter(Writer out, TrimSymbol symbol) {
        this.out = out;
        this.symbol = symbol;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        String content = String.copyValueOf(cbuf);
        this.contentBuilder.append(content);
    }

    public void afterWrite() throws IOException {
        String content = this.contentBuilder.toString();
        content = symbol.process(content);
        out.write(content);
    }

    public void render(InternalContextAdapter context, Node block) throws IOException, MethodInvocationException, ParseErrorException, ResourceNotFoundException {
        block.render(context, this);
        this.afterWrite();
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
