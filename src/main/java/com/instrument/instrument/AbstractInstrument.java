package com.instrument.instrument;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Created by ipopkov on 06/06/15.
 */
public abstract class AbstractInstrument {

    private String name;
    protected final static double ZERO_VALUE = 0;

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


    public void filterAndAddData(LocalDate date, double value) {
        if(!isWorkingDay(date) || !validDate(date)){
            return;
        }
        addData(date, value);
    }

    private boolean isWorkingDay(LocalDate date) {
        if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            return false;
        }
        return true;
    }


    protected abstract boolean validDate(LocalDate date);

    protected abstract void addData(LocalDate date, double value);

    public abstract double calculateMean();

    public abstract double getTotal();

    public abstract long getCount();

    @Override
    public String toString() {
        return "Instrument {" +
                "name='" + name + '\'' +
                '}';
    }
}
