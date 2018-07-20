package com.daimler.duke.document.service;

import java.util.List;

import com.daimler.duke.document.dto.ApplicationAutherizationGroupV1;
import com.daimler.duke.document.dto.ApplicationIdentifierV1;

import io.jsonwebtoken.Claims;

public interface IDocumentAppIdentifierService {

    /**
     * get the Document ApplicationIdentifier
     * 
     * @param documentId
     * @return
     */
    ApplicationIdentifierV1 getDocumentApplicationIdentifier(String documentId);

    /**
     * create DocumentApplicationIdentifier
     * 
     * @param document
     * @return
     */
    String createDocumentApplicationIdentifier(ApplicationIdentifierV1 document);

    /**
     * delete DocumentApplicationIdentifier
     * 
     * @param documentId
     */
    void deleteDocumentApplicationIdentifier(String documentId);

    /**
     * update DocumentApplicationIdentifier
     * 
     * @param document
     * @return
     */
    String updateDocumentApplicationIdentifier(ApplicationIdentifierV1 document);

    /**
     * verify the ApplicationName
     * 
     * @param claims
     * @param applicationName
     */
    void verifyApplicationName(final Claims claims, final String applicationName);

    /**
     * decrypt ApplicationName.
     * 
     * @param applicationName
     * @return
     */
    String decryptApplicationName(final String applicationName);

    /**
     * Get SecreteKey By given ApplicationName.
     * 
     * @param applicationName
     * @return
     */
    String getSecreteKeyByApplicationName(String applicationName);

    /**
     * return get list of Authorized Groups for given Application name.
     * 
     * @param applicationName
     * @return
     */
    List<ApplicationAutherizationGroupV1> getListOfAuthorizedGroups(String applicationName);

    /**
     * returns ApplicationIdentifierV1 for given application name.
     * 
     * @param applicationName
     * @return
     */
    ApplicationIdentifierV1 findByApplicationName(String applicationName);

}
