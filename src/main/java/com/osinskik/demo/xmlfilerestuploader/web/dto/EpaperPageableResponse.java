package com.osinskik.demo.xmlfilerestuploader.web.dto;

import java.util.List;

import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EpaperPageableResponse {

  private final List<EpaperRequest> epaperRequests;
  private final PaginationData paginationData;

}
