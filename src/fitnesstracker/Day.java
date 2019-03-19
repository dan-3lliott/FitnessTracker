/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnesstracker;

import java.util.ArrayList;

/**
 *
 * @author 9632019
 */
public class Day {
    public ArrayList<Meal> meals = new ArrayList<Meal>();
    private ArrayList<Workout> workouts = new ArrayList<Workout>();
    private int calorieGoal;
    private int burnGoal;
    public Day (int calorieGoal, int burnGoal) {
        this.calorieGoal = calorieGoal;
        this.burnGoal = burnGoal;
    }
    public void addMeal(String title) {
        meals.add(new Meal(title));
    }
    public void addWorkout(int duration, int calories) {
        workouts.add(new Workout(duration, calories));
    }
    public int getCalGoal() {
        return calorieGoal;
    }
    public int getBurnGoal() {
        return burnGoal;
    }
    public int getCalBurned() {
        int calBurned = 0;
        for (int i = 0; i < workouts.size(); i++) {
            calBurned += workouts.get(i).getCal();
        }
        return calBurned;
    }
    public int getDuration() {
        int duration = 0;
        for (int i = 0; i < workouts.size(); i++) {
            duration += workouts.get(i).getDuration();
        }
        return duration;
    }
}
