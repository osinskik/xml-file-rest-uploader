package com.osinskik.demo.xmlfilerestuploader.web.parser;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Parser for xml files to EpaperRequestDTO
 */
@Component
public class EpaperRequestXmlFileParser {

  private final Schema epaperRequestSchema;
  private final JAXBContext epaperRequestContext;

  @Autowired
  public EpaperRequestXmlFileParser(
      final Schema epaperRequestSchema,
      final JAXBContext epaperRequestContext
  ) {
    this.epaperRequestSchema = epaperRequestSchema;
    this.epaperRequestContext = epaperRequestContext;
  }

  /**
   * Parses provided file to EpaperRequest DTO
   *
   * @param file file to parse
   * @return parsed file as dto
   * @throws JAXBException in case of incorrect setup of marshaller
   * @throws IllegalArgumentException in case of incorrectly unmarshalled file
   */
  public EpaperRequest parse(final MultipartFile file) throws JAXBException {
    Unmarshaller unmarshaller = epaperRequestContext.createUnmarshaller();
    unmarshaller.setSchema(epaperRequestSchema);
    try {
      return (EpaperRequest) unmarshaller.unmarshal(file.getInputStream());
    } catch (IOException | JAXBException e) {
      throw new IllegalArgumentException("Provided file cannot be unmarshalled: " + e.getMessage(), e);
    }
  }

}
