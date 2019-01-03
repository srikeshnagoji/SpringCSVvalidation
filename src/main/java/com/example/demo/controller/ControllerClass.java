package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.ValidationRepo;
import com.example.demo.validation.WorkRequestTypeMetaData;

@RestController
public class ControllerClass {

	@Autowired
	ValidationRepo vr;
	
	//Map<String,Object> hm=new HashMap<String,Object>();
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public void addValues(@RequestBody HashMap<String,Object> hm) {

		Object value = null;
		System.out.println(hm.toString());
		if(hm.containsKey("WorkRequestType"))
		{
			value=(String)hm.get("WorkRequestType");
			System.out.println("WorkRequestType: "+value);
		}
		
		List<WorkRequestTypeMetaData> list=vr.findByWorkRequestType((String) value);
		System.out.println(list);
//		System.out.println(hm.get(1));
//		System.out.println(hm.get(2));
//		hm.put("ECN","12345");
//		hm.put("NAME","SRIKESH");
//		hm.put("DOB","04-02-1997");
		
	}
}