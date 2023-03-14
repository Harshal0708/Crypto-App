package com.example.cryptoapp.modual.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cryptoapp.R;
import com.example.cryptoapp.Response.DocumentResponseItem;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DocSpinnerAdapter extends ArrayAdapter<DocumentResponseItem> {

    public DocSpinnerAdapter(Context context,
                             ArrayList<DocumentResponseItem> algorithmList) {
        super(context, 0, algorithmList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.doc_spinner_adapter, parent, false);
        }

        TextView txt_doc_name = convertView.findViewById(R.id.txt_doc_name);
        DocumentResponseItem currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            txt_doc_name.setText(currentItem.getDocumentName());
        }
        return convertView;
    }
}
