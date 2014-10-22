package be.howest.nmct3.workoutapp.data;

import java.util.List;

/**
 * Created by nielslammens on 22/10/14.
 */
public class Workout {

    public String Name;
    public List<Exercise> exercices;

    public enum MuscleGroup{
        ARMS, BACK, CHEST, SHOULDERS, ABS, LEGS;
    }
}
