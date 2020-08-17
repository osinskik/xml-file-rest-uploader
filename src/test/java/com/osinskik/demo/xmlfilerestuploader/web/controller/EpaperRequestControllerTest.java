package com.osinskik.demo.xmlfilerestuploader.web.controller;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.osinskik.demo.xmlfilerestuploader.dao.EpaperRequestRepository;
import com.osinskik.demo.xmlfilerestuploader.dto.AppInfo;
import com.osinskik.demo.xmlfilerestuploader.dto.DeviceInfo;
import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import com.osinskik.demo.xmlfilerestuploader.dto.ScreenInfo;
import com.osinskik.demo.xmlfilerestuploader.web.dto.EpaperPageableResponse;
import com.osinskik.demo.xmlfilerestuploader.web.dto.ErrorMessage;
import com.osinskik.demo.xmlfilerestuploader.web.dto.PaginationData;
import com.osinskik.demo.xmlfilerestuploader.web.parser.EpaperRequestXmlFileParser;
import com.osinskik.demo.xmlfilerestuploader.web.sorting.EpaperRequestSortingType;
import com.osinskik.demo.xmlfilerestuploader.web.sorting.SortingResolver;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EpaperRequestControllerTest {

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final String REQUEST_PARAM_FILE_NAME = "file";

  @MockBean
  private EpaperRequestRepository epaperRequestRepository;
  
  @MockBean
  private EpaperRequestXmlFileParser epaperRequestFileXmlParser;
  
  @MockBean
  private SortingResolver sortingResolver;
  
  @MockBean
  private PageToEpaperPageableResponseMapper pageToEpaperPageableResponseMapper;

  @Mock
  private Sort sort;

  @Mock
  private Page<EpaperRequest> page;

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Test
  void shouldReturnAllEpaperRequestsByDefaultParams() {
    when(sortingResolver.apply(EpaperRequestSortingType.UPLOAD_TIME, false)).thenReturn(sort);
    final ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
    when(epaperRequestRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(page);
    when(pageToEpaperPageableResponseMapper.apply(page)).thenReturn(provideTestEpaperPageableResponse());

    final EpaperPageableResponse response = restTemplate.getForObject(
        "http://localhost:" + port + "/epaperrequest",
        EpaperPageableResponse.class
    );

    assertThat(response).isEqualTo(provideTestEpaperPageableResponse());
    final Pageable capturedPageable = pageableArgumentCaptor.getValue();
    assertThat(capturedPageable.getSort()).isEqualTo(sort);
    assertThat(capturedPageable.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
    assertThat(capturedPageable.getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
  }

  @Test
  void shouldReturnAllEpaperRequestsByCustomArguments() {
    when(sortingResolver.apply(EpaperRequestSortingType.WIDTH, true)).thenReturn(sort);
    final ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
    when(epaperRequestRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(page);
    when(pageToEpaperPageableResponseMapper.apply(page)).thenReturn(provideTestEpaperPageableResponse());
    
    final EpaperPageableResponse response = restTemplate.getForObject(
        "http://localhost:" + port + "/epaperrequest"
            + "?pageNumber=10"
            + "&pageSize=20"
            + "&orderBy=width"
            + "&orderingAsc=true",
        EpaperPageableResponse.class
    );

    assertThat(response).isEqualTo(provideTestEpaperPageableResponse());
    final Pageable capturedPageable = pageableArgumentCaptor.getValue();
    assertThat(capturedPageable.getSort()).isEqualTo(sort);
    assertThat(capturedPageable.getPageNumber()).isEqualTo(9);
    assertThat(capturedPageable.getPageSize()).isEqualTo(20);
  }

  @Test
  void shouldResponseWithBadRequestForInvalidArgument() {
    final ErrorMessage response = restTemplate.getForObject(
        "http://localhost:" + port + "/epaperrequest"
            + "?pageNumber=0",
        ErrorMessage.class
    );
    
    assertThat(response.getMessage()).isNotNull();
  }
  
  @SneakyThrows
  @Test
  void shouldUploadAFile() {
    final String fileName = "fileName";
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    final MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
    final ContentDisposition contentDisposition = ContentDisposition
        .builder("form-data")
        .name(REQUEST_PARAM_FILE_NAME)
        .filename(fileName)
        .build();
    fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
    HttpEntity<byte[]> fileEntity = new HttpEntity<>("1234String".getBytes(), fileMap);

    final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", fileEntity);

    final HttpEntity<MultiValueMap<String, Object>> requestEntity =
        new HttpEntity<>(body, headers);

    final EpaperRequest epaperRequest = new EpaperRequest();

    when(epaperRequestFileXmlParser.parse(any())).thenReturn(epaperRequest);

    ResponseEntity<String> response = restTemplate.exchange(
        "http://localhost:" + port + "/epaperrequest/upload",
        HttpMethod.POST,
        requestEntity,
        String.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(fileName);
    assertThat(epaperRequest.getFileName()).isEqualTo(fileName);
    assertThat(epaperRequest.getUploadTime()).isBetween(Instant.now().minus(1, ChronoUnit.SECONDS), Instant.now());
    Mockito.verify(epaperRequestRepository).save(epaperRequest);
  }

  private EpaperPageableResponse provideTestEpaperPageableResponse() {
    return new EpaperPageableResponse(
        singletonList(provideTestEpaperRequest()),
        new PaginationData(1, 2, 3)
    );
  }

  private EpaperRequest provideTestEpaperRequest() {

    return new EpaperRequest(
        Instant.ofEpochMilli(1),
        "someFileName",
        new DeviceInfo(
            new ScreenInfo(
                100,
                200,
                300
            ),
            new AppInfo(
                "newspaper"
            )
        )
    );
  }
}
