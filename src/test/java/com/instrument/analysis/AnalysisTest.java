package com.instrument.analysis;

import com.instrument.instrument.AbstractInstrument;
import com.instrument.instrument.InstrumentFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AnalysisTest
{

    @Rule
    public TestName testName = new TestName();

    private String pathToHugeFile;

    @Before
    public void setUp() throws IOException {
        InstrumentFactory.getInstance().getUsedInstruments().clear();
        if(testName.getMethodName().equals("testHugeFileOutOfMemory")){
            System.out.println("Starting to generate file.");
            File tempHugeFile = File.createTempFile("hugeInput", "txt");
            Long size = 1024L * 1024L * 1024L;
            File hugeFile = RandomFileGenerator.generateHugeTestFile(tempHugeFile, size);
            pathToHugeFile = hugeFile.getAbsolutePath();
            System.out.println("Finished.");
        }
    }

    @org.junit.Test
    public void testSimple() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("INSTRUMENT1,09-May-2001,1");
        lines.add("INSTRUMENT2,14-Nov-2002,1");
        lines.add("INSTRUMENT3,19-Nov-2014,1");
        lines.add("INSTRUMENT4,10-May-2004,1");

        Map<String, AbstractInstrument> instruments = runAnalysis(lines);

        assertTrue(instruments.size() == 4);
        double testValue = 1;
        for(AbstractInstrument instrument : instruments.values()){
            assertTrue(instrument.calculateMean() == testValue);
        }
    }

    @org.junit.Test
    public void testIfSomeInstrumentExistsButAllWasFilteredByDate() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("INSTRUMENT1,12-May-2001,1");
        lines.add("INSTRUMENT2,16-Nov-2002,1");
        lines.add("INSTRUMENT3,15-Nov-2014,1");
        lines.add("INSTRUMENT4,09-May-2004,1");

        Map<String, AbstractInstrument> instruments = runAnalysis(lines);

        assertTrue(instruments.size() == 4);
        double testValue = 0;
        for(AbstractInstrument instrument : instruments.values()){
            assertTrue(instrument.calculateMean() == testValue);
        }
    }

    @org.junit.Test
    public void testIncorrectLine() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("INSTRUMENT1111");
        lines.add("INSTRUMENT222,11");

        Map<String, AbstractInstrument> instruments = runAnalysis(lines);
        assertTrue(instruments.isEmpty());
    }

    @Ignore
    @org.junit.Test
    public void testHugeFileOutOfMemory() {
        //It is long running test - so it is here just for personal purposes :)
        File hugeFile = new File(pathToHugeFile);
        Long start = System.currentTimeMillis();
        Map<String, AbstractInstrument> instruments = runAnalysis(hugeFile);
        assertTrue(!instruments.isEmpty());
        hugeFile.deleteOnExit();
        Long end = System.currentTimeMillis();
        System.out.println("Execution time of processing large file -  " + TimeUnit.MILLISECONDS.toSeconds(end - start));
    }

    private  Map<String, AbstractInstrument> runAnalysis(List<String> lines) throws IOException {
        File file = null;
        try {
            file = File.createTempFile("test", "tst");
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsolutePath(), true)))) {
                lines.forEach(s -> out.println(s));
            } catch (IOException e) {
                System.err.println(e);
            }
            return runAnalysis(file);
        } finally {
            file.delete();
        }
    }

    private  Map<String, AbstractInstrument> runAnalysis(File file){
        InstrumentAnalysis instrumentAnalysis = new InstrumentAnalysis(file.getAbsolutePath());
        Map<String, AbstractInstrument> instruments = instrumentAnalysis.process();
        return instruments;
    }
}
