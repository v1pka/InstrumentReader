package com.instrument.processor;

import com.instrument.helper.Constants;
import com.instrument.instrument.AbstractInstrument;
import com.instrument.instrument.InstrumentFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ipopkov on 06/06/15.
 */
public class LineProcessor implements Runnable {

    private final BlockingQueue<String> linesToProcess;
    private final InstrumentFactory instrumentFactory;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);


    public LineProcessor(BlockingQueue<String> linesToProcess) {
        this.linesToProcess = linesToProcess;
        instrumentFactory = InstrumentFactory.getInstance();
    }

    @Override
    public void run() {
        String line = "";
        try {
            while ((line = linesToProcess.take()) != null) {
                if(line.equals(Constants.POISON_PILL)){
                    return;
                }
                String[] tokens = line.split(",");
                if(tokens.length == 3){
                    String name = tokens[0];
                    LocalDate date = LocalDate.parse(tokens[1], formatter);
                    BigDecimal value = new BigDecimal(tokens[2]);
                    AbstractInstrument instrument = instrumentFactory.getInstrument(name);
                    instrument.filterAndAddData(date, value);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}