package com.vietek.taxioperation.ui.util;

import org.zkoss.zul.Grid;
import org.zkoss.zul.Radiogroup;
/**
 * 
 * @author Dungnm
 *
 */
public class RadioGroupAdvanceSearchItem {
	private Grid grid;
	private Radiogroup rg;
	private int value;
	public Grid getGrid() {
		return grid;
	}
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	public Radiogroup getRg() {
		return rg;
	}
	public void setRg(Radiogroup rg) {
		this.rg = rg;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
