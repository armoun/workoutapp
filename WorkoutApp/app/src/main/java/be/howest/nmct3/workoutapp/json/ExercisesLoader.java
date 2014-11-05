package be.howest.nmct3.workoutapp.json;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;

import be.howest.nmct3.workoutapp.R;

/**
 * Created by nielslammens on 28/10/14.
 */
public class ExercisesLoader extends JsonLoader {

    private final String mMuscleGroup;
    private final int[] mWorkoutExercises;

    public ExercisesLoader(Context context, String muscleGroup, int[] workoutExercises) {
        super(context, "exercises", new String[]{BaseColumns._ID, "id", "name", "musclegroup", "target", "description"}, R.raw.exercises);
        this.mMuscleGroup = muscleGroup;
        this.mWorkoutExercises = workoutExercises;
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException {

        int id = 0;
        String name = "";
        String musclegroup = "";
        String target = "";
        String description = "";

        reader.beginArray();

        while (reader.hasNext()){

            reader.beginObject();

            reader.nextName();
            id = reader.nextInt();
            Log.d("", id + " id __________________________________________________");
            reader.nextName();
            name = reader.nextString();
            Log.d("", name + " name __________________________________________________");
            reader.nextName();
            musclegroup = reader.nextString();
            Log.d("", musclegroup + " musclegroup __________________________________________________");
            reader.nextName();
            target = reader.nextString();
            Log.d("", target + " target __________________________________________________");
            reader.nextName();
            description = reader.nextString();
            Log.d("", description + " description __________________________________________________");

            reader.endObject();
            Log.d("", "endObject() " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + " __________________________________________________");

            if (mMuscleGroup != "") {
                if (musclegroup.equalsIgnoreCase(musclegroup)) {
                    MatrixCursor.RowBuilder row = cursor.newRow();
                    row.add(id);
                    row.add(id);
                    row.add(name);
                    row.add(musclegroup);
                    row.add(target);
                    row.add(description);
                }
            }else if (mWorkoutExercises != null){
                int length = mWorkoutExercises.length;
                for(int i = 0; i < length; i++){
                    if(mWorkoutExercises[i] == id){
                        MatrixCursor.RowBuilder row = cursor.newRow();
                        row.add(id);
                        row.add(id);
                        row.add(name);
                        row.add(musclegroup);
                        row.add(target);
                        row.add(description);
                    }
                }
            }else{
                MatrixCursor.RowBuilder row = cursor.newRow();
                row.add(id);
                row.add(id);
                row.add(name);
                row.add(musclegroup);
                row.add(target);
                row.add(description);
            }

        }

        reader.endArray();

    }
}
