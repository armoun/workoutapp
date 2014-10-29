package be.howest.nmct3.workoutapp;



import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.lang.Override;

import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class WorkoutsFragment extends Fragment {

    //public final String[] Workouts = {"Workout 1", "Workout 2", "Workout 3"};
    private CursorAdapter myWorkoutCursorAdapter;

    public WorkoutsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        WorkoutsFragment frag = new WorkoutsFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workouts_fragment_layout, null);

        String[] columns = new String[] { "name" };
        int[] viewIds = new int[] { android.R.id.text1 };

        WorkoutsLoader wl = new WorkoutsLoader(getActivity());
        Cursor cursor = wl.loadInBackground();

        ListView listView = (ListView) root.findViewById(R.id.workout_list);
        myWorkoutCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.workouts_list_workout_item_rowlayout, cursor, columns, viewIds, 0);
        listView.setAdapter(myWorkoutCursorAdapter);

        return root;
    }

    /*class WorkoutsAdapter extends CursorAdapter {

        private String[] WorkoutTitles;

        public WorkoutsAdapter() {
            super(getActivity(), R.layout.workouts_list_workout_item_rowlayout, R.id.workout_item_title);
            WorkoutTitles = Workouts;
            this.addAll(WorkoutTitles);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            String WorkoutTitle = WorkoutTitles[position];

            TextView txtWorkoutTitle = (TextView) row.findViewById(R.id.workout_item_title);
            txtWorkoutTitle.setText(WorkoutTitle);

            return row;
        }

    }*/



}
