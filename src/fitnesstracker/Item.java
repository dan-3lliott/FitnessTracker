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
public class Item { //note: values are per serving so they are easily inputted off of the nutrition label
	private String title; //name of the item, e.g. "french toast"
	private int calories;
	private int fat; //grams
	private int sodium; //mg
	private int cholesterol; //mg
	private int sugars; //g
	private int protein; //g
	private int servings;
	public Item (int calories, int fat, int sodium, int cholesterol, int sugars, int protein, int servings, String title) {
		this.calories = calories;
		this.fat = fat;
		this.sodium = sodium;
		this.cholesterol = cholesterol;
		this.sugars = sugars;
		this.protein = protein;
		this.servings = servings;
		this.title = title;
	}
	public int getCal() { //all get functions return the total value for all servings
		return calories * servings;
	}
	public int getFat() {
		return fat * servings;
	}
	public int getSodium() {
		return sodium * servings;
	}
	public int getChol() {
		return cholesterol * servings;
	}
	public int getSugars() {
		return sugars * servings;
	}
	public int getProtein() {
		return protein * servings;
	}
	public String getTitle() {
		return title;
	}
}
