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
public class Month {
	private ArrayList<Week> weeks = new ArrayList<Week>();
	private int num; //1 - january, 2 - february, 3 - march, etc.
	public Month() {
	}
	public void addWeek() {
		//will add later
	}
	public int getMonth() {
		return num;
	}
	public int getNutritionInfo() {
		//will return the totals for monthly nutritional facts
	}
	public int getWorkoutInfo() {
		//will return calories burned, etc.
	}
}
