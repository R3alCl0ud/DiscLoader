package io.discloader.discloader.common.logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

public class DLPrintStream extends PrintStream {

    private final Logger logger;

    public DLPrintStream(OutputStream out, Logger logger) {
        super(out, true);

        this.logger = logger;
    }

    public void print(String string) {
        this.logger.info(string.trim());
    }

    public void println(String string) {
        this.logger.info(string);
    }
}
