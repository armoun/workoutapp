package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import be.howest.nmct3.workoutapp.data.SettingsAdmin;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingsFragment extends Fragment {

    private ListAdapter myListAdapter;

    public static final String[] Settings = {"Name", "Gender", "Date of Birth", "E-mail", "Picture", "Units"};

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        SettingsFragment frag = new SettingsFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.settings_fragment_layout, null);

        myListAdapter = new CustomSettingsAdapter();
        ListView listview = (ListView) root.findViewById(R.id.settings_list);
        listview.setAdapter(myListAdapter);

        return root;
    }

    class CustomSettingsAdapter extends ArrayAdapter<String> {

        public CustomSettingsAdapter() {
            super(getActivity(), R.layout.settings_list_item_layout, R.id.list_settings_item_text);
            this.addAll(Settings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            TextView textSettingsTitle = (TextView) row.findViewById(R.id.list_settings_item_text);
            textSettingsTitle.setText(Settings[position]);

            TextView textSettingsPreview = (TextView) row.findViewById(R.id.list_settings_item_text_preview);
            String value = SettingsAdmin.getInstance(getContext()).getValueForSetting(position);
            textSettingsPreview.setText(value);

            return row;
        }

        @Override
        public int getCount() {
            return Settings.length;
        }
    }

}
