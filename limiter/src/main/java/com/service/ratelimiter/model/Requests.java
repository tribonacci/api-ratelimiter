package com.service.ratelimiter.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class Requests {
	
	@Autowired
	ApiLimits limits;
	
	Map<Identity, List<Long>> requests;
	public Requests(){
		requests = new HashMap<Identity,List<Long>>();
	}
	
	public void clearRequest(Identity req,Long timestamp){
		int urlcountThresold = 0;
		long timeThresold = 0;
		
		List<Long> timeStampList = requests.get(req);
		List<Long> updatedtimeStampList = new LinkedList<Long>();
		
//		for(int i=0;i<timeStampList.size();i++){
//			long timediff = timestamp - timeStampList.get(i);
//			if(timediff > timeThresold){
//				//Remove from list
//				
//			}
//			else{
//				break;
//			}
//		}
		
		while(timeStampList.size()!=0){
			long timediff = timestamp - timeStampList.get(0);
			if(timediff > timeThresold){
				timeStampList.remove(0);
			}
			else{
				break;
			}
		}
		timeStampList.add(timestamp);	
	}
	
	public void addRequest(Identity req,Long timestamp){
		//Get urlcount and timestamp thresold
		if(!requests.containsKey(req)){
			List<Long> lis = new LinkedList<Long>();
			lis.add(timestamp);
			requests.put(req,lis);
		}		
		else{
			clearRequest(req,timestamp);
		}
	}
	
	public boolean IsValid(Identity idt, Long timestamp) {
		int limit = limits.GetLimit(idt.getApiUri());
		
		if(requests.get(idt).size() < limit) {
			this.addRequest(idt, timestamp);
			return true;
		}
		return false;
		
	}
}
