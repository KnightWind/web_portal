/**
 * ESpaceMeetingAsSoapConfManagementServicebindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package com.bizconf.audio.soap.conf;

import com.bizconf.audio.component.BaseConfig;

public class ESpaceMeetingAsSoapConfManagementServicebindingStub extends org.apache.axis.client.Stub implements com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfManagementService {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[37];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
        _initOperationDesc4();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createConfId");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CreateConfIdRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfIdRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.CreateConfIdResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfIdResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CreateConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.CreateConfResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("modifyConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ModifyConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyConfRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.ModifyConfResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyConfResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("cancelConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CancelConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createCycleConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CreateCycleConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateCycleConfRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.CreateCycleConfResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateCycleConfResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("modifyCycleConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ModifyCycleConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyCycleConfRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.ModifyCycleConfResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyCycleConfResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("cancelCycleConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CancelCycleConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelCycleConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryConfInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfInfoRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfInfoResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryCycleConfInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryCycleConfInfoRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfInfoRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryCycleConfInfoResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfInfoResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryConfList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfListRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfListResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryCycleConfList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryCycleConfListRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfListRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryCycleConfListResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfListResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryConfMemberList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfMemberListRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfMemberListRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfMemberListResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfMemberListResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("startConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.StartConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("extendConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ExtendConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestExtendConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("muteConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.MuteConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("unmuteConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnmuteConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("lockConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.LockConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestLockConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("unlockConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnlockConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnlockConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("changeConfMode");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ChangeConfModeRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfModeRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("changeConfMedia");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ChangeConfMediaRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfMediaRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("inviteUsers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.InviteUsersRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestInviteUsersRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.InviteUsersResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseInviteUsersResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("kickUsers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.KickUsersRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestKickUsersRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[21] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("muteUsers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.MuteUsersRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteUsersRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[22] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("unmuteUsers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnmuteUsersRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteUsersRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[23] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("subscribeConfStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.SubscribeConfStatusRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestSubscribeConfStatusRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[24] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("unsubscribeConfStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnsubscribeConfStatusRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnsubscribeConfStatusRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[25] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("startRecordConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.StartRecordConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartRecordConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[26] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("stopRecordConf");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.StopRecordConfRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStopRecordConfRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[27] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryUsersStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryUsersStatusRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryUsersStatusRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryUsersStatusResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryUsersStatusResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[28] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("notifyTerminalType");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.NotifyTerminalTypeRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestNotifyTerminalTypeRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.NotifyTerminalTypeResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseNotifyTerminalTypeResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[29] = oper;

    }

    private static void _initOperationDesc4(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDataConfInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.GetDataConfInfoRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetDataConfInfoRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.GetDataConfInfoResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetDataConfInfoResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[30] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryConfStat1");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfStat1Request"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfStat1Request.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfStat1Response"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfStat1Response.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[31] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryConfListByPtcptUri");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfListByPtcptUriRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListByPtcptUriRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfListByPtcptUriResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListByPtcptUriResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[32] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryConfInfoByPin");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfInfoByPinRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoByPinRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfInfoByPinResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoByPinResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[33] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryConfListEx");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfListExRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListExRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfListExResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListExResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[34] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateConfMemberList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UpdateConfMemberListRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUpdateConfMemberListRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[35] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getConfRealtimeStat");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.GetConfRealtimeStatRequest"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetConfRealtimeStatRequest.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "response"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.GetConfRealtimeStatResponse"), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result"));
        oper.setReturnClass(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "_result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[36] = oper;

    }

    public ESpaceMeetingAsSoapConfManagementServicebindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ESpaceMeetingAsSoapConfManagementServicebindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ESpaceMeetingAsSoapConfManagementServicebindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.LongList");
            cachedSerQNames.add(qName);
            cls = int[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ArgList");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ConfInfo");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ConfStat1");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfStat1.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ConfStat1List");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfStat1[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ConfStat1");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.CycleConfInfo");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapCycleConfInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.DataConfInfo");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapDataConfInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.InvitedUser");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapInvitedUser.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.InvitedUserList");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapInvitedUser[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.InvitedUser");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.KWArgList");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsStringKV[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.StringKV");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.NotifyTypeList");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsStringKV[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.StringKV");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.QueryPage");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapQueryPage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CancelConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CancelCycleConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelCycleConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ChangeConfMediaRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfMediaRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ChangeConfModeRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfModeRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CreateConfIdRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfIdRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CreateConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.CreateCycleConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateCycleConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ExtendConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestExtendConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.GetConfRealtimeStatRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetConfRealtimeStatRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.GetDataConfInfoRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetDataConfInfoRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.InviteUsersRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestInviteUsersRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.KickUsersRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestKickUsersRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.LockConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestLockConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ModifyConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.ModifyCycleConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyCycleConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.MuteConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.MuteUsersRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteUsersRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.NotifyTerminalTypeRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestNotifyTerminalTypeRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfInfoByPinRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoByPinRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfInfoRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfListByPtcptUriRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListByPtcptUriRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfListExRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListExRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfListRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfMemberListRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfMemberListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryConfStat1Request");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfStat1Request.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryCycleConfInfoRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfInfoRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryCycleConfListRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.QueryUsersStatusRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryUsersStatusRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.StartConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.StartRecordConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartRecordConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.StopRecordConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStopRecordConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.SubscribeConfStatusRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestSubscribeConfStatusRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnlockConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnlockConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnmuteConfRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteConfRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnmuteUsersRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteUsersRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UnsubscribeConfStatusRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnsubscribeConfStatusRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.request.UpdateConfMemberListRequest");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUpdateConfMemberListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Requester");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequester.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.ConfInfoList");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ConfInfo");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.CreateConfIdResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfIdResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.CreateConfResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.CreateCycleConfResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateCycleConfResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.GetConfRealtimeStatResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.GetDataConfInfoResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetDataConfInfoResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.InviteUsersResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseInviteUsersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.ModifyConfResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyConfResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.ModifyCycleConfResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyCycleConfResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.NotifyTerminalTypeResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseNotifyTerminalTypeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfInfoByPinResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoByPinResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfInfoResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfListByPtcptUriResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListByPtcptUriResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfListExResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListExResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfListResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfMemberListResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfMemberListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryConfStat1Response");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfStat1Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryCycleConfInfoResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfInfoResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryCycleConfListResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.response.QueryUsersStatusResponse");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryUsersStatusResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Result");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ScheduledUser");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapScheduledUser.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ScheduledUserList");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapScheduledUser[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.ScheduledUser");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.TerminalInfo");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapTerminalInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.Token");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapToken.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.UserInfo");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapUserInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.UserStatus");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapUserStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.UserStatusList");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapUserStatus[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.soap.UserStatus");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.StringKV");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsStringKV.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.StringKVList");
            cachedSerQNames.add(qName);
            cls = com.bizconf.audio.soap.conf.ESpaceMeetingAsStringKV[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.StringKV");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("eSpaceMeeting", "eSpaceMeeting.as.StringList");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            //设置soap请求时限  默认为5s
            _call.setTimeout(BaseConfig.getInstance().getInt("time_out", 5000));
            return _call;
        }
        catch (java.lang.Exception _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult createConfId(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfIdRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseCreateConfIdResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#createConfId");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "createConfId"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfIdResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfIdResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfIdResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult createConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseCreateConfResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#createConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "createConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult modifyConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseModifyConfResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#modifyConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "modifyConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyConfResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyConfResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyConfResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult cancelConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#cancelConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "cancelConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult createCycleConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateCycleConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseCreateCycleConfResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#createCycleConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "createCycleConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateCycleConfResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateCycleConfResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateCycleConfResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult modifyCycleConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyCycleConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseModifyCycleConfResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#modifyCycleConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "modifyCycleConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyCycleConfResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyCycleConfResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyCycleConfResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult cancelCycleConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelCycleConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#cancelCycleConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "cancelCycleConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfInfo(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfInfoResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryConfInfo");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryConfInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryCycleConfInfo(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfInfoRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryCycleConfInfoResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryCycleConfInfo");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryCycleConfInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfInfoResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfInfoResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfInfoResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfListResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryConfList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryConfList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryCycleConfList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfListRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryCycleConfListResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryCycleConfList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryCycleConfList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfListResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfListResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryCycleConfListResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfMemberList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfMemberListRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfMemberListResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryConfMemberList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryConfMemberList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfMemberListResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfMemberListResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfMemberListResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult startConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#startConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "startConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult extendConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestExtendConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#extendConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "extendConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult muteConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#muteConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "muteConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unmuteConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#unmuteConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "unmuteConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult lockConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestLockConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#lockConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "lockConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unlockConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnlockConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#unlockConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "unlockConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult changeConfMode(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfModeRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#changeConfMode");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "changeConfMode"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult changeConfMedia(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfMediaRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#changeConfMedia");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "changeConfMedia"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult inviteUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestInviteUsersRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseInviteUsersResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#inviteUsers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "inviteUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseInviteUsersResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseInviteUsersResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseInviteUsersResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult kickUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestKickUsersRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#kickUsers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "kickUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult muteUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteUsersRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[22]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#muteUsers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "muteUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unmuteUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteUsersRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[23]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#unmuteUsers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "unmuteUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult subscribeConfStatus(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestSubscribeConfStatusRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[24]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#subscribeConfStatus");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "subscribeConfStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unsubscribeConfStatus(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnsubscribeConfStatusRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[25]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#unsubscribeConfStatus");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "unsubscribeConfStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult startRecordConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartRecordConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[26]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#startRecordConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "startRecordConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult stopRecordConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStopRecordConfRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[27]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#stopRecordConf");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "stopRecordConf"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryUsersStatus(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryUsersStatusRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[28]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryUsersStatus");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryUsersStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryUsersStatusResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryUsersStatusResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryUsersStatusResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult notifyTerminalType(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestNotifyTerminalTypeRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseNotifyTerminalTypeResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[29]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#notifyTerminalType");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "notifyTerminalType"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseNotifyTerminalTypeResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseNotifyTerminalTypeResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseNotifyTerminalTypeResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getDataConfInfo(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetDataConfInfoRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetDataConfInfoResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[30]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#getDataConfInfo");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "getDataConfInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetDataConfInfoResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetDataConfInfoResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetDataConfInfoResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfStat1(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfStat1Request request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfStat1ResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[31]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryConfStat1");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryConfStat1"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfStat1Response) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfStat1Response) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfStat1Response.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfListByPtcptUri(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListByPtcptUriRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfListByPtcptUriResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[32]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryConfListByPtcptUri");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryConfListByPtcptUri"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListByPtcptUriResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListByPtcptUriResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListByPtcptUriResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfInfoByPin(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoByPinRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfInfoByPinResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[33]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryConfInfoByPin");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryConfInfoByPin"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoByPinResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoByPinResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoByPinResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfListEx(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListExRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfListExResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[34]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#queryConfListEx");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "queryConfListEx"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListExResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListExResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListExResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult updateConfMemberList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUpdateConfMemberListRequest request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[35]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#updateConfMemberList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "updateConfMemberList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getConfRealtimeStat(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetConfRealtimeStatRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponseHolder response) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[36]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("eSpaceMeeting.as.soap.ConfManagementService#getConfRealtimeStat");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("eSpaceMeeting.as.soap.ConfManagementService", "getConfRealtimeStat"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse) _output.get(new javax.xml.namespace.QName("", "response"));
            } catch (java.lang.Exception _exception) {
                response.value = (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "response")), com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse.class);
            }
            try {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
