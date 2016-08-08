package com.vietek.taxioperation.ui.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ItemRenderer;

public class M2MItemRenderer<T> implements ItemRenderer<T> {

	@Override
	public String render(Component comp, T data, int index) throws Exception {
		String str = "...";
		if (data != null)
			str = data.toString();
		return str;
	}
}
