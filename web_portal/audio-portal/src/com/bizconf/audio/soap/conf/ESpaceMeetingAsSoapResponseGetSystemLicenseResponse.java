/**
 * ESpaceMeetingAsSoapResponseGetSystemLicenseResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package com.bizconf.audio.soap.conf;

public class ESpaceMeetingAsSoapResponseGetSystemLicenseResponse  implements java.io.Serializable {
    private com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapLicenseRes lic;

    public ESpaceMeetingAsSoapResponseGetSystemLicenseResponse() {
    }

    public ESpaceMeetingAsSoapResponseGetSystemLicenseResponse(
           com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapLicenseRes lic) {
           this.lic = lic;
    }


    /**
     * Gets the lic value for this ESpaceMeetingAsSoapResponseGetSystemLicenseResponse.
     * 
     * @return lic
     */
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapLicenseRes getLic() {
        return lic;
    }


    /**
     * Sets the lic value for this ESpaceMeetingAsSoapResponseGetSystemLicenseResponse.
     * 
     * @param lic
     */
    public void setLic(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapLicenseRes lic) {
        this.lic = lic;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESpaceMeetingAsSoapResponseGetSystemLicenseResponse)) return false;
        ESpaceMeetingAsSoapResponseGetSystemLicenseResponse other = (ESpaceMeetingAsSoapResponseGetSystemLicenseResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lic==null && other.getLic()==null) || 
             (this.lic!=null &&
              this.lic.equals(other.getLic())));
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
        if (getLic() != null) {
            _hashCode += getLic().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESpaceMeetingAsSoapResponseGetSystemLicenseResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.GetSystemLicenseResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lic");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lic"));
        elemField.setXmlType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.LicenseRes"));
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
