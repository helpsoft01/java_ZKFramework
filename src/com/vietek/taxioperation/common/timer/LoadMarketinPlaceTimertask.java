package com.vietek.taxioperation.common.timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.controller.ArrangementTaxiController;
import com.vietek.taxioperation.model.ArrangementTaxi;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.ObjectUtils;

/**
 *
 * @author VuD
 */
public class LoadMarketinPlaceTimertask extends TimerTask {

	public LoadMarketinPlaceTimertask() {
		super();
	}

	@Override
	public void run() {
		try {
			ArrangementTaxiController controller = (ArrangementTaxiController) ControllerUtils
					.getController(ArrangementTaxiController.class);
			List<ArrangementTaxi> lst = controller.find("from ArrangementTaxi where isActive = ?", true);
			if (lst != null) {
				// ListCommon.LIST_ARRANGAMENT_PLACE.clear();
				// ListCommon.LIST_ARRANGAMENT_PLACE.addAll(lst);
				List<ArrangementTaxi> lstRemove = new ArrayList<>();
				for (ArrangementTaxi arrangementTaxi : ListCommon.LIST_ARRANGAMENT_PLACE) {
					if (!lst.contains(arrangementTaxi)) {
						lstRemove.add(arrangementTaxi);
					}
				}
				for (ArrangementTaxi arrangementTaxi : lstRemove) {
					ListCommon.LIST_ARRANGAMENT_PLACE.remove(arrangementTaxi);
					MapCommon.MAP_ARRANGEMENT.remove(arrangementTaxi.getId() + "");
				}

				for (ArrangementTaxi marketingPlace : lst) {
					if (MapCommon.MAP_TAXI_LIST.get(marketingPlace.getId()) == null) {
						LinkedList<Taxi> lstTmp = new LinkedList<>();
						MapCommon.MAP_TAXI_LIST.put(marketingPlace.getId(), lstTmp);
						MapCommon.MAP_ARRANGEMENT.put(marketingPlace.getId() + "", marketingPlace);
					}
					boolean isNew = true;
					for (ArrangementTaxi arrangementTaxi : ListCommon.LIST_ARRANGAMENT_PLACE) {
						if (arrangementTaxi.getId() == marketingPlace.getId()) {
							ObjectUtils.copyObjectSyn(marketingPlace, arrangementTaxi);
							isNew = false;
						}
					}
					if (isNew) {
						ListCommon.LIST_ARRANGAMENT_PLACE.add(marketingPlace);
					}
				}
				List<Integer> lstMarketingId = new ArrayList<>();
				Iterator<Integer> ite = MapCommon.MAP_TAXI_LIST.keySet().iterator();
				while (ite.hasNext()) {
					Integer key = (Integer) ite.next();
					boolean isRemove = true;
					for (ArrangementTaxi marketingPlace : lst) {
						if (marketingPlace.getId() == key) {
							isRemove = false;
							break;
						}
					}
					if (isRemove) {
						lstMarketingId.add(key);
					}
				}
				for (Integer integer : lstMarketingId) {
					MapCommon.MAP_TAXI_LIST.remove(integer);
				}
				// Check online taxi to remove in queue arrangement taxi
				for (ArrangementTaxi arrangementTaxi : ListCommon.LIST_ARRANGAMENT_PLACE) {
					LinkedList<Taxi> lstTaxi = (LinkedList<Taxi>)MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId());
					if(lstTaxi != null && lstTaxi.size() > 0){
						for (Taxi taxi : lstTaxi) {
							if(!taxi.isConnect()){
								taxi.goOutTaxiQueue();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

}
