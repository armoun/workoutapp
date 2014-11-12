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


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingsFragment extends Fragment {

    private ListAdapter myListAdapter;


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
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            TextView txtNavDrawerTitle = (TextView) row.findViewById(R.id.list_settings_item_text);
            txtNavDrawerTitle.setText("test");

            return row;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

}
