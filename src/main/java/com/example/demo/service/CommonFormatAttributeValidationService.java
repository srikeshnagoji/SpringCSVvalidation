package com.example.demo.service;

import com.example.demo.exceptions.ValidationException;
import com.example.demo.model.Standard_Object_Attributes;
import com.example.demo.model.Validation_Master;
import com.example.demo.model.Work_Request_Type_Master;
import com.example.demo.pojos.CommonFormatAttributeMetaData;
import com.example.demo.pojos.WorkRequestTypeMetaData;
import com.example.demo.repos.StandardObjAttrRepo;
import com.example.demo.repos.ValidationMasterRepo;
import com.example.demo.repos.WorkReqTypeMasterRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class CommonFormatAttributeValidationService {
	@Autowired
	private WorkReqTypeMasterRepo workReqTypeMasterRepo;
	@Autowired
	private StandardObjAttrRepo standardObjAttrRepo;
	@Autowired
	private ValidationMasterRepo validationMasterRepo;

	private boolean flag;
	public HashMap<String, Object> validate(HashMap<String, Object> hm, String workrequestType)
			throws ValidationException {
		
		
		WorkRequestTypeMetaData wrtypemetadata = this.getAttributesForWorkRequestType(workrequestType);
		//
		// validate for :
		// 1. mandatory fields (required or not)
		// first we shall clone the map.
		HashMap<String, Object> hm2 =  (HashMap<String, Object>) hm.clone();
		// check if all the mandatory fields are present or not.


		for (CommonFormatAttributeMetaData commonFormatAttributeMetaData : wrtypemetadata.getAttributes()) {
			if (commonFormatAttributeMetaData.isRequired()) {
				//////// --------
				flag = hm2.containsKey(commonFormatAttributeMetaData.getAttributeName());
				if (flag == false) {
					throw new ValidationException("mandatory field not present.", commonFormatAttributeMetaData.getAttributeName());
				}

				hm2.remove(commonFormatAttributeMetaData.getAttributeName());
			} else {
				hm2.remove(commonFormatAttributeMetaData.getAttributeName());
			}

		}

		boolean ignoreOtherAttr = wrtypemetadata.isIgnoreAdditionalAttributes();
		// 2. check for ignore other fields (clone the akar map and use hashmap to find
		// out left over ones and to do this we need all the entries in common attr
		// table)
		// now we have leftover fields in cloned map.
		// see if known fields are there in these leftover fields.

		if (!ignoreOtherAttr) {
			// check for known attr.
			List<Standard_Object_Attributes> allAttrList = standardObjAttrRepo.findAll();
			// now we have all std attr list.
			String attrname;
			for (Standard_Object_Attributes standard_Object_Attributes : allAttrList) {
				attrname = standard_Object_Attributes.getCommonFormatFieldName();
				hm2.remove(attrname);
			}

			
			if (hm2.size() > 0) {
				throw new ValidationException("Unknown attributes present.", hm2.keySet().iterator().next());
			} //
			return hm;// return validated hash map.
		} else {
			System.out.println(hm2.toString());//
			return hm;// return validated hashmap.
		}
	}

	private WorkRequestTypeMetaData getAttributesForWorkRequestType(String workRequestType) {
	    //
	        // get all the common format attr for that work req type
	        //first find out wr ID using the known wr type.
	    	System.out.println(workRequestType);
	        Work_Request_Type_Master workReqTypeobj=workReqTypeMasterRepo.findByWorkRequestType(workRequestType);
	        int wid=workReqTypeobj.getwId();
	        System.out.println("workreq id: "+wid);
	        //using this ID get all the mandatory fields relating to this wr id.
	        List<CommonFormatAttributeMetaData> cfam=new ArrayList<CommonFormatAttributeMetaData>();
	        List<Validation_Master> validationMasterlist=validationMasterRepo.findAllByWorkRequestTypeId_wId(wid);//
			for (Validation_Master validation_Master : validationMasterlist) {
	            Standard_Object_Attributes soa=validation_Master.getcFField();
	            boolean isreq=validation_Master.isRequired();

	            //construct CommonFormatAttributeMetaData object and add it to list cfam.
	            cfam.add(new CommonFormatAttributeMetaData(soa.getCommonFormatFieldName(),isreq,null));

			}//now we have a list containing objects of CommonFormatAttributeMetaData type.
	        System.out.println("list conatining the common format attr relating to that particular wr type"+cfam);


			//get values to populate WorkRequestTypeMetaData
//	      Object[] o=new Object[cfam.size()];//conversion form list to array
	        CommonFormatAttributeMetaData[] wrtm = cfam.toArray(new CommonFormatAttributeMetaData[cfam.size()]);
//	        CommonFormatAttributeMetaData[] wrtm=(CommonFormatAttributeMetaData[]) cfam.toArray();

	        WorkRequestTypeMetaData wrtypemetadata = new WorkRequestTypeMetaData(workRequestType,wid,workReqTypeobj.getIgnoreAditionalAttributes(),wrtm);
	        //using "wrtypemetadata" object created.. validate for mandatory fields.
	        //
	        return wrtypemetadata;
		}
}
