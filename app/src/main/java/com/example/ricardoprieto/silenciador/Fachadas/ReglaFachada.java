package com.example.ricardoprieto.silenciador.Fachadas;

import android.content.Context;
import android.widget.Toast;

import com.example.ricardoprieto.silenciador.Entities.Regla;
import com.example.ricardoprieto.silenciador.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardoprieto on 02/10/14.
 */
public class ReglaFachada {

    public static void guardar(Context applicationContext, Regla regla) {
        String nombre = applicationContext.getPackageName();

        List<Regla> reglas = leerReglas(applicationContext);

        if (reglas.contains(regla)) {
            reglas.remove(regla);
            reglas.add(regla);
        } else {
            reglas.add(regla);
        }

        try {
            FileOutputStream fos = applicationContext.openFileOutput(nombre + ".dat", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(reglas);
            os.close();

            Toast.makeText(applicationContext, applicationContext.getString(R.string.reglaGuardada), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Regla> leerReglas(Context context) {
        String nombre = context.getPackageName();

        try {
            FileInputStream fis = context.openFileInput(nombre + ".dat");
            ObjectInputStream is = new ObjectInputStream(fis);
            List<Regla> simpleClass = (ArrayList<Regla>) is.readObject();
            is.close();
            return simpleClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<Regla>();
        } catch (OptionalDataException e) {
            e.printStackTrace();
            return new ArrayList<Regla>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<Regla>();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return new ArrayList<Regla>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Regla>();
        }

    }

    public static void eliminarRegla(Context applicationContext, Regla regla) {
        String nombre = applicationContext.getPackageName();

        List<Regla> reglas = leerReglas(applicationContext);

        if (reglas.contains(regla)) {
            reglas.remove(regla);
        }

        try {
            FileOutputStream fos = applicationContext.openFileOutput(nombre + ".dat", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(reglas);
            os.close();

            Toast.makeText(applicationContext, applicationContext.getString(R.string.reglaEliminada), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
