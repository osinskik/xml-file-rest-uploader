package com.osinskik.demo.xmlfilerestuploader.web.sorting;

import org.springframework.core.convert.converter.Converter;

/**
 * Implementation of Spring converter from String to EpaperRequestSortingType
 */
public class EpaperRequestSortingTypeConverter implements Converter<String, EpaperRequestSortingType> {
  
  @Override
  public EpaperRequestSortingType convert(String source) {
    return EpaperRequestSortingType.valueOf(source.toUpperCase());
  }

}
