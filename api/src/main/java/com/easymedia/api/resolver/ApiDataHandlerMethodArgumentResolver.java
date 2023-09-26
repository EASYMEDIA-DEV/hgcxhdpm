package com.easymedia.api.resolver;

import com.easymedia.dto.EmfMap;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Component
public class ApiDataHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        System.out.println("supportsParameter");
        return methodParameter.getParameterType().equals(EmfMap.class);

        //return methodParameter.getParameterType().equals(EmfMap.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        EmfMap dataMap = new EmfMap();
        Iterator<String> pName = nativeWebRequest.getParameterNames();
        for (Iterator<String> iterator = nativeWebRequest.getHeaderNames(); iterator.hasNext();) {
            String headerName = iterator.next();
            String headerValue = nativeWebRequest.getHeader(headerName);
            //System.out.println(headerName + " : " + headerValue);
        }
        String body = "";
        if(nativeWebRequest.getParameterMap() != null && !nativeWebRequest.getParameterMap().isEmpty()){
            while (pName.hasNext())
            {
                String paramname = pName.next();
                String[] values = nativeWebRequest.getParameterValues(paramname);
                if(values.length > 1)
                {
                    dataMap.put(paramname, Arrays.asList(values));
                }
                else
                {
                    dataMap.put(paramname, values[0]);
                }
            }
        }
        else
        {
            body = getRequestBody(nativeWebRequest);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject map = mapper.readValue(body, new TypeReference<JSONObject>() {});
            dataMap = jsonToMap(map);
        }
        return dataMap;
    }

    /**
     * 웹에서 Ajax 호출시 메소드가 'put'이거나 'delete'인 경우 데이터가 담겨오는 그릇이 다르므로
     * 따로 담아서 String형태로 리턴
     *
     * @param webRequest
     * @return String
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String body = "";
        try
        {
            body =   IOUtils.toString(servletRequest.getInputStream(), "UTF-8");
        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
        finally
        {

        }
        return body;
    }

    //json을 받아 hashmap으로 변환하는 메소드
    public static EmfMap jsonToMap(JSONObject json) throws JsonEOFException {
        EmfMap retMap = new EmfMap();
        if(json != null) {
            retMap = toMap(json);
        }
        return retMap;
    }

    //json객체 안에 또다른 json 객체가 있을 경우
    public static EmfMap toMap(JSONObject object) throws JsonEOFException {
        EmfMap map = new EmfMap();
        @SuppressWarnings("rawtypes")
        Set keys = object.keySet();
        @SuppressWarnings("unchecked")
        Iterator<String> keysItr = keys.iterator();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    //json객체 안에 json 배열이 있을경우
    public static List<Object> toList(JSONArray array) throws JsonEOFException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
