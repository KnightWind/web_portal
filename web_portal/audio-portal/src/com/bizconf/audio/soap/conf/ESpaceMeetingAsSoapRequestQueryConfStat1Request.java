/**
 * ESpaceMeetingAsSoapRequestQueryConfStat1Request.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package com.bizconf.audio.soap.conf;

public class ESpaceMeetingAsSoapRequestQueryConfStat1Request  implements java.io.Serializable {
    private com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapToken token;

    private com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequester requester;

    private com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapQueryPage page;

    public ESpaceMeetingAsSoapRequestQueryConfStat1Request() {
    }

    public ESpaceMeetingAsSoapRequestQueryConfStat1Request(
           com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapToken token,
           com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequester requester,
           com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapQueryPage page) {
           this.token = token;
           this.requester = requester;
           this.page = page;
    }


    /**
     * Gets the token value for this ESpaceMeetingAsSoapRequestQueryConfStat1Request.
     * 
     * @return token
     */
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapToken getToken() {
        return token;
    }


    /**
     * Sets the token value for this ESpaceMeetingAsSoapRequestQueryConfStat1Request.
     * 
     * @param token
     */
    public void setToken(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapToken token) {
        this.token = token;
    }


    /**
     * Gets the requester value for this ESpaceMeetingAsSoapRequestQueryConfStat1Request.
     * 
     * @return requester
     */
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequester getRequester() {
        return requester;
    }


    /**
     * Sets the requester value for this ESpaceMeetingAsSoapRequestQueryConfStat1Request.
     * 
     * @param requester
     */
    public void setRequester(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequester requester) {
        this.requester = requester;
    }


    /**
     * Gets the page value for this ESpaceMeetingAsSoapRequestQueryConfStat1Request.
     * 
     * @return page
     */
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapQueryPage getPage() {
        return page;
    }


    /**
     * Sets the page value for this ESpaceMeetingAsSoapRequestQueryConfStat1Request.
     * 
     * @param page
     */
    public void setPage(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapQueryPage page) {
        this.page = page;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESpaceMeetingAsSoapRequestQueryConfStat1Request)) return false;
        ESpaceMeetingAsSoapRequestQueryConfStat1Request other = (ESpaceMeetingAsSoapRequestQueryConfStat1Request) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken()))) &&
            ((this.requester==null && other.getRequester()==null) || 
             (this.requester!=null &&
              this.requester.equals(other.getRequester()))) &&
            ((this.page==null && other.getPage()==null) || 
             (this.page!=null &&
              this.page.equals(other.getPage())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        if (getRequester() != null) {
            _hashCode += getRequester().hashCode();
        }
        if (getPage() != null) {
            _hashCode += getPage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESpaceMeetingAsSoapRequestQueryConfStat1Request.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfStat1Request"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("", "token"));
        elemField.setXmlType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Token"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requester");
        elemField.setXmlName(new javax.xml.namespace.QName("", "requester"));
        elemField.setXmlType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Requester"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("page");
        elemField.setXmlName(new javax.xml.namespace.QName("", "page"));
        elemField.setXmlType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.QueryPage"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
