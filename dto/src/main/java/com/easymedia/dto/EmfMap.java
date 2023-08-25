package com.easymedia.dto;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EmfMap extends ListOrderedMap {

    private static final long serialVersionUID = 1L;
    private String CAMEL_YN = "Y";

    public Object put(Object key, Object value)
    {
        return super.put( "Y".equals(CAMEL_YN) ? this.haeConvert2Camel((String)key) : (String)key, value);
    }

    public void setCamelYn(String val)
    {
        this.CAMEL_YN = val;
    }

    public Set<String> keySet()
    {
        return super.keySet();
    }

    public Object clone()
    {
        Object obj = new Object();

        try
        {
            obj = super.clone();
        }
        catch (Exception e)
        {
            e.getMessage();
        }

        return obj;
    }

    public Object get(int index)
    {
        return super.get(index);
    }

    /**
     * 맵의 데이터를 가져온다
     *
     * @param str
     * @return null 이면 ""로 받는다.
     */
    public String getString(Object str)
    {
        Object src = this.get(str);

        if (src != null && src instanceof java.math.BigDecimal)
        {
            return ((BigDecimal)src).toString();
        }

        if (src != null && src instanceof Integer)
        {
            return src.toString();
        }

        if (src != null && src instanceof Long)
        {
            return src.toString();
        }

        if (src != null && src instanceof Double)
        {
            return src.toString();
        }

        if (src != null && src instanceof LocalDateTime)
        {
            //JDK 1.8
            return ((LocalDateTime)src).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if (src == null || src.equals("null"))
        {
            return "";
        }
        else
        {
            return ((String)src).trim();
        }
    }

    /**
     * 맵의 데이터를 리스트로 가져온다
     *
     * @param str
     * @return null 이면 ""로 받는다.
     */
    public List<String> getList(Object str)
    {
        Object src = this.get(str);

        if (src == null)
        {
            return new ArrayList();
        }
        else
        {
            if(src instanceof Object[])
            {
                ArrayList list = new ArrayList(Arrays.asList((Object[])super.get(str)));
                return list;
            }
            else
            {
                ArrayList list = new ArrayList();
                list.add(src);
                return list;
            }
        }
    }

    /** Custom Function **/
    private static String haeConvert2Camel(String str)
    {
        if ((str.indexOf('_') < 0) && (Character.isLowerCase(str.charAt(0))))
        {
            return str;
        }

        StringBuilder result = new StringBuilder();

        boolean nextUpper = false;

        int len = str.length();

        for (int i = 0; i < len; i++)
        {
            char currentChar = str.charAt(i);

            if (currentChar == '_')
            {
                nextUpper = true;
            }
            else if (nextUpper)
            {
                result.append(Character.toUpperCase(currentChar));
                nextUpper = false;
            } else
            {
                result.append(Character.toLowerCase(currentChar));
            }
        }
        return result.toString();
    }
}
