package com.osinskik.demo.xmlfilerestuploader.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationData {

  private final Integer pageSize;
  private final Integer pageNumber;
  private final Integer count;

}
