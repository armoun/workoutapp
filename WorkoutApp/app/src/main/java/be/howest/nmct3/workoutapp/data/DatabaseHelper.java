package be.howest.nmct3.workoutapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nielslammens on 22/10/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static DatabaseHelper INSTANCE;
    public static Object lock = new Object();

    public static final String DB_NAME = "workoutapp.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (lock){
                if(INSTANCE == null){
                    INSTANCE = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Workouts.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.WorkoutExercises.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Exercises.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Planners.CONTENT_DIRECTORY);

        createWorkoutsTableV1(db);
        createWorkoutExercisesTableV1(db);
        createExercisesTableV1(db);
        createPlannerTableV1(db);

        //createDefaultRecords(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Workouts.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.WorkoutExercises.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Exercises.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Planners.CONTENT_DIRECTORY);

        createWorkoutsTableV1(db);
        createWorkoutExercisesTableV1(db);
        createExercisesTableV1(db);
        createPlannerTableV1(db);

        //createDefaultRecords(db);
    }

    private void createWorkoutsTableV1(SQLiteDatabase db){
        String SQL = "CREATE TABLE " + Contract.Workouts.CONTENT_DIRECTORY + " ("
                + Contract.WorkoutColumns._ID       + " INTEGER PRIMARY KEY, "
                + Contract.WorkoutColumns.NAME      + " TEXT,"
                + Contract.WorkoutColumns.ISPAID    + " INTEGER,"
                + Contract.WorkoutColumns.USERNAME  + " TEXT"
                + ");";

        Log.d("DatabaseHelper","Workouts: " + SQL);

        db.execSQL(SQL);

    }

    private void createWorkoutExercisesTableV1(SQLiteDatabase db){
        String SQL = "CREATE TABLE " + Contract.WorkoutExercises.CONTENT_DIRECTORY + " ("
                + Contract.WorkoutExerciseColumns._ID           + " INTEGER PRIMARY KEY, "
                + Contract.WorkoutExerciseColumns.WORKOUT_ID    + " INTEGER, "
                + Contract.WorkoutExerciseColumns.EXERCISE_ID   + " INTEGER, "
                + Contract.WorkoutExerciseColumns.REPS          + " TEXT"
                + ");";
        db.execSQL(SQL);
        Log.d("DatabaseHelper","WorkoutExercises: " + SQL);
    }

    private void createExercisesTableV1(SQLiteDatabase db){
        String SQL = "CREATE TABLE " + Contract.Exercises.CONTENT_DIRECTORY + " ("
                + Contract.ExerciseColumns._ID                  + " INTEGER PRIMARY KEY, "
                + Contract.ExerciseColumns.EXERCISE_NAME        + " TEXT, "
                + Contract.ExerciseColumns.MUSCLE_GROUP         + " TEXT, "
                + Contract.ExerciseColumns.TARGET               + " TEXT, "
                + Contract.ExerciseColumns.DESCRIPTION          + " TEXT, "
                + Contract.ExerciseColumns.IMAGE_NAME           + " TEXT "
                + ");";
        db.execSQL(SQL);
        Log.d("DatabaseHelper","Exercises: " + SQL);
    }

    private void createPlannerTableV1(SQLiteDatabase db) {
        String SQL = "CREATE TABLE " + Contract.Planners.CONTENT_DIRECTORY + " ("
                + Contract.PlannersColumns._ID                   + " INTEGER PRIMARY KEY, "
                + Contract.PlannersColumns.WORKOUT_ID            + " INTEGER, "
                + Contract.PlannersColumns.WO_DATE               + " TEXT"
                + ");";
        db.execSQL(SQL);
        Log.d("DatabaseHelper","Planner: " + SQL);
    }

    private void createDefaultRecords(SQLiteDatabase db){

        ContentValues cvDeadlift = new ContentValues();
        cvDeadlift.put(Contract.Exercises.EXERCISE_NAME, "Deadlift");
        cvDeadlift.put(Contract.Exercises.MUSCLE_GROUP , "back1");
        cvDeadlift.put(Contract.Exercises.TARGET , "lower back1");
        cvDeadlift.put(Contract.Exercises.DESCRIPTION , "Stand in front of a loaded barbell. ...");
        cvDeadlift.put(Contract.Exercises.IMAGE_NAME , "deadlift");
        Long deadLiftId = db.insert(Contract.Exercises.CONTENT_DIRECTORY, "", cvDeadlift);

        ContentValues cvSquat = new ContentValues();
        cvSquat.put(Contract.Exercises.EXERCISE_NAME, "Squat");
        cvSquat.put(Contract.Exercises.MUSCLE_GROUP , "legs");
        cvSquat.put(Contract.Exercises.TARGET , "quadriceps");
        cvSquat.put(Contract.Exercises.DESCRIPTION , "This exercise is best performed inside a squat rack for safety purposes. ...");
        cvSquat.put(Contract.Exercises.IMAGE_NAME , "squat");
        Long squatId = db.insert(Contract.Exercises.CONTENT_DIRECTORY, "", cvSquat);

        ContentValues cvBenchPress = new ContentValues();
        cvBenchPress.put(Contract.Exercises.EXERCISE_NAME, "Bench press");
        cvBenchPress.put(Contract.Exercises.MUSCLE_GROUP , "chest");
        cvBenchPress.put(Contract.Exercises.TARGET , "chest");
        cvBenchPress.put(Contract.Exercises.DESCRIPTION , "Lie back1 on a flat bench. ...");
        cvBenchPress.put(Contract.Exercises.IMAGE_NAME , "benchpress");
        Long benchPressId = db.insert(Contract.Exercises.CONTENT_DIRECTORY, "", cvBenchPress);

        ContentValues cvOverheadPress = new ContentValues();
        cvOverheadPress.put(Contract.Exercises.EXERCISE_NAME, "Overhead press");
        cvOverheadPress.put(Contract.Exercises.MUSCLE_GROUP , "shoulders");
        cvOverheadPress.put(Contract.Exercises.TARGET , "shoulders");
        cvOverheadPress.put(Contract.Exercises.DESCRIPTION , "Start by placing a barbell that is about chest high on a squat rack. ...");
        cvOverheadPress.put(Contract.Exercises.IMAGE_NAME , "overheadpress");
        Long overheadPressId = db.insert(Contract.Exercises.CONTENT_DIRECTORY, "", cvOverheadPress);

        ContentValues cvBicycleCrunch = new ContentValues();
        cvBicycleCrunch.put(Contract.Exercises.EXERCISE_NAME, "Bicycle crunch");
        cvBicycleCrunch.put(Contract.Exercises.MUSCLE_GROUP , "abs");
        cvBicycleCrunch.put(Contract.Exercises.TARGET , "abs");
        cvBicycleCrunch.put(Contract.Exercises.DESCRIPTION , "Lie flat on the floor with your lower back1 pressed to the ground. ...");
        cvBicycleCrunch.put(Contract.Exercises.IMAGE_NAME , "bicyclecrunch");
        Long bicycleCrunchId = db.insert(Contract.Exercises.CONTENT_DIRECTORY, "", cvBicycleCrunch);

        ContentValues cvBicepCurl = new ContentValues();
        cvBicepCurl.put(Contract.Exercises.EXERCISE_NAME, "Bicep curl");
        cvBicepCurl.put(Contract.Exercises.MUSCLE_GROUP , "arms");
        cvBicepCurl.put(Contract.Exercises.TARGET , "biceps");
        cvBicepCurl.put(Contract.Exercises.DESCRIPTION , "Stand up with a dumbbell in each hand being held at arms length. ...");
        cvBicepCurl.put(Contract.Exercises.IMAGE_NAME , "bicepcurl");
        Long bicepCurlId = db.insert(Contract.Exercises.CONTENT_DIRECTORY, "", cvBicepCurl);


        // Sample workouts 1: chest, bench press, free

        ContentValues v1 = new ContentValues();
        v1.put(Contract.Workouts.NAME, "Chest day");
        v1.put(Contract.Workouts.ISPAID, 0);
        Long id = db.insert(Contract.Workouts.CONTENT_DIRECTORY, "", v1);

        ContentValues v1e = new ContentValues();
        v1e.put(Contract.WorkoutExercises.WORKOUT_ID, id);
        v1e.put(Contract.WorkoutExercises.EXERCISE_ID, benchPressId);
        v1e.put(Contract.WorkoutExercises.REPS, "12,10,8");
        Long id2 = db.insert(Contract.WorkoutExercises.CONTENT_DIRECTORY, "", v1e);

        // Sample workouts 2: legs, squat, free

        ContentValues v2 = new ContentValues();
        v2.put(Contract.Workouts.NAME, "Leg day");
        v2.put(Contract.Workouts.ISPAID, 0);
        Long id3 = db.insert(Contract.Workouts.CONTENT_DIRECTORY, "", v2);

        ContentValues v2e = new ContentValues();
        v2e.put(Contract.WorkoutExercises.WORKOUT_ID, id3);
        v2e.put(Contract.WorkoutExercises.EXERCISE_ID, squatId);
        v2e.put(Contract.WorkoutExercises.REPS, "12,10,8");
        Long id4 = db.insert(Contract.WorkoutExercises.CONTENT_DIRECTORY, "", v2e);

        // Sample workouts 3: arms, bicep curl, paid

        ContentValues v3 = new ContentValues();
        v3.put(Contract.Workouts.NAME, "Arm day");
        v3.put(Contract.Workouts.ISPAID, 1);
        Long id5 = db.insert(Contract.Workouts.CONTENT_DIRECTORY, "", v3);

        ContentValues v3e = new ContentValues();
        v3e.put(Contract.WorkoutExercises.WORKOUT_ID, id5);
        v3e.put(Contract.WorkoutExercises.EXERCISE_ID, bicepCurlId);
        v3e.put(Contract.WorkoutExercises.REPS, "12,10,8");
        Long id6 = db.insert(Contract.WorkoutExercises.CONTENT_DIRECTORY, "", v3e);

    }
}
