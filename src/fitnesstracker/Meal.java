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
public class Meal {
	private String title; //name of the meal, e.g. "breakfast"
	public ArrayList<Item> items = new ArrayList<Item>();
	public Meal() {
	}
	public void addItem(Item in) {
            items.add(in);
	}
        public void setTitle(String title) {
            this.title = title;
        }
	public int getCal() { //all get functions return the total value for the entire meal
		int total = 0;
		for (Item gItem : items) {
                    total += gItem.getCal();
                }
		return total;
	}
	public int getFat() {
		int total = 0;
		for (Item gItem : items) {
                    total += gItem.getFat();
                }
		return total;
	}
	public int getSodium() {
		int total = 0;
		for (Item gItem : items) {
                    total += gItem.getSodium();
                }
		return total;
	}
	public int getChol() {
		int total = 0;
		for (Item gItem : items) {
                    total += gItem.getChol();
                }
		return total;
	}
	public int getSugars() {
		int total = 0;
		for (Item gItem : items) {
                    total += gItem.getSugars();
                }
		return total;
	}
	public int getProtein() {
		int total = 0;
		for (Item gItem : items) {
                    total += gItem.getProtein();
                }
		return total;
	}
	public String getTitle() {
		return title;
	}
}
