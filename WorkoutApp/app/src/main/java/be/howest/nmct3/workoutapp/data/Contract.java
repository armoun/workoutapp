package be.howest.nmct3.workoutapp.data;

import android.provider.BaseColumns;

/**
 * Created by nielslammens on 22/10/14.
 */
public class Contract {

    public interface WorkoutColumns extends BaseColumns{
        public static final String NAME = "name";
        public static final String ISPAID = "ispaid";
    }

    public static final class Workouts implements WorkoutColumns{
        public static final String CONTENT_DIRECTORY = "workouts";
    }

    /*public interface ExerciseColumns extends BaseColumns{
        public static final String NAME = "name";
    }

    public static final class Exercises implements ExerciseColumns{
        public static final String CONTENT_DIRECTORY = "exercises";
    }*/

    public interface WorkoutExerciseColumns extends BaseColumns{
        public static final String WORKOUT_ID = "workout_id";
        public static final String EXERCISE_ID = "exercise_id";
        public static final String REPS = "reps";

    }

    public static final class WorkoutExercises implements WorkoutColumns{
        public static final String CONTENT_DIRECTORY = "workoutexercises";
    }




}
