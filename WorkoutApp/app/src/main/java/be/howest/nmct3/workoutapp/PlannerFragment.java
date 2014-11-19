package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


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

        //WorkoutsLoader wl = new WorkoutsLoader(getActivity());
        //final Cursor cursor = wl.loadInBackground();

        final ListView listView = (ListView) root.findViewById(R.id.plannerList);
        myPlannerAdapter = new SimpleCursorAdapter(getActivity(),R.layout.planner_list_row_layout, MainActivity.plannerWorkoutCursor, columns, viewIds, 0);
        listView.setAdapter(myPlannerAdapter);

        if(MainActivity.plannerSelectedWorkoutId!=-1)
        {
            MainActivity.plannerWorkoutCursor.moveToPosition(MainActivity.plannerSelectedWorkoutId);
            Toast.makeText(getActivity(),MainActivity.plannerWorkoutCursor.getString(1), Toast.LENGTH_SHORT).show();
        }

        CalendarView planner = (CalendarView) root.findViewById(R.id.planner);
        final TextView plannerCurrentDate = (TextView) root.findViewById(R.id.plannerCurrentDate);

        plannerCurrentDate.setText("Workouts for today");

        planner.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                plannerCurrentDate.setText("Workouts for " + dayOfMonth + "/" + month + "/" + year);
            }
        });

        return root;
    }


}
