package com.easymedia.utility;

import com.easymedia.dto.EmfMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class COJsonUtil
{
    public static JSONObject getJsonStringFromMap(EmfMap emfMap) throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        
        if (emfMap != null)
        {
        	Iterator<String> keys = emfMap.keySet().iterator();
        	
            while (keys.hasNext())
            {
                String key = keys.next();
                Object value = emfMap.get(key);
                
                jsonObject.put(key, value);
            }
        }
        
        return jsonObject;
    }
    
    public static JSONArray getJsonArrayFromList(List<EmfMap> list) throws Exception
    {
        JSONArray jsonArray = new JSONArray();
        
        if (list != null)
        {
        	for (EmfMap map : list) 
        	{
                jsonArray.add(getJsonStringFromMap(map));
            }
        }
        
        return jsonArray;
    }
    
    public static EmfMap getMapFromJsonObject(JSONObject json) throws Exception
    {
    	EmfMap emfMap = new EmfMap();
    	
        if (json != null) 
        {
        	emfMap = toMap(json);
        }
        
        return emfMap;
    }
 
    public static List<EmfMap> getListMapFromJsonArray(JSONArray jsonArray) throws Exception
    {
        List<EmfMap> list = new ArrayList<EmfMap>();
        
        if (jsonArray != null)
        {
            for (int i = 0, len = jsonArray.size(); i < len; i++)
            {
                list.add(COJsonUtil.getMapFromJsonObject((JSONObject) jsonArray.get(i)));
            }
        }
        
        return list;
    }
    
	public static EmfMap toMap(JSONObject object) throws Exception
    {
    	EmfMap emfMap = new EmfMap();

    	if (object != null)
    	{
    		Iterator<String> keysItr = object.entrySet().iterator();
            
            while (keysItr.hasNext()) 
            {
                String key = keysItr.next();
                Object value = object.get(key);

                if (value instanceof JSONArray)
                {
                    value = toList((JSONArray) value);
                }
                else if (value instanceof JSONObject) 
                {
                    value = toMap((JSONObject) value);
                }
                
                emfMap.put(key, value);
            }
    	}

    	return emfMap;
    }
    
    public static List<Object> toList(JSONArray array) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        
        if (array != null)
        {
        	for (int i = 0, len = array.size(); i < len; i++) 
        	{
                Object value = array.get(i);
                
                if (value instanceof JSONArray) 
                {
                    value = toList((JSONArray) value);
                }
                else if(value instanceof JSONObject) 
                {
                    value = toMap((JSONObject) value);
                }
                
                list.add(value);
            }
        }
        
        return list;
    }
}