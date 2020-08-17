package com.osinskik.demo.xmlfilerestuploader.web.sorting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class SortingResolverTest {

  @Test
  public void shouldResolveSorting() {
    final Sort expectedSort = Sort.by(Sort.Direction.ASC, "deviceInfo.screenInfo.dpi");
    assertThat(new SortingResolver().apply(EpaperRequestSortingType.DPI, true)).isEqualTo(expectedSort);
  }
}
