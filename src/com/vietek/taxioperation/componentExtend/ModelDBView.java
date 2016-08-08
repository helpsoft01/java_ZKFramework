package com.vietek.taxioperation.componentExtend;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.bind.annotation.Init;

import com.vietek.taxioperation.util.ControllerUtils;

public class ModelDBView {

	ModelDBList<?> model;

	public ModelDBList<?> getModel() {
		return model;
	}

	public void setModel(ModelDBList<?> model) {
		this.model = model;
	}

	@Init
	public void init() {

		model = new ModelDBList<Object>(10) {

			@Override
			protected int loadTotalSize() {
				if (totalSize > -1)
					return totalSize;
				Session session = ControllerUtils.getCurrentSession();
				Query query;

				String sql = String.format("select count(*) from %s where 1=1 %s", "", "");

				query = session.createQuery(sql);
				totalSize = ((Number) query.uniqueResult()).intValue();
				return totalSize;
			}

			@Override
			protected List<Object> loadFromDB(int startIndex, int size) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		;
	}
}
