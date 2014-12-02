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
public class ExercisesLoaderJson extends JsonLoader {

    private final String mMuscleGroup;
    private final int[] mWorkoutExercises;

    public ExercisesLoaderJson(Context context, String muscleGroup, int[] workoutExercises) {
        super(context, "exercises", new String[]{BaseColumns._ID, "name", "musclegroup", "target", "description", "image"}, R.raw.exercises);
        this.mMuscleGroup = muscleGroup;
        this.mWorkoutExercises = workoutExercises;
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException {

        int id = -1;
        String name = "";
        String musclegroup = "";
        String target = "";
        String description = "";
        String image = "";

        reader.beginArray();

        while (reader.hasNext()){

            id++;

            reader.beginObject();

            reader.nextName();
            id = reader.nextInt();

            reader.nextName();
            name = reader.nextString();

            reader.nextName();
            musclegroup = reader.nextString();

            reader.nextName();
            target = reader.nextString();

            reader.nextName();
            description = reader.nextString();

            reader.nextName();
            image = reader.nextString();

            reader.endObject();


            if (mMuscleGroup != "") {
                Log.d("", mMuscleGroup + "__________________________________________________");
                if (mMuscleGroup.equalsIgnoreCase(musclegroup)) {
                    Log.d("", mMuscleGroup + " vgl met " + musclegroup + " is " + musclegroup.equalsIgnoreCase(musclegroup) + "__________________________________________________");
                    MatrixCursor.RowBuilder row = cursor.newRow();
                    row.add(id);
                    row.add(name);
                    row.add(musclegroup);
                    row.add(target);
                    row.add(description);

                    Log.d("", "endObject() " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + " __________________________________________________");

                }
            }else if (mWorkoutExercises != null){
                int length = mWorkoutExercises.length;
                for(int i = 0; i < length; i++){
                    if(mWorkoutExercises[i] == id){
                        MatrixCursor.RowBuilder row = cursor.newRow();
                        row.add(id);
                        row.add(name);
                        row.add(musclegroup);
                        row.add(target);
                        row.add(description);
                        Log.d("", "endObject() " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + " __________________________________________________");
                    }
                }
            }else{
                MatrixCursor.RowBuilder row = cursor.newRow();
                row.add(id);
                row.add(name);
                row.add(musclegroup);
                row.add(target);
                row.add(description);
                Log.d("", "endObject() " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + " __________________________________________________");
            }

        }

        reader.endArray();

    }
}
