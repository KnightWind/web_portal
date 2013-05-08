/**
 * 
 */
package com.bizconf.audio.util;

/**
 * ����java����ת����json��ݸ�ʽʱ��Ҫ�����ڸ�ʽ�����ø�ʽ���ࣺJsonDateValueProcessor   
 * @author shhc
 *
 */
 
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
     
/*    
 * @author johncon    
 * �������� 2008-9-10    
 * json����ֵ������    
 */     
public class JsonDateValueProcessor implements JsonValueProcessor {      
     
    private String format = "yyyy-MM-dd HH:mm:ss";      
     
    public JsonDateValueProcessor() {      
     
    }      
     
    public JsonDateValueProcessor(String format) {      
        this.format = format;      
    }      
     
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {      
        return process(value, jsonConfig);      
    }      
     
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {      
        return process(value, jsonConfig);      
    }      
          
    private Object process( Object value, JsonConfig jsonConfig ) {      
        if (value instanceof Date) {      
            String str = new SimpleDateFormat(format).format((Date) value);      
            return str;      
        }      
        return value == null ? null : value.toString();      
    }      
     
    public String getFormat() {      
        return format;      
    }      
     
    public void setFormat(String format) {      
        this.format = format;      
    }      
     
}    
