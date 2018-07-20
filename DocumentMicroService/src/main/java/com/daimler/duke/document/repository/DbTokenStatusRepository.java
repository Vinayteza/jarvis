package com.daimler.duke.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daimler.duke.document.db.entity.DbTokenStatus;

public interface DbTokenStatusRepository extends MongoRepository<DbTokenStatus,String> {

    DbTokenStatus findByTokenValue(String tokenValue);

    DbTokenStatus findByDocumentId(String documentId);
}
