package com.example.ricardoprieto.silenciador.Entities;

import android.net.wifi.ScanResult;

import java.io.Serializable;

/**
 * Created by ricardoprieto on 01/10/14.
 */
public class Regla implements Serializable {

    @Override
    public boolean equals(Object o) {
        if(o.getClass()!=this.getClass()) return false;

        Regla other=(Regla)o;

        return other.getBssid().equals(getBssid());
    }

    private String bssid;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;

    private boolean muteNotificacion;
    private boolean muteAlarma;
    private boolean muteMusica;
    private boolean muteTimbre;
    private boolean muteSistema;

    public Regla() {
        bssid = "";
        nombre = "";
    }

    public Regla(ScanResult scanResult) {
        bssid = scanResult.BSSID;
        nombre = scanResult.SSID;
        setMuteAlarma(true);
        setMuteMusica(true);
        setMuteNotificacion(true);
        setMuteSistema(true);
        setMuteTimbre(true);
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public boolean isMuteNotificacion() {
        return muteNotificacion;
    }

    public void setMuteNotificacion(boolean muteNotificacion) {
        this.muteNotificacion = muteNotificacion;
    }

    public boolean isMuteAlarma() {
        return muteAlarma;
    }

    public void setMuteAlarma(boolean muteAlarma) {
        this.muteAlarma = muteAlarma;
    }

    public boolean isMuteMusica() {
        return muteMusica;
    }

    public void setMuteMusica(boolean muteMusica) {
        this.muteMusica = muteMusica;
    }

    public boolean isMuteTimbre() {
        return muteTimbre;
    }

    public void setMuteTimbre(boolean muteTimbre) {
        this.muteTimbre = muteTimbre;
    }

    public boolean isMuteSistema() {
        return muteSistema;
    }

    public void setMuteSistema(boolean muteSistema) {
        this.muteSistema = muteSistema;
    }


}
