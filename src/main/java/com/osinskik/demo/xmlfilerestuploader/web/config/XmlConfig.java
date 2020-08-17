package com.osinskik.demo.xmlfilerestuploader.web.config;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

@Configuration
public class XmlConfig {

  @Bean
  JAXBContext epaperRequestContext() throws JAXBException {
    return JAXBContext.newInstance(EpaperRequest.class);
  }

  @Bean
  SchemaFactory schemaFactory() {
    return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
  }

  @Bean
  Schema epaperRequestSchema(
      @Value("classpath:xsd/epaperRequest.xsd") Resource xsdResourceFile,
      SchemaFactory schemaFactory
  ) throws IOException, SAXException {
    return schemaFactory.newSchema(xsdResourceFile.getFile());
  }

}
