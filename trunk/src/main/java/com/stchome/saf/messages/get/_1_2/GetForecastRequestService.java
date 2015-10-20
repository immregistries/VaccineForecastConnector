

/**
 * GetForecastRequestService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.stchome.saf.messages.get._1_2;

    /*
     *  GetForecastRequestService java interface
     */

    public interface GetForecastRequestService {
          

        /**
          * Auto generated method signature
          * 
                    * @param getForecastRequest0
                
         */

         
                     public com.stchome.saf.messages.get._1_3.GetForecastResponse getForecast(

                        com.stchome.saf.messages.get._1_3.GetForecastRequest getForecastRequest0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getForecastRequest0
            
          */
        public void startgetForecast(

            com.stchome.saf.messages.get._1_3.GetForecastRequest getForecastRequest0,

            final com.stchome.saf.messages.get._1_2.GetForecastRequestServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    