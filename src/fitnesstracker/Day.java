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
    private int num; //number for the day, e.g. june 10 would be the num 10
    private int numOfWeek; //number for which day this is, e.g. 1 would be monday
    private int weekNum; //which week this day is in
    private int calorieGoal;
    private int burnGoal;
    private int fatGoal; //grams
    private int sodiumGoal; //mg
    private int cholesterolGoal; //mg
    private int sugarsGoal; //g
    private int proteinGoal; //g
    public Day (int num, int numOfWeek, int weekNum) {
        this.num = num;
        this.numOfWeek = numOfWeek;
        this.weekNum = weekNum;
    }
    public void addMeal() {
        meals.add(new Meal());
    }
    public void addWorkout(int duration, int calories) {
        workouts.add(new Workout(duration, calories));
    }
    public int getNum() {
        return num;
    }
    public int getNumOfWeek() {
        return numOfWeek;
    }
    public int getWeekNum() {
        return weekNum;
    }
    public int getCalGoal() {
        return calorieGoal;
    }
    public int getBurnGoal() {
        return burnGoal;
    }
    public int getCalBurned() {
        int calBurned = 0;
        for (Workout gWorkout : workouts) {
            calBurned += gWorkout.getCal();
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
    public int getFatGoal() {
        return fatGoal;
    }
    public int getSodiumGoal() {
        return sodiumGoal;
    }
    public int getCholesterolGoal() {
        return cholesterolGoal;
    }
    public int getSugarsGoal() {
        return sugarsGoal;
    }
    public int getProteinGoal() {
        return proteinGoal;
    }
    public void setCalGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }
    public void setBurnGoal(int burnGoal) {
        this.burnGoal = burnGoal;
    }
    public void setFatGoal(int fatGoal) {
        this.fatGoal = fatGoal;
    }
    public void setSodiumGoal(int sodiumGoal) {
        this.sodiumGoal = sodiumGoal;
    }
    public void setCholesterolGoal(int cholesterolGoal) {
        this.cholesterolGoal = cholesterolGoal;
    }
    public void setSugarsGoal(int sugarsGoal) {
        this.sugarsGoal = sugarsGoal;
    }
    public void setProteinGoal(int proteinGoal) {
        this.proteinGoal = proteinGoal;
    }
    public int getTotalCal() {
        int total = 0;
        for (Meal gMeal : meals) {
            total += gMeal.getCal();
        }
        return total;
    }
    public int getTotalFat() {
        int total = 0;
        for (Meal gMeal : meals) {
            total += gMeal.getFat();
        }
        return total;
    }
    public int getTotalSodium() {
        int total = 0;
        for (Meal gMeal : meals) {
            total += gMeal.getSodium();
        }
        return total;
    }
    public int getTotalCholesterol() {
        int total = 0;
        for (Meal gMeal : meals) {
            total += gMeal.getChol();
        }
        return total;
    }
    public int getTotalSugars() {
        int total = 0;
        for (Meal gMeal : meals) {
            total += gMeal.getSugars();
        }
        return total;
    }
    public int getTotalProtein() {
        int total = 0;
        for (Meal gMeal : meals) {
            total += gMeal.getProtein();
        }
        return total;
    }
}
