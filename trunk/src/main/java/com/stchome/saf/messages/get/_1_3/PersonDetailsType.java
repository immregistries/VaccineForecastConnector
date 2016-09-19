
/**
 * PersonDetailsType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

            
                package com.stchome.saf.messages.get._1_3;
            

            /**
            *  PersonDetailsType bean class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class PersonDetailsType
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = PersonDetailsType
                Namespace URI = http://saf.stchome.com/messages/get/1.3
                Namespace Prefix = ns3
                */
            

                        /**
                        * field for ScheduleType
                        */

                        
                                    protected com.stchome.saf.common.ScheduleType localScheduleType ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localScheduleTypeTracker = false ;

                           public boolean isScheduleTypeSpecified(){
                               return localScheduleTypeTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.ScheduleType
                           */
                           public  com.stchome.saf.common.ScheduleType getScheduleType(){
                               return localScheduleType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ScheduleType
                               */
                               public void setScheduleType(com.stchome.saf.common.ScheduleType param){
                            localScheduleTypeTracker = param != null;
                                   
                                            this.localScheduleType=param;
                                    

                               }
                            

                        /**
                        * field for DateOfBirth
                        */

                        
                                    protected com.stchome.saf.common.DateYYYYMMDDType localDateOfBirth ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.DateYYYYMMDDType
                           */
                           public  com.stchome.saf.common.DateYYYYMMDDType getDateOfBirth(){
                               return localDateOfBirth;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DateOfBirth
                               */
                               public void setDateOfBirth(com.stchome.saf.common.DateYYYYMMDDType param){
                            
                                            this.localDateOfBirth=param;
                                    

                               }
                            

                        /**
                        * field for Gender
                        */

                        
                                    protected com.stchome.saf.common.GenderType localGender ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localGenderTracker = false ;

                           public boolean isGenderSpecified(){
                               return localGenderTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.GenderType
                           */
                           public  com.stchome.saf.common.GenderType getGender(){
                               return localGender;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Gender
                               */
                               public void setGender(com.stchome.saf.common.GenderType param){
                            localGenderTracker = param != null;
                                   
                                            this.localGender=param;
                                    

                               }
                            

                        /**
                        * field for Vaccination
                        * This was an Array!
                        */

                        
                                    protected com.stchome.saf.messages.get._1_3.VaccinationType[] localVaccination ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localVaccinationTracker = false ;

                           public boolean isVaccinationSpecified(){
                               return localVaccinationTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.messages.get._1_3.VaccinationType[]
                           */
                           public  com.stchome.saf.messages.get._1_3.VaccinationType[] getVaccination(){
                               return localVaccination;
                           }

                           
                        


                               
                              /**
                               * validate the array for Vaccination
                               */
                              protected void validateVaccination(com.stchome.saf.messages.get._1_3.VaccinationType[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Vaccination
                              */
                              public void setVaccination(com.stchome.saf.messages.get._1_3.VaccinationType[] param){
                              
                                   validateVaccination(param);

                               localVaccinationTracker = param != null;
                                      
                                      this.localVaccination=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.stchome.saf.messages.get._1_3.VaccinationType
                             */
                             public void addVaccination(com.stchome.saf.messages.get._1_3.VaccinationType param){
                                   if (localVaccination == null){
                                   localVaccination = new com.stchome.saf.messages.get._1_3.VaccinationType[]{};
                                   }

                            
                                 //update the setting tracker
                                localVaccinationTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localVaccination);
                               list.add(param);
                               this.localVaccination =
                             (com.stchome.saf.messages.get._1_3.VaccinationType[])list.toArray(
                            new com.stchome.saf.messages.get._1_3.VaccinationType[list.size()]);

                             }
                             

                        /**
                        * field for Contraindication
                        * This was an Array!
                        */

                        
                                    protected com.stchome.saf.messages.get._1_3.ContraindicationType[] localContraindication ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localContraindicationTracker = false ;

                           public boolean isContraindicationSpecified(){
                               return localContraindicationTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.messages.get._1_3.ContraindicationType[]
                           */
                           public  com.stchome.saf.messages.get._1_3.ContraindicationType[] getContraindication(){
                               return localContraindication;
                           }

                           
                        


                               
                              /**
                               * validate the array for Contraindication
                               */
                              protected void validateContraindication(com.stchome.saf.messages.get._1_3.ContraindicationType[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Contraindication
                              */
                              public void setContraindication(com.stchome.saf.messages.get._1_3.ContraindicationType[] param){
                              
                                   validateContraindication(param);

                               localContraindicationTracker = param != null;
                                      
                                      this.localContraindication=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.stchome.saf.messages.get._1_3.ContraindicationType
                             */
                             public void addContraindication(com.stchome.saf.messages.get._1_3.ContraindicationType param){
                                   if (localContraindication == null){
                                   localContraindication = new com.stchome.saf.messages.get._1_3.ContraindicationType[]{};
                                   }

                            
                                 //update the setting tracker
                                localContraindicationTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localContraindication);
                               list.add(param);
                               this.localContraindication =
                             (com.stchome.saf.messages.get._1_3.ContraindicationType[])list.toArray(
                            new com.stchome.saf.messages.get._1_3.ContraindicationType[list.size()]);

                             }
                             

                        /**
                        * field for PersonId
                        * This was an Attribute!
                        */

                        
                                    protected com.stchome.saf.common.Len20StringType localPersonId ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.Len20StringType
                           */
                           public  com.stchome.saf.common.Len20StringType getPersonId(){
                               return localPersonId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PersonId
                               */
                               public void setPersonId(com.stchome.saf.common.Len20StringType param){
                            
                                            this.localPersonId=param;
                                    

                               }
                            

                        /**
                        * field for FullForecast
                        * This was an Attribute!
                        */

                        
                                    protected java.lang.String localFullForecast ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFullForecast(){
                               return localFullForecast;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FullForecast
                               */
                               public void setFullForecast(java.lang.String param){
                            
                                            this.localFullForecast=param;
                                    

                               }
                            

                        /**
                        * field for EvaluationDate
                        * This was an Attribute!
                        */

                        
                                    protected com.stchome.saf.common.DateYYYYMMDDType localEvaluationDate ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.stchome.saf.common.DateYYYYMMDDType
                           */
                           public  com.stchome.saf.common.DateYYYYMMDDType getEvaluationDate(){
                               return localEvaluationDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EvaluationDate
                               */
                               public void setEvaluationDate(com.stchome.saf.common.DateYYYYMMDDType param){
                            
                                            this.localEvaluationDate=param;
                                    

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
                           namespacePrefix+":PersonDetailsType",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "PersonDetailsType",
                           xmlWriter);
                   }

               
                   }
               
                                    
                                    if (localPersonId != null){
                                        writeAttribute("",
                                           "personId",
                                           localPersonId.toString(), xmlWriter);
                                    }
                                    
                                            if (localFullForecast != null){
                                        
                                                writeAttribute("",
                                                         "fullForecast",
                                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFullForecast), xmlWriter);

                                            
                                      }
                                    
                                    
                                    if (localEvaluationDate != null){
                                        writeAttribute("",
                                           "evaluationDate",
                                           localEvaluationDate.toString(), xmlWriter);
                                    }
                                     if (localScheduleTypeTracker){
                                            if (localScheduleType==null){
                                                 throw new org.apache.axis2.databinding.ADBException("scheduleType cannot be null!!");
                                            }
                                           localScheduleType.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","scheduleType"),
                                               xmlWriter);
                                        }
                                            if (localDateOfBirth==null){
                                                 throw new org.apache.axis2.databinding.ADBException("dateOfBirth cannot be null!!");
                                            }
                                           localDateOfBirth.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","dateOfBirth"),
                                               xmlWriter);
                                         if (localGenderTracker){
                                            if (localGender==null){
                                                 throw new org.apache.axis2.databinding.ADBException("gender cannot be null!!");
                                            }
                                           localGender.serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","gender"),
                                               xmlWriter);
                                        } if (localVaccinationTracker){
                                       if (localVaccination!=null){
                                            for (int i = 0;i < localVaccination.length;i++){
                                                if (localVaccination[i] != null){
                                                 localVaccination[i].serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","vaccination"),
                                                           xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("vaccination cannot be null!!");
                                        
                                    }
                                 } if (localContraindicationTracker){
                                       if (localContraindication!=null){
                                            for (int i = 0;i < localContraindication.length;i++){
                                                if (localContraindication[i] != null){
                                                 localContraindication[i].serialize(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","contraindication"),
                                                           xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("contraindication cannot be null!!");
                                        
                                    }
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

                 if (localScheduleTypeTracker){
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "scheduleType"));
                            
                            
                                    if (localScheduleType==null){
                                         throw new org.apache.axis2.databinding.ADBException("scheduleType cannot be null!!");
                                    }
                                    elementList.add(localScheduleType);
                                }
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "dateOfBirth"));
                            
                            
                                    if (localDateOfBirth==null){
                                         throw new org.apache.axis2.databinding.ADBException("dateOfBirth cannot be null!!");
                                    }
                                    elementList.add(localDateOfBirth);
                                 if (localGenderTracker){
                            elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                      "gender"));
                            
                            
                                    if (localGender==null){
                                         throw new org.apache.axis2.databinding.ADBException("gender cannot be null!!");
                                    }
                                    elementList.add(localGender);
                                } if (localVaccinationTracker){
                             if (localVaccination!=null) {
                                 for (int i = 0;i < localVaccination.length;i++){

                                    if (localVaccination[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                          "vaccination"));
                                         elementList.add(localVaccination[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("vaccination cannot be null!!");
                                    
                             }

                        } if (localContraindicationTracker){
                             if (localContraindication!=null) {
                                 for (int i = 0;i < localContraindication.length;i++){

                                    if (localContraindication[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3",
                                                                          "contraindication"));
                                         elementList.add(localContraindication[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("contraindication cannot be null!!");
                                    
                             }

                        }
                            attribList.add(
                            new javax.xml.namespace.QName("","personId"));
                            
                                      attribList.add(localPersonId.toString());
                                
                            attribList.add(
                            new javax.xml.namespace.QName("","fullForecast"));
                            
                                      attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFullForecast));
                                
                            attribList.add(
                            new javax.xml.namespace.QName("","evaluationDate"));
                            
                                      attribList.add(localEvaluationDate.toString());
                                

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
        public static PersonDetailsType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            PersonDetailsType object =
                new PersonDetailsType();

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
                    
                            if (!"PersonDetailsType".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (PersonDetailsType)com.stchome.saf.messages.get._1_2.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    // handle attribute "personId"
                    java.lang.String tempAttribPersonId =
                        
                                reader.getAttributeValue(null,"personId");
                            
                   if (tempAttribPersonId!=null){
                         java.lang.String content = tempAttribPersonId;
                        
                                                  object.setPersonId(
                                                        com.stchome.saf.common.Len20StringType.Factory.fromString(reader,tempAttribPersonId));
                                            
                    } else {
                       
                    }
                    handledAttributes.add("personId");
                    
                    // handle attribute "fullForecast"
                    java.lang.String tempAttribFullForecast =
                        
                                reader.getAttributeValue(null,"fullForecast");
                            
                   if (tempAttribFullForecast!=null){
                         java.lang.String content = tempAttribFullForecast;
                        
                                                 object.setFullForecast(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(tempAttribFullForecast));
                                            
                    } else {
                       
                    }
                    handledAttributes.add("fullForecast");
                    
                    // handle attribute "evaluationDate"
                    java.lang.String tempAttribEvaluationDate =
                        
                                reader.getAttributeValue(null,"evaluationDate");
                            
                   if (tempAttribEvaluationDate!=null){
                         java.lang.String content = tempAttribEvaluationDate;
                        
                                                  object.setEvaluationDate(
                                                        com.stchome.saf.common.DateYYYYMMDDType.Factory.fromString(reader,tempAttribEvaluationDate));
                                            
                    } else {
                       
                    }
                    handledAttributes.add("evaluationDate");
                    
                    
                    reader.next();
                
                        java.util.ArrayList list4 = new java.util.ArrayList();
                    
                        java.util.ArrayList list5 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","scheduleType").equals(reader.getName())){
                                
                                                object.setScheduleType(com.stchome.saf.common.ScheduleType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","dateOfBirth").equals(reader.getName())){
                                
                                                object.setDateOfBirth(com.stchome.saf.common.DateYYYYMMDDType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","gender").equals(reader.getName())){
                                
                                                object.setGender(com.stchome.saf.common.GenderType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","vaccination").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list4.add(com.stchome.saf.messages.get._1_3.VaccinationType.Factory.parse(reader));
                                                                
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
                                                                if (new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","vaccination").equals(reader.getName())){
                                                                    list4.add(com.stchome.saf.messages.get._1_3.VaccinationType.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone4 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setVaccination((com.stchome.saf.messages.get._1_3.VaccinationType[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.stchome.saf.messages.get._1_3.VaccinationType.class,
                                                                list4));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","contraindication").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list5.add(com.stchome.saf.messages.get._1_3.ContraindicationType.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone5 = false;
                                                        while(!loopDone5){
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
                                                                loopDone5 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://saf.stchome.com/messages/get/1.3","contraindication").equals(reader.getName())){
                                                                    list5.add(com.stchome.saf.messages.get._1_3.ContraindicationType.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone5 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setContraindication((com.stchome.saf.messages.get._1_3.ContraindicationType[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.stchome.saf.messages.get._1_3.ContraindicationType.class,
                                                                list5));
                                                            
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
           
    