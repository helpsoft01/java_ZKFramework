package com.vietek.taxioperation.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.EnumUserAction;
import com.vietek.taxioperation.common.QueCommon;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.LoggingUserAction;
import com.vietek.taxioperation.model.SysFunction;

public class SaveLogToQueue extends Thread{

	private AbstractModel model;
	private List<AbstractModel> lstmodel;
	private EnumUserAction action;	
	private String form;
	private int userId;
	
	@Override
	public void run(){
		if (this.model != null) {
			this.ParseModelDataToLog(this.model, this.action, this.form, this.userId);
		}else {
			if (this.lstmodel != null) {
				this.ParseLstModelDataToLog(this.lstmodel, this.action, this.form, this.userId);
			}			
		}
	}
	
	public SaveLogToQueue(Object model, EnumUserAction action, SysFunction form, int userId) {
		// TODO Auto-generated constructor stub
		this.model = (AbstractModel) model;
		this.action = action;
		this.userId = userId;
		this.lstmodel = null;
		
		if (form == null) {
			this.form = "/zul/index.zul";
		}else {
			if (!StringUtils.isEmpty(form.getClazz())) {
				this.form = form.getClazz();
			}else{
				this.form = form.getZulFile();
			}	
		}		
	}
	
	public SaveLogToQueue(List<?> lstmodel, EnumUserAction action, SysFunction form, int userId) {
		// TODO Auto-generated constructor stub
		this.model = null;
		this.action = action;
		this.userId = userId;
		this.lstmodel = new ArrayList<AbstractModel>();
		for (Object object : lstmodel) {
			this.lstmodel.add((AbstractModel) object);
		}	
		
		if (form == null) {
			this.form = "/zul/index.zul";
		}else {
			if (!StringUtils.isEmpty(form.getClazz())) {
				this.form = form.getClazz();
			}else{
				this.form = form.getZulFile();
			}	
		}		
	}
	
	@SuppressWarnings("rawtypes")
	public void ParseModelDataToLog(AbstractModel model, EnumUserAction action, String form, int userId){		
		try {
			LoggingUserAction loggingUserAction = new LoggingUserAction();
			
			if (model != null) {
				Class modelClazz = model.getClass();
				List<Field> fields = AbstractModel.getAllFields(modelClazz);
				StringBuilder names = new StringBuilder();
				StringBuilder values = new StringBuilder();
				for (Field field : fields) {
					try {
						field.setAccessible(true);
						String name = field.getName();	
						String value = "";
						if (name.equals("serialVersionUID")) {
							continue;
						}
						Object fieldvalue = AbstractModel.getValue(model, name);			
						if (fieldvalue == null) {
							value = "null";
						}else {
							String typename = AbstractModel.getDataType(model, name).getName();
							
							if (typename.contains("com.vietek.taxioperation.model")) {		
								AbstractModel obj = (AbstractModel)fieldvalue;
								value = String.valueOf(obj.getId());
							} else {
								value = fieldvalue.toString();
							}						
						}
						names.append(name).append(";");
						values.append(value).append(";");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}					
				
				loggingUserAction.setModelname(model.getClass().getName());
				loggingUserAction.setFormname(form);
				loggingUserAction.setAction(action.getValue());
				loggingUserAction.setFieldsdetail(names.toString());
				loggingUserAction.setValuesdetail(values.toString());
				loggingUserAction.setTimelog(new Timestamp(new Date().getTime()));
				loggingUserAction.setUserid(this.userId);	
				
			}else {
				loggingUserAction.setModelname("");
				loggingUserAction.setFormname(form);
				loggingUserAction.setAction(action.getValue());
				loggingUserAction.setFieldsdetail("");
				loggingUserAction.setValuesdetail("");
				loggingUserAction.setTimelog(new Timestamp(new Date().getTime()));
				loggingUserAction.setUserid(userId);
			}
			
			QueCommon.queueLoggingUserAction.offer(loggingUserAction);		
			
		} catch (Exception e) {
			// TODO: handle exception
			AppLogger.logDebug.error("dungnd:" + e.getMessage());
		}
	}
	
	public void ParseLstModelDataToLog(List<AbstractModel> lstmodel, EnumUserAction action, String form, int userId){
		for (AbstractModel model : lstmodel) {
			ParseModelDataToLog(model, action, form, userId);
		}
	}
}

