package com.instrument.analysis;

import com.instrument.files.LargeFileReader;
import com.instrument.helper.Constants;
import com.instrument.instrument.AbstractInstrument;
import com.instrument.instrument.AggregationInstrument;
import com.instrument.instrument.InstrumentFactory;
import com.instrument.processor.LineProcessor;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ipopkov on 06/06/15.
 */
public class InstrumentAnalysis {

    private File source;

    public InstrumentAnalysis(String path) {
        this.source = new File(path);
        if (!source.exists()) {
            throw new RuntimeException("File not found!");
        }
    }

    public Map<String, AbstractInstrument> process() {
        BlockingQueue<String> linesToProcess = new LinkedBlockingQueue<String>();

        LargeFileReader fileReader = new LargeFileReader(source, linesToProcess);

        //All cpus should work :)
        int cpuNumber = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(cpuNumber);

        List<InstrumentFactory> usedFactories = new LinkedList<>();
        for (int i = 0; i < cpuNumber - 1; i++) {
            InstrumentFactory factory = InstrumentFactory.createInstance();
            usedFactories.add(factory);
            es.execute(new LineProcessor(linesToProcess, factory));
        }

        es.execute(fileReader);

        //Waiting for completion
        for (; ; ) {
            if (fileReader.isCompleted && linesToProcess.isEmpty()) {
                killThreadsByPoisonPill(es, linesToProcess);
                Map<String, AbstractInstrument> sortedInstruments = aggregateFactories(usedFactories);
                printResult(sortedInstruments);
                es.shutdown();
                return sortedInstruments;
            }
            synchronized (this) {
                try {
                    wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<String, AbstractInstrument> aggregateFactories(List<InstrumentFactory> usedFactories) {
        Map<String, AbstractInstrument> notSortedInstruments = new HashMap<>();
        usedFactories.forEach(factory -> {
                    factory.getUsedInstruments().forEach((name, currentInstrument) -> {
                                AbstractInstrument existingInstrument = notSortedInstruments.get(name);
                                AggregationInstrument aggregationInstrument;
                                if (existingInstrument == null) {
                                    aggregationInstrument = new AggregationInstrument(name);
                                } else {
                                    aggregationInstrument = (AggregationInstrument) existingInstrument;
                                }
                                aggregationInstrument.addDataWithoutValidation(currentInstrument.getTotal(),
                                        currentInstrument.getCount());
                                notSortedInstruments.put(name, aggregationInstrument);
                            }
                    );
                }
        );
        return new TreeMap<>(notSortedInstruments);
    }

    private void printResult(Map<String, AbstractInstrument> sortedInstruments) {
        System.out.println("Result: ");
        for (AbstractInstrument instrument : sortedInstruments.values()) {
            System.out.println(instrument.getName() + " mean: " + instrument.calculateMean());
        }
    }

    private void killThreadsByPoisonPill(ThreadPoolExecutor es, BlockingQueue<String> fileContent) {
        while (es.getActiveCount() != 0) {
            try {
                fileContent.put(Constants.POISON_PILL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

