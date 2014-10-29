package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.Override;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class WorkoutsFragment extends Fragment {

<<<<<<< Updated upstream
    public final String[] Workouts = {"Workout 1", "Workout 2", "Workout 3"};
    private ListAdapter myWorkoutListAdapter;
=======
    Menu menu;

>>>>>>> Stashed changes

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
<<<<<<< Updated upstream

        myWorkoutListAdapter = new WorkoutsAdapter();
        ListView listView = (ListView) root.findViewById(R.id.workout_list);
        listView.setAdapter(myWorkoutListAdapter);
=======
        setHasOptionsMenu(true);
>>>>>>> Stashed changes

        return root;
    }

<<<<<<< Updated upstream
    class WorkoutsAdapter extends ArrayAdapter<String> {

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

    }

=======
    //creates the items on action bar


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.workout, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }


>>>>>>> Stashed changes

}
