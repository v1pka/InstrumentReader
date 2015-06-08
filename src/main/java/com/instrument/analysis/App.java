package com.instrument.analysis;


/**
 * Main application
 */
public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("Must be exactly one argument - path to the file ");
        }
        String path = args[0];
        new InstrumentAnalysis(path).process();
    }
}
