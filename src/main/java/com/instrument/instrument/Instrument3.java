package com.instrument.instrument;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ipopkov on 06/06/15.
 */
public class Instrument3 extends AbstractInstrument {

    private volatile BigDecimal total = new BigDecimal(0);
    private AtomicLong counter = new AtomicLong(0);


    public Instrument3(String name) {
        super(name);
    }

    @Override
    protected boolean validDate(LocalDate date) {
        if(date.getYear() == 2014){
            return true;
        }
        return false;
    }

    @Override
    protected void addData(LocalDate date, BigDecimal value) {
        total = total.add(value);
        counter.incrementAndGet();
    }

    @Override
    public BigDecimal calculateMean() {
        if(counter.get() == 0){
            return ZERO_VALUE;
        }
        return total.divide(new BigDecimal(counter.get()), RoundingMode.HALF_UP) ;
    }
}
