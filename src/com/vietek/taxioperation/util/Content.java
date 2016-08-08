package com.vietek.taxioperation.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

	/**
	 * Create content for send notify
	 * */

	public class Content{
		private List<String> registration_ids;
	    private Map<String,String> data;

	    public void addRegId(List<String> list){
	        if(registration_ids == null)
	            registration_ids = new LinkedList<String>();
	        registration_ids.addAll(list);
	    }

	    public void createData(String message){
	        if(data == null)
	            data = new HashMap<String,String>();

	        data.put("message", message);
	     
	    }
	    /**
		 * Create content example to send
		 * */
		public static Content createContent(String content,List<String> list) {
			Content c = new Content();
			c.addRegId(list);
			c.createData(content);
			return c;
		}
	}