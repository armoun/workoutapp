package be.howest.nmct3.workoutapp.json;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.JsonReader;

import java.io.IOException;

import be.howest.nmct3.workoutapp.R;

/**
 * Created by nielslammens on 28/10/14.
 */
public class ExercisesLoader extends JsonLoader {

    public ExercisesLoader(Context context) {
        super(context, "exercises", new String[]{"id", "name", "musclegroup", "target", "description"}, R.raw.exercises);
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException {
        reader.beginArray();
        
    }
}
