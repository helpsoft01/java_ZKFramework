package com.vietek.taxioperation.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
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
	private EnumUserAction action;	
	private String form;
	private int userId;
	
	@Override
	public void run(){
		this.ParseModelDataToLog();
	}
	
	public SaveLogToQueue(Object model,  EnumUserAction action, SysFunction form, int userId) {
		// TODO Auto-generated constructor stub
		this.model = (AbstractModel) model;
		this.action = action;
		this.userId = userId;
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
	public void ParseModelDataToLog(){		
		try {
			LoggingUserAction loggingUserAction = new LoggingUserAction();
			
			if (this.model != null) {
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
				loggingUserAction.setFormname(this.form);
				loggingUserAction.setAction(action.getValue());
				loggingUserAction.setFieldsdetail(names.toString());
				loggingUserAction.setValuesdetail(values.toString());
				loggingUserAction.setTimelog(new Timestamp(new Date().getTime()));
				loggingUserAction.setUserid(this.userId);	
				
			}else {
				loggingUserAction.setModelname("");
				loggingUserAction.setFormname(this.form);
				loggingUserAction.setAction(action.getValue());
				loggingUserAction.setFieldsdetail("");
				loggingUserAction.setValuesdetail("");
				loggingUserAction.setTimelog(new Timestamp(new Date().getTime()));
				loggingUserAction.setUserid(this.userId);
			}
			
			QueCommon.queueLoggingUserAction.offer(loggingUserAction);		
			
		} catch (Exception e) {
			// TODO: handle exception
			AppLogger.logDebug.error("dungnd:" + e.getMessage());
		}
	}
}

