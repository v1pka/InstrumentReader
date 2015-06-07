package com.instrument.instrument;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ipopkov on 06/06/15.
 */
public class Instrument1 extends AbstractInstrument {

    private AtomicLong counter = new AtomicLong(0);
    private volatile double total = 0;

    public Instrument1(String name) {
        super(name);
    }

    @Override
    protected boolean validDate(LocalDate date) {
        return true;
    }

    @Override
    protected void addData(LocalDate date, double value) {
        total += value;
        counter.incrementAndGet();
    }

    @Override
    public double calculateMean() {
        if(counter.get() == 0){
            return ZERO_VALUE;
        }
        return total / counter.get();
    }
}
