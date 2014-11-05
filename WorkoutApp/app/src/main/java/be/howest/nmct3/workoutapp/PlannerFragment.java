package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PlannerFragment extends Fragment {

    private CursorAdapter myPlannerAdapter;


    public PlannerFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        PlannerFragment frag = new PlannerFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.planner_fragment_layout, null);

        String[] columns = new String[] { "name" };
        int[] viewIds = new int[] { R.id.dayWorkoutTitle };

        WorkoutsLoader wl = new WorkoutsLoader(getActivity());
        final Cursor cursor = wl.loadInBackground();

        final ListView listView = (ListView) root.findViewById(R.id.plannerList);
        myPlannerAdapter = new SimpleCursorAdapter(getActivity(),R.layout.planner_list_row_layout, cursor, columns, viewIds, 0);
        listView.setAdapter(myPlannerAdapter);

        return root;
    }


}
