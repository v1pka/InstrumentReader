package com.instrument.instrument;

import java.time.LocalDate;

/**
 * Created by ipopkov on 06/06/15.
 */
public class Instrument1 extends AbstractInstrument {

    private Long counter = 0L;
    private double total = 0;

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
        counter++;
    }

    @Override
    public double calculateMean() {
        if (counter == 0) {
            return ZERO_VALUE;
        }
        return total / counter;
    }

    @Override
    public double getTotal() {
        return total;
    }

    @Override
    public long getCount() {
        return counter;
    }
}
