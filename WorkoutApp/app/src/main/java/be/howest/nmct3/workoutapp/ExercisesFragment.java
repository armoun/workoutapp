package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

import be.howest.nmct3.workoutapp.data.Exercise;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ExercisesFragment extends Fragment {

    public final String[] MuscleGroups = {"CHEST", "BACK", "ARMS", "SHOULDERS", "ABS", "LEGS"};
    private ListAdapter myListAdapter;


    public ExercisesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        ExercisesFragment frag = new ExercisesFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.exercises_fragment_layout, null);

        myListAdapter = new MuscleGroupAdapter();
        GridView gridView = (GridView) root.findViewById(R.id.gridview_musclegroups);
        gridView.setAdapter(myListAdapter);


        return root;
    }



    class MuscleGroupAdapter extends ArrayAdapter<String>
    {
        private String[] MuscleGroupTitles;

        public MuscleGroupAdapter()
        {
            super(getActivity(), R.layout.exercises_grid_musclegroup_item_layout, R.id.grid_item_text);
            MuscleGroupTitles = MuscleGroups;
            this.addAll(MuscleGroupTitles);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            String MuscleGroupTitle = MuscleGroupTitles[position];

            TextView txtMuscleGroupTitle = (TextView) row.findViewById(R.id.grid_item_text);
            txtMuscleGroupTitle.setText(MuscleGroupTitle);

            return row;
        }

        @Override
        public int getCount() {
            return MuscleGroupTitles.length;
        }
    }




}
