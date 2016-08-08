package com.vietek.taxioperation.googlemapSearch;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Combobox;

import com.vietek.taxioperation.model.MyPrediction;
import com.vietek.taxioperation.model.TmpPlace;
import com.vietek.taxioperation.util.MapUtils;

/**
 * 
 * @author hung
 * 
 */
public class SearchGooglePlaces implements Runnable {

	Combobox combobox;
	String datasearch;
	Desktop desktop;
	List<TmpPlace> lstTmpPlaces = new ArrayList<>();

	private MyPrediction[] pre;

	public SearchGooglePlaces(Combobox combo, String datasearch, Desktop destop) {
		this.combobox = combo;
		this.datasearch = datasearch;
		desktop = destop;
	}

	@Override
	public void run() {
		try {
			Executions.activate(desktop);
			if (combobox != null) {
				combobox.setAttribute("fulfill", "onSelected");
				combobox.setAutodrop(true);

				combobox.getItems().clear();
				if (lstTmpPlaces != null)
					lstTmpPlaces.clear();

				TmpPlace tmpPlace = new TmpPlace();
				if (datasearch != null && !datasearch.isEmpty()) {
					// Get in google places
					pre = MapUtils.getPredictions(datasearch);
					if (pre != null) {
						for (int i = 0; i < pre.length; i++) {
							combobox.appendItem(pre[i].getDescription());
							tmpPlace = new TmpPlace();
							tmpPlace.setDes(pre[i].getDescription());
							tmpPlace.setPlaceId(pre[i].getPlace_id());
							tmpPlace.setType(2);
							if (lstTmpPlaces != null && lstTmpPlaces.size() > 0) {
								if (!lstTmpPlaces.contains(tmpPlace)) {
									lstTmpPlaces.add(tmpPlace);
								}
							} else {
								lstTmpPlaces = new ArrayList<TmpPlace>();
								lstTmpPlaces.add(tmpPlace);
							}
						}
					} else {
						tmpPlace.setDes("Không tìm thấy !");
						lstTmpPlaces.add(tmpPlace);
					}
					if (lstTmpPlaces != null) {
						for (int i = 0; i < lstTmpPlaces.size(); i++) {
							combobox.appendItem(lstTmpPlaces.get(i).getDes());

						}
						if (combobox.getItemCount() > 0) {
							combobox.open();
						}
					}
				}
			}

			datasearch = "";
			Executions.deactivate(desktop);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDatasearch() {
		return datasearch;
	}

	public void setDatasearch(String datasearch) {
		this.datasearch = datasearch;
	}
}