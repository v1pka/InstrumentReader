package com.instrument.instrument;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ipopkov on 06/06/15.
 */
public class OtherInstrument extends AbstractInstrument {

    private Map<LocalDate, BigDecimal> tenLastDates = new HashMap<LocalDate, BigDecimal>();

    public OtherInstrument(String name) {
        super(name);
    }

    @Override
    protected boolean validDate(LocalDate date) {
        return true;
    }

    @Override
    protected void addData(LocalDate date, BigDecimal value) {
        if(tenLastDates.size() < 10){
            tenLastDates.put(date, value);
            return;
        } else {
            Iterator<Map.Entry<LocalDate, BigDecimal>> iter = tenLastDates.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<LocalDate, BigDecimal> entry = iter.next();
                if(entry.getKey().isBefore(date)){
                    iter.remove();
                    tenLastDates.put(date, value);
                    return;
                }
            }
        }
        return;
    }

    @Override
    public BigDecimal calculateMean() {
        BigDecimal total = new BigDecimal(0);
        for (BigDecimal value : tenLastDates.values()) {
            total = total.add(value);
        }
        long counter = tenLastDates.size();
        if(counter == 0){
            return ZERO_VALUE;
        }
        return total.divide(new BigDecimal(counter), RoundingMode.FLOOR);
    }
}
