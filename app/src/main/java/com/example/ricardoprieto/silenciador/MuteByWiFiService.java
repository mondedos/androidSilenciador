package com.example.ricardoprieto.silenciador;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.example.ricardoprieto.silenciador.Entities.Regla;
import com.example.ricardoprieto.silenciador.Fachadas.ConfiguracionFachada;
import com.example.ricardoprieto.silenciador.Fachadas.ReglaFachada;
import com.example.ricardoprieto.silenciador.Utils.ReglasHashMap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MuteByWiFiService extends Service {
    // constant
    // constant
    public static final long NOTIFY_INTERVAL = 60 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    ReglasHashMap reglas = null;

    public MuteByWiFiService() {
    }

    /**
     * indicates how to behave if the service is killed
     */
    int mStartMode;
    /**
     * interface for clients that bind
     */
    IBinder mBinder;
    /**
     * indicates whether onRebind should be used
     */
    boolean mAllowRebind;

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        servicioCancelado = false;
        reglas = new ReglasHashMap(ReglaFachada.leerReglas(this));

        // cancel if already existed
        recargarDatosConfiguracion();
    }

    private void recargarDatosConfiguracion() {
        long tiempoRefresco= 5;

        try{
            tiempoRefresco=Long.parseLong(ConfiguracionFachada.getStringValue(this, "sync_frequency_service"));


            // cancel if already existed
            if (mTimer != null) {

                try{
                    mTimer.cancel();
                }catch (Exception ex){
                }
            } mTimer = new Timer();
            // schedule task
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,NOTIFY_INTERVAL*tiempoRefresco);
        }catch (Exception ex){
            Object o=ex;
        }


    }
    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Servicio mute inicializado", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called when all clients have unbound with unbindService()
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * Called when a client is binding to the service with bindService()
     */
    @Override
    public void onRebind(Intent intent) {

    }

    boolean servicioCancelado;

    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {
        servicioCancelado = true;
        mTimer.cancel();
        setMute(new ArrayList<Regla>());
        super.onDestroy();
        Toast.makeText(this, "Servicio Mute destruido", Toast.LENGTH_LONG).show();
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    scanWifiAndMute();
                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

    }

    boolean _notificacionMuted;
    boolean _alarmaMuted;
    boolean _musicaMuted;
    boolean _timbreMuted;
    boolean _systemaMuted;

    private void setMute(Collection<Regla> reglaCollection) {

        boolean notificacion = false, alarma = false, musica = false, timbre = false, systema = false;

        for (Regla regla : reglaCollection) {
            if (servicioCancelado) break;
            Toast.makeText(getApplicationContext(), String.format("Regla %s encontrada.", regla.getNombre()),
                    Toast.LENGTH_SHORT).show();
            notificacion = notificacion || regla.isMuteNotificacion();
            alarma = alarma || regla.isMuteAlarma();
            musica = musica || regla.isMuteMusica();
            timbre = timbre || regla.isMuteTimbre();
            systema = systema || regla.isMuteSistema();
        }


        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(notificacion){
            if(!_notificacionMuted){
                _notificacionMuted=notificacion;

                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, notificacion);
            }
        }
        else {
            _notificacionMuted=false;
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, notificacion);
        }
        if(alarma){
            if(!_alarmaMuted){
                _alarmaMuted=alarma;
                amanager.setStreamMute(AudioManager.STREAM_ALARM, alarma);
            }
        }
        else {
            _alarmaMuted=false;
            amanager.setStreamMute(AudioManager.STREAM_ALARM, alarma);
        }
        if(musica){
            if(!_musicaMuted){
                _musicaMuted=musica;
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, musica);
            }
        }
        else {
            _musicaMuted=false;
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, musica);
        }
        if(timbre){
            if(!_timbreMuted){
                _timbreMuted=timbre;
                amanager.setStreamMute(AudioManager.STREAM_RING, timbre);
            }
        }
        else {
            _timbreMuted=false;
            amanager.setStreamMute(AudioManager.STREAM_RING, timbre);
        }
        if(systema){
            if(!_systemaMuted){
                _systemaMuted=systema;
                amanager.setStreamMute(AudioManager.STREAM_SYSTEM, systema);
            }
        }
        else {
            _systemaMuted=false;
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, systema);
        }

    }

    private void scanWifiAndMute() {
        WifiManager wManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> wifiList = wManager.getScanResults();

        List<Regla> reglaList = new ArrayList<Regla>();

        for (ScanResult scanresult : wifiList) {
            if (reglas.containsKey(scanresult.BSSID)) {
                reglaList.add(reglas.get(scanresult.BSSID));
            }
        }

        setMute(reglaList);
    }
}
