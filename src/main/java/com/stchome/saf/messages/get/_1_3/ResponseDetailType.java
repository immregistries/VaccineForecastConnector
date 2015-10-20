
/**
 * ResponseDetailType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

            
                package com.stchome.saf.messages.get._1_3;
            

            /**
            *  ResponseDetailType bean class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ResponseDetailType
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = ResponseDetailType
                Namespace URI = http://saf.stchome.com/messages/get/1.3
                Namespace Prefix = ns3
                */
            

                        /**
                        * field for PersonDetails
                        */

                        
                                    protected com.stchome.saf.messages.get._1_3.PersonDetailsType localPersonDetails ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.messages.get._1_3.PersonDetailsType
                           */
                           public  com.stchome.saf.messages.get._1_3.PersonDetailsType getPersonDetails(){
                               return localPersonDetails;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PersonDetails
                               */
                               public void setPersonDetails(com.stchome.saf.messages.get._1_3.PersonDetailsType param){
                            
                                            this.localPersonDetails=param;
                                    

                               }
                            

                        /**
                        * field for ForecastDetails
                        * This was an Array!
                        */

                        
                                    protected com.stchome.saf.messages.get._1_3.ForecastDetailsType[] localForecastDetails ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localForecastDetailsTracker = false ;

                           public boolean isForecastDetailsSpecified(){
                               return localForecastDetailsTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.messages.get._1_3.ForecastDetailsType[]
                           */
                           public  com.stchome.saf.messages.get._1_3.ForecastDetailsType[] getForecastDetails(){
                               return localForecastDetails;
                           }

                           
                        


                               
                              /**
                               * validate the array for ForecastDetails
                               */
                              protected void validateForecastDetails(com.stchome.saf.messages.get._1_3.ForecastDetailsType[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param ForecastDetails
                              */
                              public void setForecastDetails(com.stchome.saf.messages.get._1_3.ForecastDetailsType[] param){
                              
                                   validateForecastDetails(param);

                               localForecastDetailsTracker = param != null;
                                      
                                      this.localForecastDetails=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.stchome.saf.messages.get._1_3.ForecastDetailsType
                             */
                             public void addForecastDetails(com.stchome.saf.messages.get._1_3.ForecastDetailsType param){
                                   if (localForecastDetails == null){
                                   localForecastDetails = new com.stchome.saf.messages.get._1_3.ForecastDetailsType[]{};
                                   }

                            
                                 //update the setting tracker
                                localForecastDetailsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localForecastDetails);
                               list.add(param);
                               this.localForecastDetails =
                             (com.stchome.saf.messages.get._1_3.ForecastDetailsType[])list.toArray(
                            new com.stchome.saf.messages.get._1_3.ForecastDetailsType[list.size()]);

                             }
                             

                        /**
                        * field for Information
                        */

                        
                                    protected java.lang.String localInformation ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localInformationTracker = false ;

                           public boolean isInformationSpecified(){
                               return localInformationTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getInformation(){
                               return localInformation;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Information
                               */
                               public void setInformation(java.lang.String param){
                            localInformationTracker = param != null;
                                   
                                            this.localInformation=param;
                                    

                               }
                            

                        /**
                        * field for ReleaseVersion
                        */

                        
                                    protected java.lang.String localReleaseVersion ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localReleaseVersionTracker = false ;

                           public boolean isReleaseVersionSpecified(){
                               return localReleaseVersionTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getReleaseVersion(){
                               return localReleaseVersion;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ReleaseVersion
                               */
                               public void setReleaseVersion(java.lang.String param){
                            localReleaseVersionTracker = param != null;
                                   
                                            this.localReleaseVersion=param;
                                    

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
                           namespacePrefix+":ResponseDetailType",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "ResponseDetailType",
                           xmlWriter);
                   }

               
                   }
               
                                            if (localPersonDetails==null){
                                                 throw new org.apache.axis2.databinding.ADBException("personDetails cannot be null!!");
                                            }
                                           localPersonDetails.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","personDetails"),
                                               xmlWriter);
                                         if (localForecastDetailsTracker){
                                       if (localForecastDetails!=null){
                                            for (int i = 0;i < localForecastDetails.length;i++){
                                                if (localForecastDetails[i] != null){
                                                 localForecastDetails[i].serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","forecastDetails"),
                                                           xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("forecastDetails cannot be null!!");
                                        
                                    }
                                 } if (localInformationTracker){
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "information", xmlWriter);
                             

                                          if (localInformation==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("information cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localInformation);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localReleaseVersionTracker){
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "releaseVersion", xmlWriter);
                             

                                          if (localReleaseVersion==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("releaseVersion cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localReleaseVersion);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
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
                                                                      "personDetails"));
                            
                            
                                    if (localPersonDetails==null){
                                         throw new org.apache.axis2.databinding.ADBException("personDetails cannot be null!!");
                                    }
                                    elementList.add(localPersonDetails);
                                 if (localForecastDetailsTracker){
                             if (localForecastDetails!=null) {
                                 for (int i = 0;i < localForecastDetails.length;i++){

                                    if (localForecastDetails[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                          "forecastDetails"));
                                         elementList.add(localForecastDetails[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("forecastDetails cannot be null!!");
                                    
                             }

                        } if (localInformationTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "information"));
                                 
                                        if (localInformation != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInformation));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("information cannot be null!!");
                                        }
                                    } if (localReleaseVersionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "releaseVersion"));
                                 
                                        if (localReleaseVersion != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReleaseVersion));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("releaseVersion cannot be null!!");
                                        }
                                    }

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
        public static ResponseDetailType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            ResponseDetailType object =
                new ResponseDetailType();

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
                    
                            if (!"ResponseDetailType".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ResponseDetailType)com.stchome.saf.messages.get._1_2.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                        java.util.ArrayList list2 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","personDetails").equals(reader.getName())){
                                
                                                object.setPersonDetails(com.stchome.saf.messages.get._1_3.PersonDetailsType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","forecastDetails").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list2.add(com.stchome.saf.messages.get._1_3.ForecastDetailsType.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone2 = false;
                                                        while(!loopDone2){
                                                            // We should be at the end element, but make sure
                                                            while (!reader.isEndElement())
                                                                reader.next();
                                                            // Step out of this element
                                                            reader.next();
                                                            // Step to next element event.
                                                            while (!reader.isStartElement() && !reader.isEndElement())
                                                                reader.next();
                                                            if (reader.isEndElement()){
                                                                //two continuous end elements means we are exiting the xml structure
                                                                loopDone2 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","forecastDetails").equals(reader.getName())){
                                                                    list2.add(com.stchome.saf.messages.get._1_3.ForecastDetailsType.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone2 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setForecastDetails((com.stchome.saf.messages.get._1_3.ForecastDetailsType[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.stchome.saf.messages.get._1_3.ForecastDetailsType.class,
                                                                list2));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","information").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"information" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInformation(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","releaseVersion").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"releaseVersion" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setReleaseVersion(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
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
           
    