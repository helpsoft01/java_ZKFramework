/**
 * BsmswsSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface BsmswsSoap extends java.rmi.Remote {
    public int sendBrandSms(java.lang.String username, java.lang.String password, java.lang.String phonenumber, java.lang.String message, java.lang.String brandname, int loaitin) throws java.rmi.RemoteException;
    public int sendBrandSmsQC(java.lang.String username, java.lang.String password, java.lang.String billid, java.lang.String billname, java.lang.String phonenumber, java.lang.String message, java.lang.String brandname, java.lang.String sendtime) throws java.rmi.RemoteException;
    public int UPDATE_STATUS(java.lang.String username, java.lang.String password, java.lang.String idrequest, java.lang.String brandname) throws java.rmi.RemoteException;
}
