package com.osinskik.demo.xmlfilerestuploader.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import com.osinskik.demo.xmlfilerestuploader.web.dto.EpaperPageableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PageToEpaperPageableResponseMapperTest {

  private static final int PAGE_COUNT = 333;
  private static final int PAGE_NUMBER = 123;
  private static final int PAGE_SIZE = 321;
  
  @Mock
  private Page<EpaperRequest> page;
  
  @Mock
  private Pageable pageable;
  
  @Mock
  private List<EpaperRequest> epaperRequests;

  @Test
  public void shoulMapPageToEpaperPageableResponse() {
    when(page.getPageable()).thenReturn(pageable);
    when(page.getNumberOfElements()).thenReturn(PAGE_COUNT);
    when(pageable.getPageNumber()).thenReturn(PAGE_NUMBER);
    when(pageable.getPageSize()).thenReturn(PAGE_SIZE);
    when(page.getContent()).thenReturn(epaperRequests);

    final EpaperPageableResponse actualResponse = new PageToEpaperPageableResponseMapper().apply(page);
    assertThat(actualResponse.getPaginationData().getCount()).isEqualTo(PAGE_COUNT);
    assertThat(actualResponse.getPaginationData().getPageNumber()).isEqualTo(PAGE_NUMBER + 1);
    assertThat(actualResponse.getPaginationData().getPageSize()).isEqualTo(PAGE_SIZE);
    assertThat(actualResponse.getEpaperRequests()).isEqualTo(epaperRequests);
    verifyNoMoreInteractions(page, pageable);
  }

}
