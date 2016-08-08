package com.vietek.taxioperation.ui.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.ui.editor.Editor;

public class AdvanceSearchItem {
	public static String CONDITION_AND = "AND";
	public static String CONDITION_OR = "OR";
	public static int INT_AND = 1;
	public static int INT_OR = 2;
	public static int ITEM_ADD = 1;
	public static int ITEM_DEL = 0;
	public static int CONDITION_ROOT = 0; 
	String strKey;
	String strValue;
	int status;
	Vlayout vlayout;
	Editor editor; 
	Component component;
	Button btnAdd;
	Button btnDel;
	Button btnSearch; 
	Combobox cbb;
	Class<?> clazz;
	RadioGroupAdvanceSearchItem rgAdvanceSearchItem;
	//if type = 0: CONDITION ROOT
	//type = 1: CONDITION CHILD
	int type;
	public static String getCONDITION_OR() {
		return CONDITION_OR;
	}
	public static void setCONDITION_OR(String cONDITION_OR) {
		CONDITION_OR = cONDITION_OR;
	}
	public String getStrKey() {
		return strKey;
	}
	public void setStrKey(String strKey) {
		this.strKey = strKey;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Vlayout getVlayout() {
		return vlayout;
	}
	public void setVlayout(Vlayout vlayout) {
		this.vlayout = vlayout;
	}
	public Editor getEditor() {
		return editor;
	}
	public void setEditor(Editor editor) {
		this.editor = editor;
	}
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	public Button getBtnAdd() {
		return btnAdd;
	}
	public void setBtnAdd(Button btnAdd) {
		this.btnAdd = btnAdd;
	}
	public Button getBtnDel() {
		return btnDel;
	}
	public void setBtnDel(Button btnDel) {
		this.btnDel = btnDel;
	}
	public Combobox getCbb() {
		return cbb;
	}
	public void setCbb(Combobox cbb) {
		this.cbb = cbb;
	}
	public RadioGroupAdvanceSearchItem getRgAdvanceSearchItem() {
		return rgAdvanceSearchItem;
	}
	public void setRgAdvanceSearchItem(
			RadioGroupAdvanceSearchItem rgAdvanceSearchItem) {
		this.rgAdvanceSearchItem = rgAdvanceSearchItem;
	}
	public Button getBtnSearch() {
		return btnSearch;
	}
	public void setBtnSearch(Button btnSearch) {
		this.btnSearch = btnSearch;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	@Override
    public boolean equals(Object object)
    {
        boolean result = false;

        if (object != null && object instanceof AdvanceSearchItem)
        {
           AdvanceSearchItem ad = (AdvanceSearchItem) object;
           if(this.vlayout.getId().equals(ad.vlayout.getId())) {
        	   result = true;
           }
        }

        return result;
    }
	
	
}
