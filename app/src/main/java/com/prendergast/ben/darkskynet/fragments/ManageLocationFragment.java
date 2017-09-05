package com.prendergast.ben.darkskynet.fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.prendergast.ben.darkskynet.R;
import com.prendergast.ben.darkskynet.WeatherUpdateService;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.prendergast.ben.darkskynet.MainActivity.SHARED_PREFERENCES;
import static com.prendergast.ben.darkskynet.MainActivity.ZIP_PREFERENCE;

/**
 * Created by doubl on 9/2/2017.
 */
public class ManageLocationFragment extends Fragment implements TextView.OnEditorActionListener, View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_location, container, false);

        Context context = container != null ? container.getContext() : null;
        if(context != null) {
            String currentZip = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).getString(ZIP_PREFERENCE, "");
            if(!TextUtils.isEmpty(currentZip)) {
                ((TextView)view.findViewById(R.id.currentZip)).setText(getString(R.string.current_zip, currentZip));
            }
        }

        ((EditText)view.findViewById(R.id.zipEntryEditText)).setOnEditorActionListener(this);
        view.findViewById(R.id.okButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        View contentView = getView();
        if(contentView != null) {
            saveZIPCodeAndExit((EditText)contentView.findViewById(R.id.zipEntryEditText));
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        View contentView = getView();
        if(contentView != null) {
            saveZIPCodeAndExit((EditText)contentView.findViewById(R.id.zipEntryEditText));
        }
        return false;
    }

    private void saveZIPCodeAndExit(EditText editText) {
        Editable editable = editText.getText();
        if(editable == null) {
            return;
        }

        String zipCode = editable.toString();
        if(TextUtils.isEmpty(zipCode)) {
            return;
        }

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(ZIP_PREFERENCE, zipCode);
        editor.apply();

        WeatherUpdateService.startAlarmForService(getActivity(), true);
        getFragmentManager().popBackStack();
    }
}
