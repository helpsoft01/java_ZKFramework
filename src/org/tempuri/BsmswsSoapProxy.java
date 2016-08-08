package org.tempuri;

public class BsmswsSoapProxy implements org.tempuri.BsmswsSoap {
  private String _endpoint = null;
  private org.tempuri.BsmswsSoap bsmswsSoap = null;
  
  public BsmswsSoapProxy() {
    _initBsmswsSoapProxy();
  }
  
  public BsmswsSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initBsmswsSoapProxy();
  }
  
  private void _initBsmswsSoapProxy() {
    try {
      bsmswsSoap = (new org.tempuri.BsmswsLocator()).getbsmswsSoap();
      if (bsmswsSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)bsmswsSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)bsmswsSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (bsmswsSoap != null)
      ((javax.xml.rpc.Stub)bsmswsSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.BsmswsSoap getBsmswsSoap() {
    if (bsmswsSoap == null)
      _initBsmswsSoapProxy();
    return bsmswsSoap;
  }
  
  public int sendBrandSms(java.lang.String username, java.lang.String password, java.lang.String phonenumber, java.lang.String message, java.lang.String brandname, int loaitin) throws java.rmi.RemoteException{
    if (bsmswsSoap == null)
      _initBsmswsSoapProxy();
    return bsmswsSoap.sendBrandSms(username, password, phonenumber, message, brandname, loaitin);
  }
  
  public int sendBrandSmsQC(java.lang.String username, java.lang.String password, java.lang.String billid, java.lang.String billname, java.lang.String phonenumber, java.lang.String message, java.lang.String brandname, java.lang.String sendtime) throws java.rmi.RemoteException{
    if (bsmswsSoap == null)
      _initBsmswsSoapProxy();
    return bsmswsSoap.sendBrandSmsQC(username, password, billid, billname, phonenumber, message, brandname, sendtime);
  }
  
  public int UPDATE_STATUS(java.lang.String username, java.lang.String password, java.lang.String idrequest, java.lang.String brandname) throws java.rmi.RemoteException{
    if (bsmswsSoap == null)
      _initBsmswsSoapProxy();
    return bsmswsSoap.UPDATE_STATUS(username, password, idrequest, brandname);
  }
  
  
}