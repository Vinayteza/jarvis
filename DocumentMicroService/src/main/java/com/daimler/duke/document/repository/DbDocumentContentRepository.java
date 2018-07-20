package com.daimler.duke.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daimler.duke.document.db.entity.DbDocumentContent;

/**
 * 
 * @author RMAHAKU
 *
 */
public interface DbDocumentContentRepository extends MongoRepository<DbDocumentContent, String> {
}
