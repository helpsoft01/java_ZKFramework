package com.vietek.taxioperation.componentExtend;

import java.lang.reflect.Method;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ItemRenderer;

import com.vietek.taxioperation.ui.editor.Editor;

public class RenderItem_CSB implements ItemRenderer<Object> {

	@Override
	public String render(Component item, Object data, int index) throws Exception {
		// TODO Auto-generated method stub
		Method setNameMethod = data.getClass().getMethod("getId");
		int id = (int) setNameMethod.invoke(data); // pass arg
		if (id == 0) {
			return Editor.TEXTSEARCH;
		} else {
			return data.toString();
		}
	}
}
