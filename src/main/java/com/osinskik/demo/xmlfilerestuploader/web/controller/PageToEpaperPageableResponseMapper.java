package com.osinskik.demo.xmlfilerestuploader.web.controller;

import java.util.function.Function;

import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import com.osinskik.demo.xmlfilerestuploader.web.dto.EpaperPageableResponse;
import com.osinskik.demo.xmlfilerestuploader.web.dto.PaginationData;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Maps Spring Data Page results to EpaperPageableResponse
 */
@Component
public class PageToEpaperPageableResponseMapper implements Function<Page<EpaperRequest>, EpaperPageableResponse> {

  /**
   * Maps Spring Data Page results to EpaperPageableResponse
   * @param page Spring Data paginated response
   * @return parsed response
   */
  @Override
  public EpaperPageableResponse apply(final Page<EpaperRequest> page) {
    final PaginationData paginationData = new PaginationData(
        page.getPageable().getPageSize(),
        page.getPageable().getPageNumber() + 1,
        page.getNumberOfElements()
    );
    return new EpaperPageableResponse(
        page.getContent(),
        paginationData
    );
  }

}
