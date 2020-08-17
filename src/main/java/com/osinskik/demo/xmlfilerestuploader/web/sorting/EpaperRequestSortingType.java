package com.osinskik.demo.xmlfilerestuploader.web.sorting;

/**
 * Contains all possible fields to sort,
 * Where enum name is a requested field, and "propertyName" is a property used to sort on.
 */
public enum EpaperRequestSortingType {
  UPLOAD_TIME("uploadTime"),
  WIDTH("deviceInfo.screenInfo.width"),
  HEIGHT("deviceInfo.screenInfo.height"),
  DPI("deviceInfo.screenInfo.dpi"),
  NEWSPAPER_NAME("deviceInfo.appInfo.newsppaperName");

  private final String propertyName;

  EpaperRequestSortingType(final String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   * @return DTO property name
   */
  public String getPropertyName() {
    return propertyName;
  }
}
