package com.osinskik.demo.xmlfilerestuploader.dao;

import com.osinskik.demo.xmlfilerestuploader.dto.EpaperRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data Mongo Repository
 */
public interface EpaperRequestRepository extends MongoRepository<EpaperRequest, String> {

}
