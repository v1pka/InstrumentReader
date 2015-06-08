package com.instrument.instrument;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ipopkov on 06/06/15.
 */
public class OtherInstrument extends AbstractInstrument {

    private Map<LocalDate, Double> tenLastDates = new HashMap<LocalDate, Double>();

    public OtherInstrument(String name) {
        super(name);
    }

    @Override
    protected boolean validDate(LocalDate date) {
        return true;
    }

    @Override
    protected void addData(LocalDate date, double value) {
        if (tenLastDates.size() < 10) {
            tenLastDates.put(date, value);
            return;
        } else {
            Iterator<Map.Entry<LocalDate, Double>> iter = tenLastDates.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<LocalDate, Double> entry = iter.next();
                if (entry.getKey().isBefore(date)) {
                    iter.remove();
                    tenLastDates.put(date, value);
                    return;
                }
            }
        }
        return;
    }

    @Override
    public double calculateMean() {
        return getTotal() / getCount();
    }


    @Override
    public double getTotal() {
        Double total = new Double(0);
        for (Double value : tenLastDates.values()) {
            total += value;
        }
        return total;
    }

    @Override
    public long getCount() {
        return tenLastDates.size();
    }
}
