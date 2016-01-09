package com.example.ricardoprieto.silenciador.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ricardoprieto.silenciador.Entities.Regla;
import com.example.ricardoprieto.silenciador.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardoprieto on 03/10/14.
 */
public class BusquedaReglasArrayAdapter extends ArrayAdapter<Regla> {
    private final Context context = null;
    private final int resource = -1;

    private final List<Regla> listaReglas = new ArrayList<Regla>();

    public BusquedaReglasArrayAdapter(Context context, int resource, List<Regla> objects) {
        super(context, resource, objects);
        context = context;
        resource = resource;
        listaReglas.addAll(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views

        if (rowView == null) {
            Context context = getContext();
            Activity activity = (Activity) context;

            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.busqueda_reglas, null);

            Regla item = listaReglas.get(position);

            rowView.setTag(item);
        }

        Regla row = (Regla) rowView.getTag();

        if (row != null) {
            final TextView nombreReglaLine = (TextView) rowView.findViewById(R.id.nombreReglaLine);
            final TextView bssidLine = (TextView) rowView.findViewById(R.id.bssidLine);

            nombreReglaLine.setText(row.getNombre());

            bssidLine.setText(row.getBssid()+" - "+(!"".equals(row.getNombre()) ? row.getNombre() : getContext().getString(R.string.oculto)));
        }

        return rowView;
    }
}
