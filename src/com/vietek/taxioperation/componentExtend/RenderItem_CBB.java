package com.vietek.taxioperation.componentExtend;

import java.lang.reflect.Method;
import java.util.Random;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.vietek.taxioperation.ui.editor.Editor;

class RenderItem_CBB implements ComboitemRenderer<Object> {

	public RenderItem_CBB() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("serial")
	public void render(Comboitem item, Object data, int index) throws Exception {
		Method setNameMethod = data.getClass().getMethod("getId");
		int id = (int) setNameMethod.invoke(data); // pass arg
		if (id == 0) {
			item.setLabel(Editor.TEXTSEARCH);
			item.setClass("cssSearchExtend");
		} else {
			item.setLabel(data.toString());
		}
		item.setValue(data);
		item.setDescription("");
	}
}
