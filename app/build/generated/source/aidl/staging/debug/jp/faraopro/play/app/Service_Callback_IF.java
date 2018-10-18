/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/masui/Documents/FaRaoPRO/Android/FaRaoPRO_GP/1.1.5/app/src/main/aidl/jp/faraopro/play/app/Service_Callback_IF.aidl
 */
package jp.faraopro.play.app;
public interface Service_Callback_IF extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jp.faraopro.play.app.Service_Callback_IF
{
private static final java.lang.String DESCRIPTOR = "jp.faraopro.play.app.Service_Callback_IF";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jp.faraopro.play.app.Service_Callback_IF interface,
 * generating a proxy if needed.
 */
public static jp.faraopro.play.app.Service_Callback_IF asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jp.faraopro.play.app.Service_Callback_IF))) {
return ((jp.faraopro.play.app.Service_Callback_IF)iin);
}
return new jp.faraopro.play.app.Service_Callback_IF.Stub.Proxy(obj);
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
case TRANSACTION_onNotifyResult:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.onNotifyResult(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onNotifyChannelList:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
jp.faraopro.play.domain.ListDataInfo _arg2;
if ((0!=data.readInt())) {
_arg2 = jp.faraopro.play.domain.ListDataInfo.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
this.onNotifyChannelList(_arg0, _arg1, _arg2);
reply.writeNoException();
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_onNotifyMusicData:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
jp.faraopro.play.domain.MusicInfo _arg2;
if ((0!=data.readInt())) {
_arg2 = jp.faraopro.play.domain.MusicInfo.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
this.onNotifyMusicData(_arg0, _arg1, _arg2);
reply.writeNoException();
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_onNotifyWithString:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
this.onNotifyWithString(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onNotifyMCItemList:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
jp.faraopro.play.domain.MCListDataInfo _arg2;
if ((0!=data.readInt())) {
_arg2 = jp.faraopro.play.domain.MCListDataInfo.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
this.onNotifyMCItemList(_arg0, _arg1, _arg2);
reply.writeNoException();
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_onNotifyUserData:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
jp.faraopro.play.model.FROUserData _arg2;
if ((0!=data.readInt())) {
_arg2 = jp.faraopro.play.model.FROUserData.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
this.onNotifyUserData(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jp.faraopro.play.app.Service_Callback_IF
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
@Override public void onNotifyResult(int when, int statusCode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(when);
_data.writeInt(statusCode);
mRemote.transact(Stub.TRANSACTION_onNotifyResult, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onNotifyChannelList(int when, int statusCode, jp.faraopro.play.domain.ListDataInfo list) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(when);
_data.writeInt(statusCode);
if ((list!=null)) {
_data.writeInt(1);
list.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNotifyChannelList, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
list.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onNotifyMusicData(int when, int statusCode, jp.faraopro.play.domain.MusicInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(when);
_data.writeInt(statusCode);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNotifyMusicData, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
info.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onNotifyWithString(int when, int statusCode, java.lang.String data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(when);
_data.writeInt(statusCode);
_data.writeString(data);
mRemote.transact(Stub.TRANSACTION_onNotifyWithString, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onNotifyMCItemList(int when, int statusCode, jp.faraopro.play.domain.MCListDataInfo list) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(when);
_data.writeInt(statusCode);
if ((list!=null)) {
_data.writeInt(1);
list.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNotifyMCItemList, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
list.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onNotifyUserData(int when, int statusCode, jp.faraopro.play.model.FROUserData data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(when);
_data.writeInt(statusCode);
if ((data!=null)) {
_data.writeInt(1);
data.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNotifyUserData, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onNotifyResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onNotifyChannelList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onNotifyMusicData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onNotifyWithString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onNotifyMCItemList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onNotifyUserData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void onNotifyResult(int when, int statusCode) throws android.os.RemoteException;
public void onNotifyChannelList(int when, int statusCode, jp.faraopro.play.domain.ListDataInfo list) throws android.os.RemoteException;
public void onNotifyMusicData(int when, int statusCode, jp.faraopro.play.domain.MusicInfo info) throws android.os.RemoteException;
public void onNotifyWithString(int when, int statusCode, java.lang.String data) throws android.os.RemoteException;
public void onNotifyMCItemList(int when, int statusCode, jp.faraopro.play.domain.MCListDataInfo list) throws android.os.RemoteException;
public void onNotifyUserData(int when, int statusCode, jp.faraopro.play.model.FROUserData data) throws android.os.RemoteException;
}
