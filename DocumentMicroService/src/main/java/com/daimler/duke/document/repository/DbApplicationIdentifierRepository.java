package com.daimler.duke.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daimler.duke.document.db.entity.DbApplicationIdentifier;

/**
 * DbApplicationIdentifierRepository class provides the basic CRUD operation for DbApplicationIdentifier collection.
 * 
 * @author NAYASAR
 *
 */
public interface DbApplicationIdentifierRepository extends MongoRepository<DbApplicationIdentifier,String> {

    /**
     * This findByApplicationName loads DbApplicationIdentifier mongodb object for given name. We should not modify this
     * method signature since it matches with the applicationName field of the DbApplicationIdentifier class.
     * 
     * @param applicationName
     * @return
     */
    DbApplicationIdentifier findByApplicationName(String applicationName);

    /**
     * This findBySecretCode loads DbApplicationIdentifier mongodb object for given name. We should not modify this
     * method signature since it matches with the secretCode field of the DbApplicationIdentifier class.
     * 
     * @param secretCode
     * @return
     */
    DbApplicationIdentifier findBySecretCode(String secretCode);

}
