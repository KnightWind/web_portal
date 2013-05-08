/**
 * ESpaceMeetingAsSoapBaseService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package com.bizconf.audio.soap.conf;

public interface ESpaceMeetingAsSoapBaseService extends java.rmi.Remote {
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getAppkey(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetAppkeyRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetAppkeyResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult heartbeat(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestHeartbeatRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult setSystemParam(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestSetSystemParamRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getSystemParam(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetSystemParamRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetSystemParamResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getSystemLicense(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetSystemLicenseRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetSystemLicenseResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getLicenseInfo(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetLicenseInfoRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetLicenseInfoResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getESN(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetESNRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetESNResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult activeLicense(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestActiveLicenseRequest request) throws java.rmi.RemoteException;
}
