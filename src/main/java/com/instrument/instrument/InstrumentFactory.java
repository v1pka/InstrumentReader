package com.instrument.instrument;

import com.instrument.helper.Constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ipopkov on 06/06/15.
 */
public class InstrumentFactory {

    private static class FactoryHolder {
        public static final InstrumentFactory instance = new InstrumentFactory();
    }

    private InstrumentFactory(){}

    public static InstrumentFactory getInstance() {
        return FactoryHolder.instance;
    }

    public Map<String, AbstractInstrument> getUsedInstruments() {
        return instruments;
    }

    private Map<String, AbstractInstrument> instruments = new ConcurrentHashMap<>();

    public AbstractInstrument getInstrument(String name){
        if(instruments.containsKey(name)){
            return instruments.get(name);
        } else {
            AbstractInstrument abstractInstrument = createByName(name);
            return abstractInstrument;
        }
    }

    private AbstractInstrument createByName(String name) {
        AbstractInstrument abstractInstrument;
        if(name.equalsIgnoreCase(Constants.INSTRUMENT1)){
            abstractInstrument = new Instrument1(name);
        } else if(name.equalsIgnoreCase(Constants.INSTRUMENT2)){
            abstractInstrument = new Instrument2(name);
        } else if(name.equalsIgnoreCase(Constants.INSTRUMENT3)){
            abstractInstrument = new Instrument3(name);
        } else {
            abstractInstrument = new OtherInstrument(name);
        }
        AbstractInstrument oldInstr = instruments.putIfAbsent(name, abstractInstrument);
        if(oldInstr != null){
            abstractInstrument = oldInstr;
        }
        return abstractInstrument;
    }

}
