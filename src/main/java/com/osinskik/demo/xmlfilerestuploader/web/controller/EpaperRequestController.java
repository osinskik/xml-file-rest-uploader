package com.osinskik.demo.xmlfilerestuploader.web.controller;

import java.time.Instant;

import javax.validation.constraints.Min;
import javax.xml.bind.JAXBException;

import com.osinskik.demo.xmlfilerestuploader.dao.EpaperRequestRepository;
import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import com.osinskik.demo.xmlfilerestuploader.web.dto.EpaperPageableResponse;
import com.osinskik.demo.xmlfilerestuploader.web.parser.EpaperRequestXmlFileParser;
import com.osinskik.demo.xmlfilerestuploader.web.sorting.EpaperRequestSortingType;
import com.osinskik.demo.xmlfilerestuploader.web.sorting.SortingResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
public class EpaperRequestController {

  private final EpaperRequestRepository epaperRequestRepository;
  private final EpaperRequestXmlFileParser epaperRequestFileXmlParser;
  private final SortingResolver sortingResolver;
  private final PageToEpaperPageableResponseMapper pageToEpaperPageableResponseMapper;
  private final Logger LOGGER = LoggerFactory.getLogger(EpaperRequestController.class);

  @Autowired
  public EpaperRequestController(
      final EpaperRequestRepository epaperRequestRepository,
      final EpaperRequestXmlFileParser epaperRequestFileXmlParser,
      final SortingResolver sortingResolver,
      final PageToEpaperPageableResponseMapper pageToEpaperPageableResponseMapper
  ) {
    this.epaperRequestRepository = epaperRequestRepository;
    this.epaperRequestFileXmlParser = epaperRequestFileXmlParser;
    this.sortingResolver = sortingResolver;
    this.pageToEpaperPageableResponseMapper = pageToEpaperPageableResponseMapper;
  }

  @PostMapping("/epaperrequest/upload")
  public String serveEPaperRequestFile(@RequestParam("file") final MultipartFile file) throws JAXBException {
    final EpaperRequest epaperRequest = epaperRequestFileXmlParser.parse(file);
    epaperRequest.setFileName(file.getOriginalFilename());
    epaperRequest.setUploadTime(Instant.now());
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("File served: \n" + epaperRequest);
    }
    epaperRequestRepository.save(epaperRequest);
    return epaperRequest.getFileName();
  }

  @GetMapping("/epaperrequest")
  public EpaperPageableResponse getEpaperRequests(
      @RequestParam(value = "pageNumber", defaultValue = "1", required = false) @Min(1) final Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) @Min(1) final Integer pageSize,
      @RequestParam(value = "orderBy", defaultValue = "upload_time", required = false) final EpaperRequestSortingType sortingType,
      @RequestParam(value = "orderingAsc", defaultValue = "false", required = false) final Boolean isOrderingAscending
  ) {
    final Sort sort = sortingResolver.apply(sortingType, isOrderingAscending);
    final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

    return pageToEpaperPageableResponseMapper.apply(epaperRequestRepository.findAll(pageable));
  }

}
