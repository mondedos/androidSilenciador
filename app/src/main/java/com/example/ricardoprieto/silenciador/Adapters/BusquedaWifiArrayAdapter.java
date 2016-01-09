package com.example.ricardoprieto.silenciador.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricardoprieto.silenciador.Entities.Regla;
import com.example.ricardoprieto.silenciador.Fachadas.ReglaFachada;
import com.example.ricardoprieto.silenciador.Principal;
import com.example.ricardoprieto.silenciador.R;
import com.example.ricardoprieto.silenciador.Utils.ReglasHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ricardoprieto on 02/10/14.
 */
public class BusquedaWifiArrayAdapter extends ArrayAdapter<ScanResult> {

    private final int resource = -1;
    private final List<ScanResult> listaWifis = new ArrayList<ScanResult>();

    ReglasHashMap reglasHashMap=new ReglasHashMap();

    public BusquedaWifiArrayAdapter(Context context, int resource, List<ScanResult> objects) {
        super(context, resource, objects);
        resource = resource;
        listaWifis.addAll(objects);
        reglasHashMap.addAll(ReglaFachada.leerReglas(context));
    }

    @Override
    public void addAll(Collection<? extends ScanResult> collection) {
        super.addAll(collection);

        reglasHashMap.addAll(ReglaFachada.leerReglas(getContext()));
    }

    @Override
    public void clear() {
        reglasHashMap.clear();
        super.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        Context context1 = getContext();
        if (rowView == null) {
            Context context = context1;
            Activity activity = (Activity) context;

            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.busqueda_wifi, null);

            ScanResult item = listaWifis.get(position);

            rowView.setTag(item);
        }

        ScanResult red = (ScanResult) rowView.getTag();

        if (red != null) {
            final TextView essidLine = (TextView) rowView.findViewById(R.id.essidLine);
            final TextView bssidLine = (TextView) rowView.findViewById(R.id.bssidLine);

            essidLine.setText(!"".equals(red.SSID) ? red.SSID : context1.getString(R.string.oculto));

            bssidLine.setText(red.BSSID+" - "+(!"".equals(red.SSID) ? red.SSID : context1.getString(R.string.oculto)));

            ImageView imageView=(ImageView)rowView.findViewById(R.id.icon);
            imageView.setImageResource(android.R.drawable.btn_star_big_off);
            if(reglasHashMap.containsKey(red.BSSID)){
                imageView.setImageResource(android.R.drawable.btn_star_big_on);
            }
        }

        return rowView;
    }
}
