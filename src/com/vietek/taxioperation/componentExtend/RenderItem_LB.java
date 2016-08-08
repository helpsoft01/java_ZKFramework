package com.vietek.taxioperation.componentExtend;

import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class RenderItem_LB implements ListitemRenderer<Object> {

	public RenderItem_LB() {
	}

	@Override
	public void render(Listitem item, Object data, int index) throws Exception {

		item.setValue(data);
		item.setLabel(data.toString());
	}
}
