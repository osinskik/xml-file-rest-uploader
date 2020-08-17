package com.osinskik.demo.xmlfilerestuploader.web.sorting;

import java.util.function.BiFunction;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Constucts Sort object for Spring Data.
 */
@Component
public class SortingResolver implements BiFunction<EpaperRequestSortingType, Boolean, Sort> {

  /**
   * Constucts Sort object for Spring Data.
   * @param epaperRequestSortingType - field to sort on
   * @param isAscending - informs about sorting type (asc/desc)
   * @return Spring Data Sort object which represents searched option.
   */
  @Override
  public Sort apply(final EpaperRequestSortingType epaperRequestSortingType, final Boolean isAscending) {
    final Sort.Direction direction = isAscending? Sort.Direction.ASC: Sort.Direction.DESC;
    return Sort.by(direction, epaperRequestSortingType.getPropertyName());
  }
}
