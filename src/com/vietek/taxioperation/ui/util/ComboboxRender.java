package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;

import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.util.ControllerUtils;

public class ComboboxRender {
	private String image;
	private String title;
	private int value;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Combobox ComboboxRendering(String[] titles, String styles, int width, int height) {
		Combobox comb = new Combobox();
		if (styles.length() > 0) {
			comb.setStyle(styles);
		}

		List<ComboboxRender> lstComboboxs = new ArrayList<ComboboxRender>();
		for (int i = 0; i < titles.length; i++) {
			ComboboxRender bean = new ComboboxRender();
			bean.setTitle("-- " + titles[i] + " --");
			bean.setValue(i);
			lstComboboxs.add(bean);
		}

		comb.setItemRenderer(new ComboitemRenderer<ComboboxRender>() {
			@Override
			public void render(final Comboitem paramComboitem, ComboboxRender bean, int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if (bean.getValue() == 0) {
					comb.setSelectedItem(paramComboitem);
				}
			}
		});

		comb.setModel(new ListModelList<ComboboxRender>(lstComboboxs));
		if (width > 0) {
			comb.setWidth(width + "px");
		} else {
			comb.setWidth("200px");
		}
		if (height > 0) {
			comb.setHeight(height + "px");
		}

		return comb;
	}

	public Combobox ComboboxRendering(String[] titles, int[] values, String styles, String bonusClass, int width,
			int height, boolean hasGetAll) {
		Combobox comb = new Combobox();
		if (titles.length != values.length) {
			return comb;
		}
		if (styles.length() > 0) {
			comb.setStyle(styles);
		}

		if (bonusClass.length() > 0) {
			comb.setSclass(bonusClass);
		}

		if (width > 0) {
			comb.setWidth(width + "px");
		} else {
			comb.setWidth("200px");
		}
		if (height > 0) {
			comb.setHeight(height + "px");
		}

		List<ComboboxRender> lstComboboxs = new ArrayList<ComboboxRender>();
		if (hasGetAll) {
			ComboboxRender cos = new ComboboxRender();
			cos.setTitle(" Chọn tất cả ");
			cos.setValue(0);
			lstComboboxs.add(cos);
		}
		for (int i = 0; i < titles.length; i++) {
			ComboboxRender bean = new ComboboxRender();
			bean.setTitle(" " + titles[i] + " ");
			bean.setValue(values[i]);
			lstComboboxs.add(bean);
		}

		comb.setItemRenderer(new ComboitemRenderer<ComboboxRender>() {
			@Override
			public void render(final Comboitem paramComboitem, ComboboxRender bean, int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if (bean.getValue() == 0) {
					comb.setSelectedItem(paramComboitem);
				}
			}
		});

		comb.setModel(new ListModelList<ComboboxRender>(lstComboboxs));

		return comb;
	}

	public Combobox ComboboxRendering(String[] titles, int[] values, String[] imagepaths, String styles,
			String bonusClass, int width, int height) {
		Combobox comb = new Combobox();
		if (titles.length != values.length) {
			return comb;
		}
		if (styles.length() > 0) {
			comb.setStyle(styles);
		}

		if (bonusClass.length() > 0) {
			comb.setSclass(bonusClass);
		}

		if (width > 0) {
			comb.setWidth(width + "px");
		} else {
			comb.setWidth("200px");
		}
		if (height > 0) {
			comb.setHeight(height + "px");
		}
		List<ComboboxRender> lstComboboxs = new ArrayList<ComboboxRender>();

		for (int i = 0; i < titles.length; i++) {
			ComboboxRender bean = new ComboboxRender();
			bean.setImage(imagepaths[i]);
			bean.setTitle(titles[i]);
			bean.setValue(values[i]);
			lstComboboxs.add(bean);
		}

		comb.setItemRenderer(new ComboitemRenderer<ComboboxRender>() {
			@Override
			public void render(final Comboitem paramComboitem, ComboboxRender bean, int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				paramComboitem.setImage(bean.getImage());
				if (bean.getValue() == 0) {
					comb.setSelectedItem(paramComboitem);
				}
			}
		});

		comb.setModel(new ListModelList<ComboboxRender>(lstComboboxs));
		return comb;
	}

	public Combobox ComboboxRendering(HashMap<String, String> data, String styles, String bonusClass, int width,
			int height, int selectedvalue, boolean hasGetAll) {
		Combobox comb = new Combobox();
		if (styles.length() > 0) {
			comb.setStyle(styles);
		}

		if (bonusClass.length() > 0) {
			comb.setSclass(bonusClass);
		}

		if (width > 0) {
			comb.setWidth(width + "px");
		} else {
			comb.setWidth("200px");
		}
		if (height > 0) {
			comb.setHeight(height + "px");
		}

		List<ComboboxRender> lstComboboxs = new ArrayList<ComboboxRender>();
		if (hasGetAll) {
			ComboboxRender cos = new ComboboxRender();
			cos.setTitle(" Chọn tất cả ");
			cos.setValue(0);
			lstComboboxs.add(cos);
		}
		for (String key : data.keySet()) {
			ComboboxRender bean = new ComboboxRender();
			bean.setTitle(data.get(key));
			bean.setValue(Integer.parseInt(key));
			lstComboboxs.add(bean);
		}

		comb.setItemRenderer(new ComboitemRenderer<ComboboxRender>() {
			@Override
			public void render(final Comboitem paramComboitem, ComboboxRender bean, int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if (bean.getValue() == selectedvalue) {
					comb.setSelectedItem(paramComboitem);
				}
			}
		});

		comb.setModel(new ListModelList<ComboboxRender>(lstComboboxs));

		return comb;
	}

	public Combobox agentComboboxReder(String styles, String bonusClass, int width, int height, int selectedvalue,
			String username) {
		Combobox comb = new Combobox();
		AgentController agentController = (AgentController) ControllerUtils.getController(AgentController.class);
		List<Agent> results = agentController.find("From Agent");
		HashMap<String, String> mapResults = new HashMap<String, String>();
		for (Agent agent : results) {
			mapResults.put(String.valueOf(agent.getId()), agent.getAgentName());
		}
		comb = ComboboxRendering(mapResults, styles, bonusClass, width, height, selectedvalue, true);
		return comb;
	}

	@SuppressWarnings("unchecked")
	public Combobox vehgroupComboboxReder(String styles, String bonusClass, int width, int height, int agentid,
			int selectedvalue, String username) {
		Combobox comb = new Combobox();
		Session session = ControllerUtils.getCurrentSession();
		List<TaxiGroup> results = (List<TaxiGroup>) session.createQuery("Select tg From TaxiGroup tg "
				+ "Join tg.agent ag " + "Where -1 = " + agentid + " Or " + "tg.type = 0 And ag.id = " + agentid).list();
		session.close();

		HashMap<String, String> mapResults = new HashMap<String, String>();
		for (TaxiGroup group : results) {
			mapResults.put(String.valueOf(group.getId()), group.getName());
		}
		comb = ComboboxRendering(mapResults, styles, bonusClass, width, height, selectedvalue, true);
		return comb;
	}

	@SuppressWarnings("unchecked")
	public Combobox parkingComboboxReder(String styles, String bonusClass, int width, int height, int groupid,
			int selectedvalue, String username) {
		Combobox comb = new Combobox();
		Session session = ControllerUtils.getCurrentSession();

		List<ParkingArea> results = (List<ParkingArea>) session.createQuery("Select pa From ParkingArea pa "
				+ "Join pa.taxigroup tg " + "Where -1 = " + groupid + " Or " + " tg.id = " + groupid).list();
		session.close();

		HashMap<String, String> mapResults = new HashMap<String, String>();
		for (ParkingArea park : results) {
			mapResults.put(String.valueOf(park.getId()), park.getParkingName());
		}
		comb = ComboboxRendering(mapResults, styles, bonusClass, width, height, selectedvalue, true);
		return comb;
	}

}