package com.easymedia.api.resolver;

import com.easymedia.dto.EmfMap;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Iterator;

@Component
public class ApiDataHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(EmfMap.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        EmfMap dataMap = new EmfMap();
        Iterator<String> pName = nativeWebRequest.getParameterNames();
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
        return dataMap;
    }
}
