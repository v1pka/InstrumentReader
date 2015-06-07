package com.instrument.instrument;

import java.time.LocalDate;

/**
 * Just for aggregation purposes
 * Created by ipopkov on 07/06/15.
 */
public class AggregationInstrument extends AbstractInstrument {

    private double total = 0;
    private long counter = 0L;

    public AggregationInstrument(String name) {
        super(name);
    }

    @Override
    protected boolean validDate(LocalDate date) {
        return false;
    }

    @Override
    protected void addData(LocalDate date, double value) {

    }

    public void addDataWithoutValidation(double value, long count) {
        total += value;
        counter += count;
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
