/**
 * BsmswsLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class BsmswsLocator extends org.apache.axis.client.Service implements org.tempuri.Bsmsws {

    public BsmswsLocator() {
    }


    public BsmswsLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BsmswsLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for bsmswsSoap
    private java.lang.String bsmswsSoap_address = "http://221.132.39.104:8083/bsmsws.asmx";

    public java.lang.String getbsmswsSoapAddress() {
        return bsmswsSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String bsmswsSoapWSDDServiceName = "bsmswsSoap";

    public java.lang.String getbsmswsSoapWSDDServiceName() {
        return bsmswsSoapWSDDServiceName;
    }

    public void setbsmswsSoapWSDDServiceName(java.lang.String name) {
        bsmswsSoapWSDDServiceName = name;
    }

    public org.tempuri.BsmswsSoap getbsmswsSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(bsmswsSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getbsmswsSoap(endpoint);
    }

    public org.tempuri.BsmswsSoap getbsmswsSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.BsmswsSoapStub _stub = new org.tempuri.BsmswsSoapStub(portAddress, this);
            _stub.setPortName(getbsmswsSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setbsmswsSoapEndpointAddress(java.lang.String address) {
        bsmswsSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.BsmswsSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.BsmswsSoapStub _stub = new org.tempuri.BsmswsSoapStub(new java.net.URL(bsmswsSoap_address), this);
                _stub.setPortName(getbsmswsSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("bsmswsSoap".equals(inputPortName)) {
            return getbsmswsSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "bsmsws");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "bsmswsSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("bsmswsSoap".equals(portName)) {
            setbsmswsSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
