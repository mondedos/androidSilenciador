package com.example.ricardoprieto.silenciador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ricardoprieto.silenciador.Adapters.BusquedaReglasArrayAdapter;
import com.example.ricardoprieto.silenciador.Entities.Regla;
import com.example.ricardoprieto.silenciador.Fachadas.ReglaFachada;

import java.util.List;


public class ListarReglasActivitie extends ActionBarActivity {

    BusquedaReglasArrayAdapter busquedaReglasArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_reglas_activitie);

        setTitle(getString(R.string.listadoDeReglas));

        final ListView listaWifi = (ListView) findViewById(R.id.listaWifi);

        reloadReglas();

        listaWifi.setAdapter(busquedaReglasArrayAdapter);

        listaWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                final Regla regla = (Regla) parent.getItemAtPosition(position);

                abreEditarReglaActivitie(regla);
            }

        });
    }

    private void reloadReglas() {

        final List<Regla> reglas = ReglaFachada.leerReglas(this);

        if (busquedaReglasArrayAdapter == null) {
            busquedaReglasArrayAdapter = new BusquedaReglasArrayAdapter(this, R.layout.activity_listar_reglas_activitie, reglas);
        } else {
            busquedaReglasArrayAdapter.clear();
            busquedaReglasArrayAdapter.addAll(reglas);
            busquedaReglasArrayAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listar_reglas_activitie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abreEditarReglaActivitie(Regla regla) {
        Intent intent = new Intent(this, EditarReglaActivitie.class);

        intent.putExtra("Regla", regla);
        startActivity(intent);
    }
}
