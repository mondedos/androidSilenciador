package com.example.ricardoprieto.silenciador;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.ricardoprieto.silenciador.Entities.Regla;
import com.example.ricardoprieto.silenciador.Fachadas.ReglaFachada;


public class EditarReglaActivitie extends ActionBarActivity {


    private Regla getRegla() {
        return (Regla) getIntent().getSerializableExtra("Regla");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_regla_activitie);

        setTitle(getString(R.string.editorDeReglas));

        final Regla regla = getRegla();

        final EditText texto = (EditText) findViewById(R.id.textViewRegla);

        texto.setText("Regla para " + regla.getNombre() + " BSSID -> " + regla.getBssid());

        final CheckBox checkBoxNotificacion = (CheckBox) findViewById(R.id.checkBoxNotificacion);
        final CheckBox checkBoxAlarma = (CheckBox) findViewById(R.id.checkBoxAlarma);
        final CheckBox checkBoxMusica = (CheckBox) findViewById(R.id.checkBoxMusica);
        final CheckBox checkBoxTimbre = (CheckBox) findViewById(R.id.checkBoxTimbre);
        final CheckBox checkBoxSistema = (CheckBox) findViewById(R.id.checkBoxSistema);

        checkBoxNotificacion.setChecked(regla.isMuteNotificacion());
        checkBoxAlarma.setChecked(regla.isMuteAlarma());
        checkBoxMusica.setChecked(regla.isMuteMusica());
        checkBoxTimbre.setChecked(regla.isMuteTimbre());
        checkBoxSistema.setChecked(regla.isMuteSistema());

        final Button guardar = (Button) findViewById(R.id.buttonGuardar);
        final Button buttonEliminar = (Button) findViewById(R.id.buttonEliminar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rellenarRegla(regla);

                ReglaFachada.guardar(getApplicationContext(), regla);
            }


        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReglaFachada.eliminarRegla(getApplicationContext(), regla);
            }
        });
    }

    private void rellenarRegla(Regla regla) {
        final EditText texto = (EditText) findViewById(R.id.textViewRegla);

        final CheckBox checkBoxNotificacion = (CheckBox) findViewById(R.id.checkBoxNotificacion);
        final CheckBox checkBoxAlarma = (CheckBox) findViewById(R.id.checkBoxAlarma);
        final CheckBox checkBoxMusica = (CheckBox) findViewById(R.id.checkBoxMusica);
        final CheckBox checkBoxTimbre = (CheckBox) findViewById(R.id.checkBoxTimbre);
        final CheckBox checkBoxSistema = (CheckBox) findViewById(R.id.checkBoxSistema);

        regla.setNombre(texto.getText().toString());
        regla.setMuteSistema(checkBoxSistema.isChecked());
        regla.setMuteTimbre(checkBoxTimbre.isChecked());
        regla.setMuteMusica(checkBoxMusica.isChecked());
        regla.setMuteAlarma(checkBoxAlarma.isChecked());
        regla.setMuteNotificacion(checkBoxNotificacion.isChecked());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editar_regla_activitie, menu);
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
}
