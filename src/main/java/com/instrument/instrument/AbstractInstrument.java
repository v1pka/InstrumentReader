package com.instrument.instrument;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ipopkov on 06/06/15.
 */
public abstract class AbstractInstrument {

    private String name;
    private final ReentrantLock lock = new ReentrantLock();
    protected final static BigDecimal ZERO_VALUE = new BigDecimal(0);

    public AbstractInstrument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractInstrument that = (AbstractInstrument) o;

        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }



    public void filterAndAddData(LocalDate date, BigDecimal value){
        if(!isWorkingDay(date) || !validDate(date)){
            return;
        }
        //Busy wait
        for (;;) {
            if(lock.tryLock()) {
                try {
                    addData(date, value);
                    return;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private boolean isWorkingDay(LocalDate date) {
        if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            return false;
        }
        return true;
    }


    protected abstract boolean validDate(LocalDate date);

    protected abstract void addData(LocalDate date, BigDecimal value);
    public abstract BigDecimal calculateMean();

    @Override
    public String toString() {
        return "Instrument {" +
                "name='" + name + '\'' +
                '}';
    }
}
