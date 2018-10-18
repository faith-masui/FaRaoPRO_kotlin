/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/masui/Documents/FaRaoPRO/Android/FaRaoPRO_GP/1.1.5/app/src/main/aidl/jp/faraopro/play/app/IBindService.aidl
 */
package jp.faraopro.play.app;
public interface IBindService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jp.faraopro.play.app.IBindService
{
private static final java.lang.String DESCRIPTOR = "jp.faraopro.play.app.IBindService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jp.faraopro.play.app.IBindService interface,
 * generating a proxy if needed.
 */
public static jp.faraopro.play.app.IBindService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jp.faraopro.play.app.IBindService))) {
return ((jp.faraopro.play.app.IBindService)iin);
}
return new jp.faraopro.play.app.IBindService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_addListener:
{
data.enforceInterface(DESCRIPTOR);
jp.faraopro.play.app.Service_Callback_IF _arg0;
_arg0 = jp.faraopro.play.app.Service_Callback_IF.Stub.asInterface(data.readStrongBinder());
this.addListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeListener:
{
data.enforceInterface(DESCRIPTOR);
jp.faraopro.play.app.Service_Callback_IF _arg0;
_arg0 = jp.faraopro.play.app.Service_Callback_IF.Stub.asInterface(data.readStrongBinder());
this.removeListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callAPI:
{
data.enforceInterface(DESCRIPTOR);
this.callAPI();
reply.writeNoException();
return true;
}
case TRANSACTION_sSignin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sSignin(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sActivation:
{
data.enforceInterface(DESCRIPTOR);
this.sActivation();
reply.writeNoException();
return true;
}
case TRANSACTION_sLogin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.sLogin(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_sLogout:
{
data.enforceInterface(DESCRIPTOR);
this.sLogout();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetStatus:
{
data.enforceInterface(DESCRIPTOR);
this.sGetStatus();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetGenreList:
{
data.enforceInterface(DESCRIPTOR);
this.sGetGenreList();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetArtistList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sGetArtistList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetHot100List:
{
data.enforceInterface(DESCRIPTOR);
this.sGetHot100List();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetMychannelList:
{
data.enforceInterface(DESCRIPTOR);
this.sGetMychannelList();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetMsg:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sGetMsg(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetPlayLists:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
this.sGetPlayLists(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_sRequestNext:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sRequestNext(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sPauseMusic:
{
data.enforceInterface(DESCRIPTOR);
this.sPauseMusic();
reply.writeNoException();
return true;
}
case TRANSACTION_sIsPlaying:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.sIsPlaying();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_sTerm:
{
data.enforceInterface(DESCRIPTOR);
this.sTerm();
reply.writeNoException();
return true;
}
case TRANSACTION_sSendDecision:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sSendDecision(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sTermMusic:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sTermMusic(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sSetMyChannel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
this.sSetMyChannel(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_sendPurchaseInfomation:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
java.lang.String _arg3;
_arg3 = data.readString();
this.sendPurchaseInfomation(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_purchaseLock:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.purchaseLock(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_purchaseCommit:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.purchaseCommit(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_purchaseCancel:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.purchaseCancel(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_oneHourNotify:
{
data.enforceInterface(DESCRIPTOR);
this.oneHourNotify();
reply.writeNoException();
return true;
}
case TRANSACTION_updateMusicInfo:
{
data.enforceInterface(DESCRIPTOR);
this.updateMusicInfo();
reply.writeNoException();
return true;
}
case TRANSACTION_sCheckPlayerInstance:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.sCheckPlayerInstance();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sGetModeType:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.sGetModeType();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_sChannelShare:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sChannelShare(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sChannelExpand:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sChannelExpand(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sCancelBoot:
{
data.enforceInterface(DESCRIPTOR);
this.sCancelBoot();
reply.writeNoException();
return true;
}
case TRANSACTION_sInterruptCancel:
{
data.enforceInterface(DESCRIPTOR);
this.sInterruptCancel();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetSession:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.sGetSession();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_sFacebookLookup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sFacebookLookup(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sFacebookLogin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.sFacebookLogin(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_sFacebookAccount:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
java.lang.String _arg4;
_arg4 = data.readString();
int _arg5;
_arg5 = data.readInt();
java.lang.String _arg6;
_arg6 = data.readString();
this.sFacebookAccount(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetFeaturedArtistList:
{
data.enforceInterface(DESCRIPTOR);
this.sGetFeaturedArtistList();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetLocationList:
{
data.enforceInterface(DESCRIPTOR);
this.sGetLocationList();
reply.writeNoException();
return true;
}
case TRANSACTION_sAdRating:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sAdRating(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sCheckTicket:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sCheckTicket(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sAddTicket:
{
data.enforceInterface(DESCRIPTOR);
this.sAddTicket();
reply.writeNoException();
return true;
}
case TRANSACTION_sSetSleepTimer:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sSetSleepTimer(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetCurrentPos:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.sGetCurrentPos();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sSearchShop:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.sSearchShop(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetBusinessList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sGetBusinessList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sPlayLocal:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sPlayLocal(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sRequestNextLocal:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sRequestNextLocal(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetTemplateList:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sGetTemplateList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sDownloadTemplateList:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.sDownloadTemplateList(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetStreamList:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sGetStreamList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sPlayStream:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sPlayStream(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sSendTemplateState:
{
data.enforceInterface(DESCRIPTOR);
this.sSendTemplateState();
reply.writeNoException();
return true;
}
case TRANSACTION_sIsInterrupt:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.sIsInterrupt();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_sCheckInterruptSchedule:
{
data.enforceInterface(DESCRIPTOR);
this.sCheckInterruptSchedule();
reply.writeNoException();
return true;
}
case TRANSACTION_sSetPreferenceBoolean:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.sSetPreferenceBoolean(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sSetPreferenceInteger:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.sSetPreferenceInteger(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sLicenseStatus:
{
data.enforceInterface(DESCRIPTOR);
this.sLicenseStatus();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetPlayerType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.sGetPlayerType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sUpdateInterruptData:
{
data.enforceInterface(DESCRIPTOR);
this.sUpdateInterruptData();
reply.writeNoException();
return true;
}
case TRANSACTION_sChangeChannelVolume:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.sChangeChannelVolume(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sChangeInterruptVolume:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.sChangeInterruptVolume(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetChannelVolume:
{
data.enforceInterface(DESCRIPTOR);
float _result = this.sGetChannelVolume();
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_sGetInterruptVolume:
{
data.enforceInterface(DESCRIPTOR);
float _result = this.sGetInterruptVolume();
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_sChangeAudioFocusedVolume:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.sChangeAudioFocusedVolume(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sGetAudioFocusedVolume:
{
data.enforceInterface(DESCRIPTOR);
float _result = this.sGetAudioFocusedVolume();
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_sIsEmergency:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.sIsEmergency();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_sPlayEmergency:
{
data.enforceInterface(DESCRIPTOR);
this.sPlayEmergency();
reply.writeNoException();
return true;
}
case TRANSACTION_sPauseEmergency:
{
data.enforceInterface(DESCRIPTOR);
this.sPauseEmergency();
reply.writeNoException();
return true;
}
case TRANSACTION_sCancelEmergency:
{
data.enforceInterface(DESCRIPTOR);
this.sCancelEmergency();
reply.writeNoException();
return true;
}
case TRANSACTION_sGetEmergencyLength:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.sGetEmergencyLength();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sGetEmergencyPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.sGetEmergencyPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sIsPlayingEmergency:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.sIsPlayingEmergency();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_sIsRecoveredEmergency:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.sIsRecoveredEmergency();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_sGetPreferenceString:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _result = this.sGetPreferenceString(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_sGetInterruptIndex:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.sGetInterruptIndex();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sGetInterruptPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.sGetInterruptPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jp.faraopro.play.app.IBindService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void addListener(jp.faraopro.play.app.Service_Callback_IF listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeListener(jp.faraopro.play.app.Service_Callback_IF listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callAPI() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callAPI, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sSignin(java.lang.String mail, java.lang.String pass) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(mail);
_data.writeString(pass);
mRemote.transact(Stub.TRANSACTION_sSignin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sActivation() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sActivation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sLogin(java.lang.String mail, java.lang.String pass, java.lang.String force) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(mail);
_data.writeString(pass);
_data.writeString(force);
mRemote.transact(Stub.TRANSACTION_sLogin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sLogout() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sLogout, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetGenreList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetGenreList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetArtistList(java.lang.String word) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(word);
mRemote.transact(Stub.TRANSACTION_sGetArtistList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetHot100List() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetHot100List, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetMychannelList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetMychannelList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetMsg(java.lang.String type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(type);
mRemote.transact(Stub.TRANSACTION_sGetMsg, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetPlayLists(int mode, int channel, java.lang.String range, int size, int permisson) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(mode);
_data.writeInt(channel);
_data.writeString(range);
_data.writeInt(size);
_data.writeInt(permisson);
mRemote.transact(Stub.TRANSACTION_sGetPlayLists, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sRequestNext(java.lang.String decison) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(decison);
mRemote.transact(Stub.TRANSACTION_sRequestNext, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sPauseMusic() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sPauseMusic, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean sIsPlaying() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sIsPlaying, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sTerm() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sTerm, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sSendDecision(java.lang.String decision) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(decision);
mRemote.transact(Stub.TRANSACTION_sSendDecision, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sTermMusic(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_sTermMusic, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sSetMyChannel(int id, java.lang.String name, int lock) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
_data.writeString(name);
_data.writeInt(lock);
mRemote.transact(Stub.TRANSACTION_sSetMyChannel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendPurchaseInfomation(java.lang.String key, int type, int market, java.lang.String receipt) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeInt(type);
_data.writeInt(market);
_data.writeString(receipt);
mRemote.transact(Stub.TRANSACTION_sendPurchaseInfomation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void purchaseLock(java.lang.String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_purchaseLock, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void purchaseCommit(java.lang.String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_purchaseCommit, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void purchaseCancel(java.lang.String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_purchaseCancel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void oneHourNotify() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_oneHourNotify, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void updateMusicInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateMusicInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int sCheckPlayerInstance() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sCheckPlayerInstance, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String sGetModeType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetModeType, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sChannelShare(java.lang.String id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(id);
mRemote.transact(Stub.TRANSACTION_sChannelShare, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sChannelExpand(java.lang.String shareKey) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(shareKey);
mRemote.transact(Stub.TRANSACTION_sChannelExpand, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sCancelBoot() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sCancelBoot, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sInterruptCancel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sInterruptCancel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String sGetSession() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetSession, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sFacebookLookup(java.lang.String email) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(email);
mRemote.transact(Stub.TRANSACTION_sFacebookLookup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sFacebookLogin(java.lang.String token, java.lang.String email, java.lang.String force) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(token);
_data.writeString(email);
_data.writeString(force);
mRemote.transact(Stub.TRANSACTION_sFacebookLogin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sFacebookAccount(java.lang.String mail, java.lang.String pass, int gender, int year, java.lang.String province, int region, java.lang.String country) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(mail);
_data.writeString(pass);
_data.writeInt(gender);
_data.writeInt(year);
_data.writeString(province);
_data.writeInt(region);
_data.writeString(country);
mRemote.transact(Stub.TRANSACTION_sFacebookAccount, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetFeaturedArtistList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetFeaturedArtistList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetLocationList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetLocationList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sAdRating(java.lang.String rating) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(rating);
mRemote.transact(Stub.TRANSACTION_sAdRating, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sCheckTicket(java.lang.String domain, java.lang.String serial) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(domain);
_data.writeString(serial);
mRemote.transact(Stub.TRANSACTION_sCheckTicket, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sAddTicket() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sAddTicket, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sSetSleepTimer(int time) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(time);
mRemote.transact(Stub.TRANSACTION_sSetSleepTimer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int sGetCurrentPos() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetCurrentPos, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sSearchShop(java.lang.String latitude, java.lang.String longitude, java.lang.String distance, java.lang.String industry) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(latitude);
_data.writeString(longitude);
_data.writeString(distance);
_data.writeString(industry);
mRemote.transact(Stub.TRANSACTION_sSearchShop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetBusinessList(java.lang.String node) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(node);
mRemote.transact(Stub.TRANSACTION_sGetBusinessList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sPlayLocal(java.lang.String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_sPlayLocal, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sRequestNextLocal(int direction) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(direction);
mRemote.transact(Stub.TRANSACTION_sRequestNextLocal, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetTemplateList(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_sGetTemplateList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sDownloadTemplateList(int type, int id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeInt(id);
mRemote.transact(Stub.TRANSACTION_sDownloadTemplateList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sGetStreamList(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_sGetStreamList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sPlayStream(int streamId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(streamId);
mRemote.transact(Stub.TRANSACTION_sPlayStream, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sSendTemplateState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sSendTemplateState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean sIsInterrupt() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sIsInterrupt, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sCheckInterruptSchedule() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sCheckInterruptSchedule, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sSetPreferenceBoolean(int type, boolean value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeInt(((value)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_sSetPreferenceBoolean, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sSetPreferenceInteger(int type, int value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeInt(value);
mRemote.transact(Stub.TRANSACTION_sSetPreferenceInteger, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sLicenseStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sLicenseStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int sGetPlayerType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetPlayerType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sUpdateInterruptData() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sUpdateInterruptData, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sChangeChannelVolume(float volume) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(volume);
mRemote.transact(Stub.TRANSACTION_sChangeChannelVolume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sChangeInterruptVolume(float volume) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(volume);
mRemote.transact(Stub.TRANSACTION_sChangeInterruptVolume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public float sGetChannelVolume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetChannelVolume, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public float sGetInterruptVolume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetInterruptVolume, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sChangeAudioFocusedVolume(float volume) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(volume);
mRemote.transact(Stub.TRANSACTION_sChangeAudioFocusedVolume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public float sGetAudioFocusedVolume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetAudioFocusedVolume, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean sIsEmergency() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sIsEmergency, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void sPlayEmergency() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sPlayEmergency, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sPauseEmergency() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sPauseEmergency, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sCancelEmergency() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sCancelEmergency, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int sGetEmergencyLength() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetEmergencyLength, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int sGetEmergencyPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetEmergencyPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean sIsPlayingEmergency() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sIsPlayingEmergency, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean sIsRecoveredEmergency() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sIsRecoveredEmergency, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String sGetPreferenceString(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_sGetPreferenceString, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int sGetInterruptIndex() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetInterruptIndex, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int sGetInterruptPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sGetInterruptPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_addListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_removeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_callAPI = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_sSignin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_sActivation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_sLogin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_sLogout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_sGetStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_sGetGenreList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_sGetArtistList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_sGetHot100List = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_sGetMychannelList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_sGetMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_sGetPlayLists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_sRequestNext = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_sPauseMusic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_sIsPlaying = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_sTerm = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_sSendDecision = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_sTermMusic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_sSetMyChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_sendPurchaseInfomation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_purchaseLock = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_purchaseCommit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_purchaseCancel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_oneHourNotify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_updateMusicInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_sCheckPlayerInstance = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_sGetModeType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_sChannelShare = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_sChannelExpand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_sCancelBoot = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_sInterruptCancel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_sGetSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_sFacebookLookup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_sFacebookLogin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_sFacebookAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_sGetFeaturedArtistList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_sGetLocationList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_sAdRating = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_sCheckTicket = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_sAddTicket = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_sSetSleepTimer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
static final int TRANSACTION_sGetCurrentPos = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
static final int TRANSACTION_sSearchShop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
static final int TRANSACTION_sGetBusinessList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
static final int TRANSACTION_sPlayLocal = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
static final int TRANSACTION_sRequestNextLocal = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
static final int TRANSACTION_sGetTemplateList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
static final int TRANSACTION_sDownloadTemplateList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
static final int TRANSACTION_sGetStreamList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
static final int TRANSACTION_sPlayStream = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
static final int TRANSACTION_sSendTemplateState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
static final int TRANSACTION_sIsInterrupt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 53);
static final int TRANSACTION_sCheckInterruptSchedule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 54);
static final int TRANSACTION_sSetPreferenceBoolean = (android.os.IBinder.FIRST_CALL_TRANSACTION + 55);
static final int TRANSACTION_sSetPreferenceInteger = (android.os.IBinder.FIRST_CALL_TRANSACTION + 56);
static final int TRANSACTION_sLicenseStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 57);
static final int TRANSACTION_sGetPlayerType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 58);
static final int TRANSACTION_sUpdateInterruptData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 59);
static final int TRANSACTION_sChangeChannelVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 60);
static final int TRANSACTION_sChangeInterruptVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 61);
static final int TRANSACTION_sGetChannelVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 62);
static final int TRANSACTION_sGetInterruptVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 63);
static final int TRANSACTION_sChangeAudioFocusedVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 64);
static final int TRANSACTION_sGetAudioFocusedVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 65);
static final int TRANSACTION_sIsEmergency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 66);
static final int TRANSACTION_sPlayEmergency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 67);
static final int TRANSACTION_sPauseEmergency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 68);
static final int TRANSACTION_sCancelEmergency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 69);
static final int TRANSACTION_sGetEmergencyLength = (android.os.IBinder.FIRST_CALL_TRANSACTION + 70);
static final int TRANSACTION_sGetEmergencyPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 71);
static final int TRANSACTION_sIsPlayingEmergency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 72);
static final int TRANSACTION_sIsRecoveredEmergency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 73);
static final int TRANSACTION_sGetPreferenceString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 74);
static final int TRANSACTION_sGetInterruptIndex = (android.os.IBinder.FIRST_CALL_TRANSACTION + 75);
static final int TRANSACTION_sGetInterruptPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 76);
}
public void addListener(jp.faraopro.play.app.Service_Callback_IF listener) throws android.os.RemoteException;
public void removeListener(jp.faraopro.play.app.Service_Callback_IF listener) throws android.os.RemoteException;
public void callAPI() throws android.os.RemoteException;
public void sSignin(java.lang.String mail, java.lang.String pass) throws android.os.RemoteException;
public void sActivation() throws android.os.RemoteException;
public void sLogin(java.lang.String mail, java.lang.String pass, java.lang.String force) throws android.os.RemoteException;
public void sLogout() throws android.os.RemoteException;
public void sGetStatus() throws android.os.RemoteException;
public void sGetGenreList() throws android.os.RemoteException;
public void sGetArtistList(java.lang.String word) throws android.os.RemoteException;
public void sGetHot100List() throws android.os.RemoteException;
public void sGetMychannelList() throws android.os.RemoteException;
public void sGetMsg(java.lang.String type) throws android.os.RemoteException;
public void sGetPlayLists(int mode, int channel, java.lang.String range, int size, int permisson) throws android.os.RemoteException;
public void sRequestNext(java.lang.String decison) throws android.os.RemoteException;
public void sPauseMusic() throws android.os.RemoteException;
public boolean sIsPlaying() throws android.os.RemoteException;
public void sTerm() throws android.os.RemoteException;
public void sSendDecision(java.lang.String decision) throws android.os.RemoteException;
public void sTermMusic(int type) throws android.os.RemoteException;
public void sSetMyChannel(int id, java.lang.String name, int lock) throws android.os.RemoteException;
public void sendPurchaseInfomation(java.lang.String key, int type, int market, java.lang.String receipt) throws android.os.RemoteException;
public void purchaseLock(java.lang.String key) throws android.os.RemoteException;
public void purchaseCommit(java.lang.String key) throws android.os.RemoteException;
public void purchaseCancel(java.lang.String key) throws android.os.RemoteException;
public void oneHourNotify() throws android.os.RemoteException;
public void updateMusicInfo() throws android.os.RemoteException;
public int sCheckPlayerInstance() throws android.os.RemoteException;
public java.lang.String sGetModeType() throws android.os.RemoteException;
public void sChannelShare(java.lang.String id) throws android.os.RemoteException;
public void sChannelExpand(java.lang.String shareKey) throws android.os.RemoteException;
public void sCancelBoot() throws android.os.RemoteException;
public void sInterruptCancel() throws android.os.RemoteException;
public java.lang.String sGetSession() throws android.os.RemoteException;
public void sFacebookLookup(java.lang.String email) throws android.os.RemoteException;
public void sFacebookLogin(java.lang.String token, java.lang.String email, java.lang.String force) throws android.os.RemoteException;
public void sFacebookAccount(java.lang.String mail, java.lang.String pass, int gender, int year, java.lang.String province, int region, java.lang.String country) throws android.os.RemoteException;
public void sGetFeaturedArtistList() throws android.os.RemoteException;
public void sGetLocationList() throws android.os.RemoteException;
public void sAdRating(java.lang.String rating) throws android.os.RemoteException;
public void sCheckTicket(java.lang.String domain, java.lang.String serial) throws android.os.RemoteException;
public void sAddTicket() throws android.os.RemoteException;
public void sSetSleepTimer(int time) throws android.os.RemoteException;
public int sGetCurrentPos() throws android.os.RemoteException;
public void sSearchShop(java.lang.String latitude, java.lang.String longitude, java.lang.String distance, java.lang.String industry) throws android.os.RemoteException;
public void sGetBusinessList(java.lang.String node) throws android.os.RemoteException;
public void sPlayLocal(java.lang.String path) throws android.os.RemoteException;
public void sRequestNextLocal(int direction) throws android.os.RemoteException;
public void sGetTemplateList(int type) throws android.os.RemoteException;
public void sDownloadTemplateList(int type, int id) throws android.os.RemoteException;
public void sGetStreamList(int type) throws android.os.RemoteException;
public void sPlayStream(int streamId) throws android.os.RemoteException;
public void sSendTemplateState() throws android.os.RemoteException;
public boolean sIsInterrupt() throws android.os.RemoteException;
public void sCheckInterruptSchedule() throws android.os.RemoteException;
public void sSetPreferenceBoolean(int type, boolean value) throws android.os.RemoteException;
public void sSetPreferenceInteger(int type, int value) throws android.os.RemoteException;
public void sLicenseStatus() throws android.os.RemoteException;
public int sGetPlayerType() throws android.os.RemoteException;
public void sUpdateInterruptData() throws android.os.RemoteException;
public void sChangeChannelVolume(float volume) throws android.os.RemoteException;
public void sChangeInterruptVolume(float volume) throws android.os.RemoteException;
public float sGetChannelVolume() throws android.os.RemoteException;
public float sGetInterruptVolume() throws android.os.RemoteException;
public void sChangeAudioFocusedVolume(float volume) throws android.os.RemoteException;
public float sGetAudioFocusedVolume() throws android.os.RemoteException;
public boolean sIsEmergency() throws android.os.RemoteException;
public void sPlayEmergency() throws android.os.RemoteException;
public void sPauseEmergency() throws android.os.RemoteException;
public void sCancelEmergency() throws android.os.RemoteException;
public int sGetEmergencyLength() throws android.os.RemoteException;
public int sGetEmergencyPosition() throws android.os.RemoteException;
public boolean sIsPlayingEmergency() throws android.os.RemoteException;
public boolean sIsRecoveredEmergency() throws android.os.RemoteException;
public java.lang.String sGetPreferenceString(int type) throws android.os.RemoteException;
public int sGetInterruptIndex() throws android.os.RemoteException;
public int sGetInterruptPosition() throws android.os.RemoteException;
}
