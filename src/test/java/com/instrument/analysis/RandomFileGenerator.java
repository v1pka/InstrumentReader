package com.instrument.analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ipopkov on 06/06/15.
 */
public class RandomFileGenerator {
    public static File generateHugeTestFile(File file, Long size) {
        List<String> lines = new ArrayList<>();
        lines.add("INSTRUMENT1,09-May-2001,1.11");
        lines.add("INSTRUMENT2,14-Nov-2002,2.22");
        lines.add("INSTRUMENT3,19-Nov-2014,3.33");
        lines.add("INSTRUMENT4,10-May-2004,4.44");

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsolutePath(), true)))) {
            while (file.length() < size) {
                lines.forEach(s -> out.println(s));
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        return file;
    }
}
