
/**
 * ForecastDetailsType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

            
                package com.stchome.saf.messages.get._1_3;
            

            /**
            *  ForecastDetailsType bean class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ForecastDetailsType
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = ForecastDetailsType
                Namespace URI = http://saf.stchome.com/messages/get/1.3
                Namespace Prefix = ns3
                */
            

                        /**
                        * field for FamilyCode
                        */

                        
                                    protected java.lang.String localFamilyCode ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFamilyCode(){
                               return localFamilyCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FamilyCode
                               */
                               public void setFamilyCode(java.lang.String param){
                            
                                            this.localFamilyCode=param;
                                    

                               }
                            

                        /**
                        * field for DoseNumber
                        */

                        
                                    protected int localDoseNumber ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDoseNumber(){
                               return localDoseNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DoseNumber
                               */
                               public void setDoseNumber(int param){
                            
                                            this.localDoseNumber=param;
                                    

                               }
                            

                        /**
                        * field for MaxAllowableDate
                        */

                        
                                    protected com.stchome.saf.common.DateYYYYMMDDType localMaxAllowableDate ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.DateYYYYMMDDType
                           */
                           public  com.stchome.saf.common.DateYYYYMMDDType getMaxAllowableDate(){
                               return localMaxAllowableDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MaxAllowableDate
                               */
                               public void setMaxAllowableDate(com.stchome.saf.common.DateYYYYMMDDType param){
                            
                                            this.localMaxAllowableDate=param;
                                    

                               }
                            

                        /**
                        * field for MinAllowableDate
                        */

                        
                                    protected com.stchome.saf.common.DateYYYYMMDDType localMinAllowableDate ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.DateYYYYMMDDType
                           */
                           public  com.stchome.saf.common.DateYYYYMMDDType getMinAllowableDate(){
                               return localMinAllowableDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MinAllowableDate
                               */
                               public void setMinAllowableDate(com.stchome.saf.common.DateYYYYMMDDType param){
                            
                                            this.localMinAllowableDate=param;
                                    

                               }
                            

                        /**
                        * field for RecommendedDate
                        */

                        
                                    protected com.stchome.saf.common.DateYYYYMMDDType localRecommendedDate ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.DateYYYYMMDDType
                           */
                           public  com.stchome.saf.common.DateYYYYMMDDType getRecommendedDate(){
                               return localRecommendedDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RecommendedDate
                               */
                               public void setRecommendedDate(com.stchome.saf.common.DateYYYYMMDDType param){
                            
                                            this.localRecommendedDate=param;
                                    

                               }
                            

                        /**
                        * field for PastDueDate
                        */

                        
                                    protected com.stchome.saf.common.DateYYYYMMDDType localPastDueDate ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.DateYYYYMMDDType
                           */
                           public  com.stchome.saf.common.DateYYYYMMDDType getPastDueDate(){
                               return localPastDueDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PastDueDate
                               */
                               public void setPastDueDate(com.stchome.saf.common.DateYYYYMMDDType param){
                            
                                            this.localPastDueDate=param;
                                    

                               }
                            

                        /**
                        * field for ForecastCVXCode
                        */

                        
                                    protected int localForecastCVXCode ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getForecastCVXCode(){
                               return localForecastCVXCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ForecastCVXCode
                               */
                               public void setForecastCVXCode(int param){
                            
                                            this.localForecastCVXCode=param;
                                    

                               }
                            

                        /**
                        * field for DoseCount
                        */

                        
                                    protected int localDoseCount ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDoseCount(){
                               return localDoseCount;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DoseCount
                               */
                               public void setDoseCount(int param){
                            
                                            this.localDoseCount=param;
                                    

                               }
                            

                        /**
                        * field for Booster
                        */

                        
                                    protected boolean localBooster ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getBooster(){
                               return localBooster;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Booster
                               */
                               public void setBooster(boolean param){
                            
                                            this.localBooster=param;
                                    

                               }
                            

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);
            
        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://saf.stchome.com/messages/get/1.3");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":ForecastDetailsType",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "ForecastDetailsType",
                           xmlWriter);
                   }

               
                   }
               
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "familyCode", xmlWriter);
                             

                                          if (localFamilyCode==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("familyCode cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localFamilyCode);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "doseNumber", xmlWriter);
                             
                                               if (localDoseNumber==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("doseNumber cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDoseNumber));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                            if (localMaxAllowableDate==null){
                                                 throw new org.apache.axis2.databinding.ADBException("maxAllowableDate cannot be null!!");
                                            }
                                           localMaxAllowableDate.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","maxAllowableDate"),
                                               xmlWriter);
                                        
                                            if (localMinAllowableDate==null){
                                                 throw new org.apache.axis2.databinding.ADBException("minAllowableDate cannot be null!!");
                                            }
                                           localMinAllowableDate.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","minAllowableDate"),
                                               xmlWriter);
                                        
                                            if (localRecommendedDate==null){
                                                 throw new org.apache.axis2.databinding.ADBException("recommendedDate cannot be null!!");
                                            }
                                           localRecommendedDate.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","recommendedDate"),
                                               xmlWriter);
                                        
                                            if (localPastDueDate==null){
                                                 throw new org.apache.axis2.databinding.ADBException("pastDueDate cannot be null!!");
                                            }
                                           localPastDueDate.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","pastDueDate"),
                                               xmlWriter);
                                        
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "forecastCVXCode", xmlWriter);
                             
                                               if (localForecastCVXCode==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("forecastCVXCode cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForecastCVXCode));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "doseCount", xmlWriter);
                             
                                               if (localDoseCount==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("doseCount cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDoseCount));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "booster", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("booster cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBooster));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                    xmlWriter.writeEndElement();
               

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://saf.stchome.com/messages/get/1.3")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        
        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "familyCode"));
                                 
                                        if (localFamilyCode != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFamilyCode));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("familyCode cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "doseNumber"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDoseNumber));
                            
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "maxAllowableDate"));
                            
                            
                                    if (localMaxAllowableDate==null){
                                         throw new org.apache.axis2.databinding.ADBException("maxAllowableDate cannot be null!!");
                                    }
                                    elementList.add(localMaxAllowableDate);
                                
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "minAllowableDate"));
                            
                            
                                    if (localMinAllowableDate==null){
                                         throw new org.apache.axis2.databinding.ADBException("minAllowableDate cannot be null!!");
                                    }
                                    elementList.add(localMinAllowableDate);
                                
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "recommendedDate"));
                            
                            
                                    if (localRecommendedDate==null){
                                         throw new org.apache.axis2.databinding.ADBException("recommendedDate cannot be null!!");
                                    }
                                    elementList.add(localRecommendedDate);
                                
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "pastDueDate"));
                            
                            
                                    if (localPastDueDate==null){
                                         throw new org.apache.axis2.databinding.ADBException("pastDueDate cannot be null!!");
                                    }
                                    elementList.add(localPastDueDate);
                                
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "forecastCVXCode"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForecastCVXCode));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "doseCount"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDoseCount));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "booster"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBooster));
                            

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ForecastDetailsType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            ForecastDetailsType object =
                new ForecastDetailsType();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"ForecastDetailsType".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ForecastDetailsType)com.stchome.saf.messages.get._1_2.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","familyCode").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"familyCode" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFamilyCode(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","doseNumber").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"doseNumber" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDoseNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","maxAllowableDate").equals(reader.getName())){
                                
                                                object.setMaxAllowableDate(com.stchome.saf.common.DateYYYYMMDDType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","minAllowableDate").equals(reader.getName())){
                                
                                                object.setMinAllowableDate(com.stchome.saf.common.DateYYYYMMDDType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","recommendedDate").equals(reader.getName())){
                                
                                                object.setRecommendedDate(com.stchome.saf.common.DateYYYYMMDDType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","pastDueDate").equals(reader.getName())){
                                
                                                object.setPastDueDate(com.stchome.saf.common.DateYYYYMMDDType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","forecastCVXCode").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"forecastCVXCode" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setForecastCVXCode(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","doseCount").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"doseCount" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDoseCount(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","booster").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"booster" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setBooster(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                              
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    