package com.example.ricardoprieto.silenciador.Utils;

import com.example.ricardoprieto.silenciador.Entities.Regla;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by ricardoprieto on 03/10/14.
 */
public class ReglasHashMap extends HashMap<String, Regla> {

    public ReglasHashMap() {

    }

    public ReglasHashMap(Collection<Regla> reglaEnumeration) {
        addAll(reglaEnumeration);
    }

    public void add(Regla regla) {
        if (containsKey(regla.getBssid())) {
            remove(regla.getBssid());
        }
        put(regla.getBssid(), regla);
    }

    public void addAll(Collection<Regla> reglaEnumeration) {
        for (Regla regla : reglaEnumeration) {
            add(regla);
        }
    }
}
