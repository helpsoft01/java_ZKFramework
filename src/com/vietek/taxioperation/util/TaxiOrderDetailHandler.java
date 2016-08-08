package com.vietek.taxioperation.util;

import org.zkoss.zul.Tab;

public interface TaxiOrderDetailHandler {

	public void onTaxiOrderChangeTitle(Tab tab, String tile);

	public void onTaxiOrderCloseForm(Tab tab,CallInfo call);
}
