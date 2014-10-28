package be.howest.nmct3.workoutapp.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by nielslammens on 28/10/14.
 */
public class ExercisesLoader extends AsyncTaskLoader<Cursor> {
    public ExercisesLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return null;
    }
}
