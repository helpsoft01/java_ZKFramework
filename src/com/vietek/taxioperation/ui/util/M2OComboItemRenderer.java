package com.vietek.taxioperation.ui.util;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.vietek.taxioperation.ui.editor.M2OEditor;

public class M2OComboItemRenderer<T> implements ComboitemRenderer<T> {

	@Override
	public void render(Comboitem item, T data, int index) throws Exception {
		if (data != null) {
			item.setLabel(data.toString());
			item.setValue(data);
		}
		else {
			item.setLabel("Load More...");
			item.setValue(M2OEditor.LOAD_MORE);
			item.setStyle("font-weight: bold; font-style: italic");
		}
	}
}
