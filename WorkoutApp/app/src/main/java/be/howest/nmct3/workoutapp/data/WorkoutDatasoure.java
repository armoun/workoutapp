package be.howest.nmct3.workoutapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Created by nielslammens on 27/10/14.
 */
public class WorkoutDatasoure {

    public void createWorkout(Context context, String name, Integer isPaid, List<Exercise> exercises){

        ContentValues values1 = new ContentValues();
        values1.put(Contract.Workouts.NAME, name);
        values1.put(Contract.Workouts.ISPAID, isPaid);
        values1.put(Contract.Workouts.USERNAME, SettingsAdmin.getInstance(context).getUsername());

        Uri uri = context.getContentResolver().insert(Contract.Workouts.CONTENT_URI, values1);

        Cursor c = context.getContentResolver().query(uri, null,null,null,null);
        c.moveToFirst();

        Log.d("be.howest.nmct3.workoutapp", uri.toString() + " " + c.getString(c.getColumnIndex(c.getColumnName(0))) + ";" + c.getString(c.getColumnIndex(c.getColumnName(1))) + ";" + c.getString(c.getColumnIndex(c.getColumnName(2))));

        Integer workout_id = c.getInt(c.getColumnIndex(c.getColumnName(0)));

        if (exercises != null){


        for (int i = 0; i < exercises.size(); i++) {
            ContentValues values2 = new ContentValues();
            values2.put(Contract.WorkoutExercises.WORKOUT_ID, workout_id);
            values2.put(Contract.WorkoutExercises.EXERCISE_ID, exercises.get(i).Id);
            values2.put(Contract.WorkoutExercises.REPS, repsToString(exercises.get(i).Reps));
            Uri uri2 = context.getContentResolver().insert(Contract.WorkoutExercises.CONTENT_URI, values2);
            Log.d("be.howest.nmct3.workoutapp", uri.toString() + "________________________________________");
        }
        }
    }

    public String repsToString(List<Integer> repList){
        String reps = "";
        for (int i = 0; i < repList.size(); i++) {
            reps += repList.get(i);
            reps += ",";
        }

        reps = reps.substring(0, reps.length() - 1);

        return reps;
    }

    public void deleteWorkout(Context context, int id){
        String wo_id = "" + id;
        context.getContentResolver().delete(Contract.Workouts.CONTENT_URI, Contract.Workouts._ID + " =?", new String[]{wo_id});
    }

    public void deleteExerciseForWorkout(Context context, int exerciseId, int workoutId){
        String ex_id = ""+ exerciseId;
        String wo_id = ""+ workoutId;
        context.getContentResolver().delete(Contract.WorkoutExercises.CONTENT_URI, Contract.WorkoutExercises.WORKOUT_ID + " =? AND " + Contract.WorkoutExercises.EXERCISE_ID + " =?", new String[]{wo_id, ex_id});
    }

    public void updateWorkoutName(Context context, int id, String newName){
        String wo_id = "" + id;
        ContentValues c = new ContentValues();
        c.put(Contract.Workouts.NAME, newName);
        context.getContentResolver().update(Contract.Workouts.CONTENT_URI,c, Contract.Workouts._ID + " =?", new String[]{wo_id});
    }

    public Uri addExerciseToWorkout(Context context, int exerciseId, int workoutId, String reps){
        ContentValues c = new ContentValues();
        c.put(Contract.WorkoutExercises.WORKOUT_ID, workoutId);
        c.put(Contract.WorkoutExercises.EXERCISE_ID, exerciseId);
        c.put(Contract.WorkoutExercises.REPS, reps);
        Uri uri = context.getContentResolver().insert(Contract.WorkoutExercises.CONTENT_URI, c);
        return uri;
    }
}
