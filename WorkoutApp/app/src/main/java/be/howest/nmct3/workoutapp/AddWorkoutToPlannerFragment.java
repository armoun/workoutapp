package be.howest.nmct3.workoutapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class AddWorkoutToPlannerFragment extends android.support.v4.app.Fragment {

    private SimpleCursorAdapter myWorkoutsAdapter;
    public static String actionBarTitle = "Add workouts to planner";

    public AddWorkoutToPlannerFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        AddWorkoutToPlannerFragment frag = new AddWorkoutToPlannerFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_workout_to_planner, null);

        String[] columns = new String[] { "name" };
        int[] viewIds = new int[] { R.id.planner_add_workout_item_title };

        final ListView listView = (ListView) root.findViewById(R.id.lst_workouts_add_planner);
        myWorkoutsAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.fragment_add_workout_to_planner_item_layout,
                MainActivity.plannerWorkoutCursor,
                columns,
                viewIds,
                0
        );
        listView.setAdapter(myWorkoutsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Log.d("", "SELECTED WORKOUT ID: " + (position+1));
                //MainActivity.plannerWorkoutCursor.moveToPosition(position);
                //Log.d("", "SELECTED WORKOUT NAME: " + MainActivity.plannerWorkoutCursor.getString(1));

                MainActivity.plannerSelectedWorkoutId = position;

                android.support.v4.app.Fragment frag = android.support.v4.app.Fragment.instantiate(getActivity(), MainActivity.fragments[3]);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main, frag).commit();

                MainActivity.activeFragment = frag;
            }
        });

        getActivity().getActionBar().setTitle("Add workouts to planner");

        return root;
    }


}