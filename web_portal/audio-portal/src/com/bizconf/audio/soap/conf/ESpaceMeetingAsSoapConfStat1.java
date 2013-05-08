/**
 * ESpaceMeetingAsSoapConfStat1.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package com.bizconf.audio.soap.conf;

public class ESpaceMeetingAsSoapConfStat1  implements java.io.Serializable {
    private java.lang.String subject;

    private java.lang.String creatorUserId;

    private java.lang.String beginDatetime;

    private java.lang.String endDatetime;

    private int phoneCount;

    private int pcCount;

    private int mcuCount;

    public ESpaceMeetingAsSoapConfStat1() {
    }

    public ESpaceMeetingAsSoapConfStat1(
           java.lang.String subject,
           java.lang.String creatorUserId,
           java.lang.String beginDatetime,
           java.lang.String endDatetime,
           int phoneCount,
           int pcCount,
           int mcuCount) {
           this.subject = subject;
           this.creatorUserId = creatorUserId;
           this.beginDatetime = beginDatetime;
           this.endDatetime = endDatetime;
           this.phoneCount = phoneCount;
           this.pcCount = pcCount;
           this.mcuCount = mcuCount;
    }


    /**
     * Gets the subject value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @return subject
     */
    public java.lang.String getSubject() {
        return subject;
    }


    /**
     * Sets the subject value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @param subject
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }


    /**
     * Gets the creatorUserId value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @return creatorUserId
     */
    public java.lang.String getCreatorUserId() {
        return creatorUserId;
    }


    /**
     * Sets the creatorUserId value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @param creatorUserId
     */
    public void setCreatorUserId(java.lang.String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }


    /**
     * Gets the beginDatetime value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @return beginDatetime
     */
    public java.lang.String getBeginDatetime() {
        return beginDatetime;
    }


    /**
     * Sets the beginDatetime value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @param beginDatetime
     */
    public void setBeginDatetime(java.lang.String beginDatetime) {
        this.beginDatetime = beginDatetime;
    }


    /**
     * Gets the endDatetime value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @return endDatetime
     */
    public java.lang.String getEndDatetime() {
        return endDatetime;
    }


    /**
     * Sets the endDatetime value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @param endDatetime
     */
    public void setEndDatetime(java.lang.String endDatetime) {
        this.endDatetime = endDatetime;
    }


    /**
     * Gets the phoneCount value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @return phoneCount
     */
    public int getPhoneCount() {
        return phoneCount;
    }


    /**
     * Sets the phoneCount value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @param phoneCount
     */
    public void setPhoneCount(int phoneCount) {
        this.phoneCount = phoneCount;
    }


    /**
     * Gets the pcCount value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @return pcCount
     */
    public int getPcCount() {
        return pcCount;
    }


    /**
     * Sets the pcCount value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @param pcCount
     */
    public void setPcCount(int pcCount) {
        this.pcCount = pcCount;
    }


    /**
     * Gets the mcuCount value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @return mcuCount
     */
    public int getMcuCount() {
        return mcuCount;
    }


    /**
     * Sets the mcuCount value for this ESpaceMeetingAsSoapConfStat1.
     * 
     * @param mcuCount
     */
    public void setMcuCount(int mcuCount) {
        this.mcuCount = mcuCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESpaceMeetingAsSoapConfStat1)) return false;
        ESpaceMeetingAsSoapConfStat1 other = (ESpaceMeetingAsSoapConfStat1) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subject==null && other.getSubject()==null) || 
             (this.subject!=null &&
              this.subject.equals(other.getSubject()))) &&
            ((this.creatorUserId==null && other.getCreatorUserId()==null) || 
             (this.creatorUserId!=null &&
              this.creatorUserId.equals(other.getCreatorUserId()))) &&
            ((this.beginDatetime==null && other.getBeginDatetime()==null) || 
             (this.beginDatetime!=null &&
              this.beginDatetime.equals(other.getBeginDatetime()))) &&
            ((this.endDatetime==null && other.getEndDatetime()==null) || 
             (this.endDatetime!=null &&
              this.endDatetime.equals(other.getEndDatetime()))) &&
            this.phoneCount == other.getPhoneCount() &&
            this.pcCount == other.getPcCount() &&
            this.mcuCount == other.getMcuCount();
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
        if (getSubject() != null) {
            _hashCode += getSubject().hashCode();
        }
        if (getCreatorUserId() != null) {
            _hashCode += getCreatorUserId().hashCode();
        }
        if (getBeginDatetime() != null) {
            _hashCode += getBeginDatetime().hashCode();
        }
        if (getEndDatetime() != null) {
            _hashCode += getEndDatetime().hashCode();
        }
        _hashCode += getPhoneCount();
        _hashCode += getPcCount();
        _hashCode += getMcuCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESpaceMeetingAsSoapConfStat1.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ConfStat1"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subject");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creatorUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creatorUserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beginDatetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "beginDatetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDatetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endDatetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phoneCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "phoneCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pcCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pcCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mcuCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mcuCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
