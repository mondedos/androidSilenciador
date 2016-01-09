package com.example.ricardoprieto.silenciador.Fachadas;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ricardoprieto on 04/10/14.
 */
public class ConfiguracionFachada {

    public static String getStringValue(Context context, String propiedad) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(propiedad, "");
    }
}
