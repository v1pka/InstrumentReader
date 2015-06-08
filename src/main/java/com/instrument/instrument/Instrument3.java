package com.instrument.instrument;

import java.time.LocalDate;

/**
 * Created by ipopkov on 06/06/15.
 */
public class Instrument3 extends AbstractInstrument {

    private double total = 0;
    private Long counter = 0L;


    public Instrument3(String name) {
        super(name);
    }

    @Override
    protected boolean validDate(LocalDate date) {
        if (date.getYear() == 2014) {
            return true;
        }
        return false;
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
