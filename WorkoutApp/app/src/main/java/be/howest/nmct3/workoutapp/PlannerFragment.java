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

import java.util.Calendar;


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

        final ListView listView = (ListView) root.findViewById(R.id.plannerList);
        //myPlannerAdapter = new SimpleCursorAdapter(getActivity(),R.layout.planner_list_row_layout, MainActivity.plannerWorkoutCursor, columns, viewIds, 0);
        listView.setAdapter(myPlannerAdapter);

        final CalendarView planner = (CalendarView) root.findViewById(R.id.planner);

        if(MainActivity.plannerSelectedWorkoutId!=-1)
        {
            MainActivity.plannerWorkoutCursor.moveToPosition(MainActivity.plannerSelectedWorkoutId);
            Toast.makeText(getActivity(),"Plan this workout: " + MainActivity.plannerWorkoutCursor.getString(1) + " for date: " + MainActivity.plannerSelectedDate, Toast.LENGTH_SHORT).show();
        }
        final TextView plannerCurrentDate = (TextView) root.findViewById(R.id.plannerCurrentDate);

        setPlannerSelectedDate(planner);
        plannerCurrentDate.setText("Workout for " + MainActivity.plannerSelectedDate);

        planner.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                MainActivity.phoneCalendar.set(Calendar.YEAR, year);
                MainActivity.phoneCalendar.set(Calendar.MONTH, month);
                MainActivity.phoneCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setPlannerSelectedDate(planner);
                plannerCurrentDate.setText("Workout for " + MainActivity.plannerSelectedDate);
            }
        });

        return root;
    }

    private void setPlannerSelectedDate(CalendarView planner) {
        MainActivity.plannerSelectedDate = MainActivity.phoneCalendar.get(Calendar.DAY_OF_MONTH)+"/"+ (MainActivity.phoneCalendar.get(Calendar.MONTH)+1) +"/"+MainActivity.phoneCalendar.get(Calendar.YEAR);
        planner.setDate(MainActivity.phoneCalendar.getTimeInMillis(),true,true);
    }
}
