package com.example.ricardoprieto.silenciador;

/**
 * Created by ricardoprieto on 04/10/14.
 */
public enum ActivityEnumerador {
    Configuracion(0),
    ListarReglas(1),
    EditarReglas(2);

    private final int value;
    private ActivityEnumerador(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
