package com.vietek.taxioperation.util;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.AbbreviationAddressController;
import com.vietek.taxioperation.model.AbbreviationAddress;

public class SearchAbbreviationUtils {
	
	private static ArrayList<AbbreviationAddress> lstAddress = new ArrayList<>();
 	
	private static boolean isLoadBVT = false;
	public static void loadBVT() {
		AbbreviationAddressController controller = (AbbreviationAddressController) ControllerUtils
				.getController(AbbreviationAddressController.class);
		String sql = "from AbbreviationAddress";
		List<AbbreviationAddress> lstAbb = controller.find(sql);
		if (lstAbb.size() > 0) {
			for (AbbreviationAddress abbreviationAddress : lstAbb) {
				lstAddress.add(abbreviationAddress);
			}
		}
		isLoadBVT = true;
		
		/*AbbreviationAddressController controller = (AbbreviationAddressController) ControllerUtils
				.getController(AbbreviationAddressController.class);
		String sql = "from AbbreviationAddress";
		List<AbbreviationAddress> lstAbb = controller.find(sql);
		if (lstAbb.size() > 0) {
			for (AbbreviationAddress abbreviationAddress : lstAbb) {
				mapAddress.put(abbreviationAddress.getValue().toUpperCase(), abbreviationAddress);
			}
		}*/
		isLoadBVT = true;
	}
	
	public static void reloadBVTAddress(AbbreviationAddress address) {
		//mapAddress.putIfAbsent(address.getValue().toUpperCase(), address);
		lstAddress.remove(address);
		lstAddress.add(address);
	}
	
	public ArrayList<AbbreviationAddress> searchAddressInMem(String searchText) {
		ArrayList<AbbreviationAddress> retVal = new ArrayList<>();
		for (int i = 0; i < lstAddress.size(); i++) {
			AbbreviationAddress address = lstAddress.get(i);
			if (address.getValue().toUpperCase().equals(searchText.toUpperCase())) {
				retVal.add(address);
			}	
		}		
		return retVal;
	}

	public ArrayList<Address> searchAddress(String headerText, String searchText) {
		ArrayList<Address> retVal = new ArrayList<>();
		if (searchText != null) {

			try {
				List<AbbreviationAddress> lstAbb = null;
				if (isLoadBVT) {
					lstAbb = searchAddressInMem(searchText);
				}
				else {
					AbbreviationAddressController controller = (AbbreviationAddressController) ControllerUtils
							.getController(AbbreviationAddressController.class);
	
					String sql = "from AbbreviationAddress where value = '" + searchText + "'";
					lstAbb = controller.find(sql);
				}
				if (lstAbb!= null && lstAbb.size() > 0) {
					for (AbbreviationAddress abbreviationAddress : lstAbb) {
						Address address = new Address();
						address.setName(headerText + " " + abbreviationAddress.getDescription());

						address.setByGoogle(false);
						if (headerText != "") {
							address.setLatitude(
									(abbreviationAddress.getLati() == null) ? 0.0 : abbreviationAddress.getLati());
							address.setLongitude(
									(abbreviationAddress.getLongi() == null) ? 0.0 : abbreviationAddress.getLongi());
						} else
							address.setTVT(headerText + " " + abbreviationAddress.getDescription());

						retVal.add(address);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				return retVal;
			} finally {

			}
		}
		return retVal;
	}
}
