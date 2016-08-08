package com.vietek.taxioperation.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.util.ControllerUtils;

@Repository
public class TelephoneTableTmsController extends
		BasicController<TelephoneTableTms> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static List<TelephoneExtensionTms> getExtensionsForTable(TelephoneTableTms tableTms) {

		TelephoneExtensionTmsController controllerChanel = (TelephoneExtensionTmsController) ControllerUtils
				.getController(TelephoneExtensionTmsController.class);

		String sql = "from TelephoneExtensionTms where telephoneTable = ? ";
		List<TelephoneExtensionTms> lstExtensions = controllerChanel.find(sql, tableTms);

		return lstExtensions;
	}
}
