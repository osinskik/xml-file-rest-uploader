package com.osinskik.demo.xmlfilerestuploader.web.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class EpaperRequestXmlFileParserTest {
  
  @Mock 
  private Schema epaperRequestSchema;
  
  @Mock
  private JAXBContext epaperRequestContext;
  
  @Mock
  private Unmarshaller unmarshaller;

  @Mock
  private EpaperRequest epaperRequest;

  @Mock
  private MultipartFile multipartFile;
  
  @Mock
  private InputStream inputStream;

  @InjectMocks
  private EpaperRequestXmlFileParser parser;
  
  @BeforeEach
  public void setup() throws JAXBException, IOException {
    when(epaperRequestContext.createUnmarshaller()).thenReturn(unmarshaller);
    doNothing().when(unmarshaller).setSchema(epaperRequestSchema);
    when(multipartFile.getInputStream()).thenReturn(inputStream);
  }
  
  @AfterEach
  public void turnDown() throws JAXBException, IOException {
    verify(epaperRequestContext).createUnmarshaller();
    verify(unmarshaller).setSchema(epaperRequestSchema);
    verify(multipartFile).getInputStream();
    verify(unmarshaller).unmarshal(inputStream);
    verifyNoMoreInteractions(epaperRequestContext, multipartFile, unmarshaller);
  }
  
  @Test
  public void shouldInvokeUnmarshalling() throws JAXBException, IOException {
    when(unmarshaller.unmarshal(inputStream)).thenReturn(epaperRequest);
    
    assertThat(parser.parse(multipartFile)).isEqualTo(epaperRequest);
  }


  @Test
  public void shouldThrowIllegalArgumentExceptionWhenUnmarshallingFailed() throws JAXBException, IOException {
    when(unmarshaller.unmarshal(inputStream)).thenThrow(JAXBException.class);

    assertThatThrownBy(() -> parser.parse(multipartFile)).isInstanceOf(IllegalArgumentException.class);
  }
}
