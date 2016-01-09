package com.example.ricardoprieto.silenciador;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ricardoprieto.silenciador.Adapters.BusquedaWifiArrayAdapter;
import com.example.ricardoprieto.silenciador.Entities.Regla;
import com.example.ricardoprieto.silenciador.Fachadas.ConfiguracionFachada;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Principal extends ActionBarActivity {

    BusquedaWifiArrayAdapter arrayAdapter = null;

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    public static final long NOTIFY_INTERVAL =1 * 1000; // 10 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        final ListView listaWifi = (ListView) findViewById(R.id.listaWifi);

        reloadWifi();

        listaWifi.setAdapter(arrayAdapter);

        listaWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final ScanResult item = (ScanResult) parent.getItemAtPosition(position);

                final Regla regla = new Regla(item);

                abreEditarReglaActivitie(regla);
            }

        });

        recargarDatosConfiguracion();
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    reloadWifi();
                }

            });
        }

    }

    private void reloadWifi() {
        final WifiManager wManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> wifiList;

        try {
            wifiList = wManager.getScanResults();
        } catch (Exception ex) {
            wifiList = new ArrayList<ScanResult>();
        }

        if (arrayAdapter == null) {
            arrayAdapter = new BusquedaWifiArrayAdapter(this, R.layout.activity_lista_wifi_activitie, wifiList);
        } else {
            arrayAdapter.clear();
            arrayAdapter.addAll(wifiList);
            arrayAdapter.notifyDataSetChanged();
        }

        Toast.makeText(this, String.format("Encontradas %d redes inalámbricas", wifiList.size()), Toast.LENGTH_LONG).show();
    }

    private void abreEditarReglaActivitie(Regla regla) {
        mTimer.cancel();
        Intent intent = new Intent(this, EditarReglaActivitie.class);

        intent.putExtra("Regla", regla);
        startActivityForResult(intent, ActivityEnumerador.EditarReglas.getValue());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    private int i = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.menu_configuracion:
                ejecutarConfiguracion();
                break;
            case R.id.menu_ver_reglas:
                abrirListadoReglas();
                break;
            case R.id.menu_recargar:

                reloadWifi();
                break;
            case R.id.menu_servicio_iniciar:
                startService();
                break;
            case R.id.menu_servicio_parar:
                stopService();
                break;
            case R.id.menu_salir:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void abrirListadoReglas() {
        mTimer.cancel();
        Intent intent = new Intent(this, ListarReglasActivitie.class);

        startActivityForResult(intent, ActivityEnumerador.ListarReglas.getValue());
    }


    // Method to start the service
    public void startService() {
        PackageManager pm = getPackageManager();
        ComponentName compName =
                new ComponentName(getApplicationContext(),
                        MuteByWifiBootReceiver.class);
        pm.setComponentEnabledSetting(
                compName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        startService(new Intent(getBaseContext(), MuteByWiFiService.class));
    }

    // Method to stop the service
    public void stopService() {

        PackageManager pm = getPackageManager();
        ComponentName compName =
                new ComponentName(getApplicationContext(),
                        MuteByWifiBootReceiver.class);
        pm.setComponentEnabledSetting(
                compName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        stopService(new Intent(getBaseContext(), MuteByWiFiService.class));
    }

    private void ejecutarConfiguracion() {
        mTimer.cancel();
        Intent intent = new Intent(this, ConfiguracionSettingsActivity.class);
        startActivityForResult(intent, ActivityEnumerador.Configuracion.getValue());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ActivityEnumerador enumerador=ActivityEnumerador.values()[requestCode];

        switch (enumerador) {
            case Configuracion:
                recargarDatosConfiguracion();
                break;
            case ListarReglas:
            case EditarReglas:
                recargarDatosConfiguracion();
                break;
        }
    }

    private void recargarDatosConfiguracion() {
        long tiempoRefresco= 5;

        try{
            tiempoRefresco=Long.parseLong(ConfiguracionFachada.getStringValue(this,"sync_frequency"));


            // cancel if already existed
            if (mTimer != null) {

              try{
                  mTimer.cancel();
              }catch (Exception ex){
              }
            } mTimer = new Timer();
            // schedule task
            mTimer = new Timer();
            mTimer.schedule(new TimeDisplayTimerTask(),NOTIFY_INTERVAL*tiempoRefresco);
        }catch (Exception ex){
Object o=ex;
        }


    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
            return rootView;
        }
    }
}
