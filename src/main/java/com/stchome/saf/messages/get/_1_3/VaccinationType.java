
/**
 * VaccinationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

            
                package com.stchome.saf.messages.get._1_3;
            

            /**
            *  VaccinationType bean class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class VaccinationType
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = VaccinationType
                Namespace URI = http://saf.stchome.com/messages/get/1.3
                Namespace Prefix = ns3
                */
            

                        /**
                        * field for VaccCode
                        */

                        
                                    protected com.stchome.saf.common.VaccCodeStringType localVaccCode ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.VaccCodeStringType
                           */
                           public  com.stchome.saf.common.VaccCodeStringType getVaccCode(){
                               return localVaccCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param VaccCode
                               */
                               public void setVaccCode(com.stchome.saf.common.VaccCodeStringType param){
                            
                                            this.localVaccCode=param;
                                    

                               }
                            

                        /**
                        * field for VaccDate
                        */

                        
                                    protected com.stchome.saf.common.DateYYYYMMDDType localVaccDate ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.DateYYYYMMDDType
                           */
                           public  com.stchome.saf.common.DateYYYYMMDDType getVaccDate(){
                               return localVaccDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param VaccDate
                               */
                               public void setVaccDate(com.stchome.saf.common.DateYYYYMMDDType param){
                            
                                            this.localVaccDate=param;
                                    

                               }
                            

                        /**
                        * field for DoseSize
                        */

                        
                                    protected java.math.BigDecimal localDoseSize =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDecimal("1.0");
                                

                           /**
                           * Auto generated getter method
                           * @return java.math.BigDecimal
                           */
                           public  java.math.BigDecimal getDoseSize(){
                               return localDoseSize;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DoseSize
                               */
                               public void setDoseSize(java.math.BigDecimal param){
                            
                                            this.localDoseSize=param;
                                    

                               }
                            

                        /**
                        * field for Message
                        * This was an Array!
                        */

                        
                                    protected com.stchome.saf.common.VaccMessageType[] localMessage ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localMessageTracker = false ;

                           public boolean isMessageSpecified(){
                               return localMessageTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.VaccMessageType[]
                           */
                           public  com.stchome.saf.common.VaccMessageType[] getMessage(){
                               return localMessage;
                           }

                           
                        


                               
                              /**
                               * validate the array for Message
                               */
                              protected void validateMessage(com.stchome.saf.common.VaccMessageType[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Message
                              */
                              public void setMessage(com.stchome.saf.common.VaccMessageType[] param){
                              
                                   validateMessage(param);

                               localMessageTracker = param != null;
                                      
                                      this.localMessage=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.stchome.saf.common.VaccMessageType
                             */
                             public void addMessage(com.stchome.saf.common.VaccMessageType param){
                                   if (localMessage == null){
                                   localMessage = new com.stchome.saf.common.VaccMessageType[]{};
                                   }

                            
                                 //update the setting tracker
                                localMessageTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localMessage);
                               list.add(param);
                               this.localMessage =
                             (com.stchome.saf.common.VaccMessageType[])list.toArray(
                            new com.stchome.saf.common.VaccMessageType[list.size()]);

                             }
                             

                        /**
                        * field for CompromisedCode
                        */

                        
                                    protected java.lang.String localCompromisedCode ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCompromisedCodeTracker = false ;

                           public boolean isCompromisedCodeSpecified(){
                               return localCompromisedCodeTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getCompromisedCode(){
                               return localCompromisedCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CompromisedCode
                               */
                               public void setCompromisedCode(java.lang.String param){
                            localCompromisedCodeTracker = param != null;
                                   
                                            this.localCompromisedCode=param;
                                    

                               }
                            

                        /**
                        * field for Booster
                        */

                        
                                    protected boolean localBooster ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localBoosterTracker = false ;

                           public boolean isBoosterSpecified(){
                               return localBoosterTracker;
                           }

                           

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
                            
                                       // setting primitive attribute tracker to true
                                       localBoosterTracker =
                                       true;
                                   
                                            this.localBooster=param;
                                    

                               }
                            

                        /**
                        * field for CodeType
                        * This was an Attribute!
                        */

                        
                                    protected com.stchome.saf.common.VaccineCodeType localCodeType ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.VaccineCodeType
                           */
                           public  com.stchome.saf.common.VaccineCodeType getCodeType(){
                               return localCodeType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CodeType
                               */
                               public void setCodeType(com.stchome.saf.common.VaccineCodeType param){
                            
                                            this.localCodeType=param;
                                    

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
                           namespacePrefix+":VaccinationType",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "VaccinationType",
                           xmlWriter);
                   }

               
                   }
               
                                    
                                    if (localCodeType != null){
                                        writeAttribute("",
                                           "codeType",
                                           localCodeType.toString(), xmlWriter);
                                    }
                                    
                                      else {
                                          throw new org.apache.axis2.databinding.ADBException("required attribute localCodeType is null");
                                      }
                                    
                                            if (localVaccCode==null){
                                                 throw new org.apache.axis2.databinding.ADBException("vaccCode cannot be null!!");
                                            }
                                           localVaccCode.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","vaccCode"),
                                               xmlWriter);
                                        
                                            if (localVaccDate==null){
                                                 throw new org.apache.axis2.databinding.ADBException("vaccDate cannot be null!!");
                                            }
                                           localVaccDate.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","vaccDate"),
                                               xmlWriter);
                                        
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "doseSize", xmlWriter);
                             

                                          if (localDoseSize==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("doseSize cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDoseSize));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localMessageTracker){
                                       if (localMessage!=null){
                                            for (int i = 0;i < localMessage.length;i++){
                                                if (localMessage[i] != null){
                                                 localMessage[i].serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","message"),
                                                           xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("message cannot be null!!");
                                        
                                    }
                                 } if (localCompromisedCodeTracker){
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "compromisedCode", xmlWriter);
                             

                                          if (localCompromisedCode==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("compromisedCode cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localCompromisedCode);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localBoosterTracker){
                                    namespace = "http://saf.stchome.com/messages/get/1.3";
                                    writeStartElement(null, namespace, "booster", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("booster cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBooster));
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
                                                                      "vaccCode"));
                            
                            
                                    if (localVaccCode==null){
                                         throw new org.apache.axis2.databinding.ADBException("vaccCode cannot be null!!");
                                    }
                                    elementList.add(localVaccCode);
                                
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "vaccDate"));
                            
                            
                                    if (localVaccDate==null){
                                         throw new org.apache.axis2.databinding.ADBException("vaccDate cannot be null!!");
                                    }
                                    elementList.add(localVaccDate);
                                
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "doseSize"));
                                 
                                        if (localDoseSize != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDoseSize));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("doseSize cannot be null!!");
                                        }
                                     if (localMessageTracker){
                             if (localMessage!=null) {
                                 for (int i = 0;i < localMessage.length;i++){

                                    if (localMessage[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                          "message"));
                                         elementList.add(localMessage[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("message cannot be null!!");
                                    
                             }

                        } if (localCompromisedCodeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "compromisedCode"));
                                 
                                        if (localCompromisedCode != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCompromisedCode));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("compromisedCode cannot be null!!");
                                        }
                                    } if (localBoosterTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "booster"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBooster));
                            }
                            attribList.add(
                            new javax.xml.namespace.QName("","codeType"));
                            
                                      attribList.add(localCodeType.toString());
                                

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
        public static VaccinationType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            VaccinationType object =
                new VaccinationType();

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
                    
                            if (!"VaccinationType".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (VaccinationType)com.stchome.saf.messages.get._1_2.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    // handle attribute "codeType"
                    java.lang.String tempAttribCodeType =
                        
                                reader.getAttributeValue(null,"codeType");
                            
                   if (tempAttribCodeType!=null){
                         java.lang.String content = tempAttribCodeType;
                        
                                                  object.setCodeType(
                                                        com.stchome.saf.common.VaccineCodeType.Factory.fromString(reader,tempAttribCodeType));
                                            
                    } else {
                       
                               throw new org.apache.axis2.databinding.ADBException("Required attribute codeType is missing");
                           
                    }
                    handledAttributes.add("codeType");
                    
                    
                    reader.next();
                
                        java.util.ArrayList list4 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","vaccCode").equals(reader.getName())){
                                
                                                object.setVaccCode(com.stchome.saf.common.VaccCodeStringType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","vaccDate").equals(reader.getName())){
                                
                                                object.setVaccDate(com.stchome.saf.common.DateYYYYMMDDType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","doseSize").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"doseSize" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDoseSize(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDecimal(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","message").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list4.add(com.stchome.saf.common.VaccMessageType.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone4 = false;
                                                        while(!loopDone4){
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
                                                                loopDone4 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","message").equals(reader.getName())){
                                                                    list4.add(com.stchome.saf.common.VaccMessageType.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone4 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setMessage((com.stchome.saf.common.VaccMessageType[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.stchome.saf.common.VaccMessageType.class,
                                                                list4));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","compromisedCode").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"compromisedCode" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCompromisedCode(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
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
           
    