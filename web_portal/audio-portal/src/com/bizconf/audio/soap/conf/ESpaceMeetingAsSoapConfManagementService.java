/**
 * ESpaceMeetingAsSoapConfManagementService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package com.bizconf.audio.soap.conf;

public interface ESpaceMeetingAsSoapConfManagementService extends java.rmi.Remote {
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult createConfId(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfIdRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseCreateConfIdResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult createConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseCreateConfResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult modifyConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseModifyConfResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult cancelConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult createCycleConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateCycleConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseCreateCycleConfResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult modifyCycleConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyCycleConfRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseModifyCycleConfResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult cancelCycleConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelCycleConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfInfo(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfInfoResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryCycleConfInfo(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfInfoRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryCycleConfInfoResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfListResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryCycleConfList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryCycleConfListRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryCycleConfListResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfMemberList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfMemberListRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfMemberListResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult startConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult extendConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestExtendConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult muteConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unmuteConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult lockConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestLockConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unlockConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnlockConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult changeConfMode(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfModeRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult changeConfMedia(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestChangeConfMediaRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult inviteUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestInviteUsersRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseInviteUsersResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult kickUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestKickUsersRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult muteUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestMuteUsersRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unmuteUsers(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnmuteUsersRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult subscribeConfStatus(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestSubscribeConfStatusRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult unsubscribeConfStatus(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUnsubscribeConfStatusRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult startRecordConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartRecordConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult stopRecordConf(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStopRecordConfRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryUsersStatus(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryUsersStatusRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult notifyTerminalType(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestNotifyTerminalTypeRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseNotifyTerminalTypeResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getDataConfInfo(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetDataConfInfoRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetDataConfInfoResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfStat1(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfStat1Request request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfStat1ResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfListByPtcptUri(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListByPtcptUriRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfListByPtcptUriResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfInfoByPin(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoByPinRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfInfoByPinResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult queryConfListEx(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListExRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfListExResponseHolder response) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult updateConfMemberList(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUpdateConfMemberListRequest request) throws java.rmi.RemoteException;
    public com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult getConfRealtimeStat(com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetConfRealtimeStatRequest request, com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponseHolder response) throws java.rmi.RemoteException;
}
