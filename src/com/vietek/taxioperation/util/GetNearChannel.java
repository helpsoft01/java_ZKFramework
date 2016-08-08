package com.vietek.taxioperation.util;

import java.math.BigDecimal;
import java.util.List;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.googlemapSearch.FindNearChannel;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.webservice.CallInWS;

public class GetNearChannel extends FindNearChannel {

	private String phoneNumber;
	private TelephoneExtensionTms extension;
	private List<JsonExtension> lstExtension;

	public List<JsonExtension> getLstextension() {
		return lstExtension;
	}

	public void setLstextension(List<JsonExtension> lstextension) {
		this.lstExtension = lstextension;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public TelephoneExtensionTms getExtension() {
		return extension;
	}

	public void setExtension(TelephoneExtensionTms extension) {
		this.extension = extension;
	}

	public GetNearChannel(List<JsonExtension> lstExtension) {
		setLstextension(lstExtension);
	}

	public GetNearChannel(String phoneNumber) {
		setPhoneNumber(phoneNumber);
	}

	public ExtensionEnable getBetterExtension(List<JsonExtension> lstExtension_In_VoipCenter, String phoneNumber,
			VoipCenter voipCenter, BigDecimal uuid) {
		Customer customer = null;
//		customer = CustomerController.getOrCreateCustomerNew(phoneNumber);
		if (customer == null) {
			AppLogger.logDebug.info("TaoKhachHang|False|" + phoneNumber);
			return null;
		}
		if (lstExtension_In_VoipCenter.size() == 0)
			return null;

		ExtensionEnable extensionEnable = new ExtensionEnable();

		return extensionEnable;
	}

	public List<ChannelTms> getListChannel() {

		if (CallInWS.lstChannel == null) {
			ChannelTmsController controllerChanel = (ChannelTmsController) ControllerUtils
					.getController(ChannelTmsController.class);
			String sql = "from ChannelTms ";
			CallInWS.lstChannel = controllerChanel.find(sql);
		}
		return CallInWS.lstChannel;
	}

}
