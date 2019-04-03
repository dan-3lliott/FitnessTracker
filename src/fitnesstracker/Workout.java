/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnesstracker;

/**
 *
 * @author 9632019
 */
public class Workout {
	private int duration; //duration in minutes
	private int calories; //total calories burned
	public Workout(int duration, int calories) {
		this.duration = duration;
		this.calories = calories;
	}
	public int getDuration() {
		return duration;
	}
	public int getCal() {
		return calories;
	}
}
