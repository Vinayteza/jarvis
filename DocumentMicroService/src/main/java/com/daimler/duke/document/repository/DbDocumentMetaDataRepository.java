package com.daimler.duke.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daimler.duke.document.db.entity.DbDocumentMetaData;

/**
 * 
 * @author RMAHAKU
 *
 */
public interface DbDocumentMetaDataRepository extends MongoRepository<DbDocumentMetaData,String> {
    /**
     * 
     * @param parentId
     * @return
     */
    // List<DbDocumentMetaData> findByParentId(String parentId);

}
