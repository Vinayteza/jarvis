package com.daimler.duke.document.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbApplicationAutherizationGroup;
import com.daimler.duke.document.db.entity.DbApplicationIdentifier;
import com.daimler.duke.document.dto.ApplicationAutherizationGroupV1;
import com.daimler.duke.document.dto.ApplicationIdentifierV1;
import com.daimler.duke.document.exception.AuthorizationException;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.EncryptionException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.interceptor.RequestValidator;
import com.daimler.duke.document.repository.DbApplicationIdentifierRepository;
import com.daimler.duke.document.util.CommonUtil;
import com.daimler.duke.document.util.ConversionUtil;
import com.daimler.duke.document.util.DataEncrypter;
import com.daimler.duke.document.util.OperationType;

import io.jsonwebtoken.Claims;

/**
 * This DocumentAppIdentifierService class have all the CRUD operations.
 * 
 * @author NAYASAR
 *
 */
@RequestScoped
@Component
// @ExceptionI
public class DocumentAppIdentifierService implements IDocumentAppIdentifierService {

    private static final Logger               LOGGER = LoggerFactory.getLogger(DocumentAppIdentifierService.class);

    @Autowired
    private DbApplicationIdentifierRepository dbApplicationIdentifierRepository;

    /**
     * get the Document ApplicationIdentifier
     * 
     * @param documentId
     * @return
     */
    @Override
    public ApplicationIdentifierV1 getDocumentApplicationIdentifier(String applicationId) {
        DbApplicationIdentifier dbApplicationIdentifier = null;
        try {
            LOGGER.info("ApplicationId" + applicationId);
            dbApplicationIdentifier = dbApplicationIdentifierRepository.findOne(applicationId);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }
        if (dbApplicationIdentifier == null) {
            throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                        CommonErrorCodes.COMMON001.getDescDe());
        }
        ApplicationIdentifierV1 applicationIdentifier =
                ConversionUtil.convertFromDbApplicationIdentifier(dbApplicationIdentifier);
        return applicationIdentifier;
    }

    /**
     * create DocumentApplicationIdentifier
     * 
     * @param document
     * @return
     */
    @Override
    public String createDocumentApplicationIdentifier(ApplicationIdentifierV1 applicationIdentifier) {
        validateApplicationIdentifierRequest(applicationIdentifier, OperationType.CREATE);
        DbApplicationIdentifier dbApplicationIdentifierToSave =
                ConversionUtil.convertFromApplicationIdentifier(applicationIdentifier);

        try {
            DbApplicationIdentifier savedDbApplicationIdentifier =
                    dbApplicationIdentifierRepository.insert(dbApplicationIdentifierToSave);
            return savedDbApplicationIdentifier.getApplicationId();
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
    }

    /**
     * delete DocumentApplicationIdentifier
     * 
     * @param documentId
     */
    @Override
    public void deleteDocumentApplicationIdentifier(String applicationId) {
        try {
            LOGGER.info("ApplicationId" + applicationId);
            dbApplicationIdentifierRepository.delete(applicationId);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }

    }

    /**
     * validates the ApplicationIdentifier Request.
     * 
     * @param applicationIdentifier
     * @param operationType
     * @throws DocumentException
     */
    private void validateApplicationIdentifierRequest(ApplicationIdentifierV1 applicationIdentifier,
                                                      OperationType operationType)
            throws ValidationException {
        List<String> validateDocumentRequest =
                RequestValidator.validateApplicationIdentifierRequest(applicationIdentifier, operationType);
        String stringFromLists = CommonUtil.getStringFromLists(validateDocumentRequest, Constants.SEPERATOR);

        if (!CommonUtil.isStringNullOrEmpty(stringFromLists)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), stringFromLists);

        }
    }

    /**
     * update DocumentApplicationIdentifier
     * 
     * @param document
     * @return
     */
    @Override
    public String updateDocumentApplicationIdentifier(ApplicationIdentifierV1 applicationIdentifier) {
        validateApplicationIdentifierRequest(applicationIdentifier, OperationType.UPDATE);
        DbApplicationIdentifier dbApplicationIdentifierToSave =
                ConversionUtil.convertFromApplicationIdentifier(applicationIdentifier);

        try {
            DbApplicationIdentifier dbApplicationIdentifier =
                    dbApplicationIdentifierRepository.findOne(applicationIdentifier.getApplicationId());

            if (dbApplicationIdentifier != null) {
                dbApplicationIdentifierToSave.setApplicationId(dbApplicationIdentifier.getApplicationId());
                DbApplicationIdentifier savedDbApplicationIdentifier =
                        dbApplicationIdentifierRepository.save(dbApplicationIdentifierToSave);
                return savedDbApplicationIdentifier.getApplicationId();
            }
            else {
                throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                            CommonErrorCodes.COMMON001.getDescDe());
            }

        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }
    }

    /**
     * verify the ApplicationName against DbApplicationIdentifier collection from database. get the application name
     * after decryption.
     * 
     * @param claims
     * @param applicationName
     */
    @Override
    public void verifyApplicationName(final Claims claims, final String encodedAppName) {

        String authGroup = (String) claims.get("role");

        try {

            // Decrypt the applicationName with key.
            String applicationName = decryptApplicationName(encodedAppName);

            DbApplicationIdentifier dbApplicationIdentifier =
                    dbApplicationIdentifierRepository.findByApplicationName(applicationName);

            if (dbApplicationIdentifier != null
                    && !CollectionUtils.isEmpty(dbApplicationIdentifier.getApplicationAutherizationGrp())) {
                List<DbApplicationAutherizationGroup> authGrp =
                        dbApplicationIdentifier.getApplicationAutherizationGrp();

                boolean isAuthKeyExit = false;
                for (DbApplicationAutherizationGroup dbApplicationAutherizationGroup: authGrp) {
                    if (dbApplicationAutherizationGroup != null
                            && dbApplicationAutherizationGroup.getAutherizationGroup() != null
                            && authGroup.equals(dbApplicationAutherizationGroup.getAutherizationGroup())) {
                        isAuthKeyExit = true;
                    }
                }

                if (!isAuthKeyExit) {
                    throw new AuthorizationException(CommonErrorCodes.NOT_VALID_AUTH_GROUP.getErrorCode(),
                                                     CommonErrorCodes.NOT_VALID_AUTH_GROUP.getDescDe()
                                                             + applicationName);
                }
            }
            else {
                throw new AuthorizationException(CommonErrorCodes.NOT_ALLOWED_TO_THIS_APPLICATION.getErrorCode(),
                                                 CommonErrorCodes.NOT_ALLOWED_TO_THIS_APPLICATION.getDescDe());
            }
        }
        catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        catch (EncryptionException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        catch (AuthorizationException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }

    }

    /**
     * This method decrypts the applicationName that contains encryptedName and secretCode combined. Last 2 digits is
     * the secretCode from encryptedName. With the secretCode, it checks and loads the DbApplicationIdentifier object
     * and find the secretKey to decrypt ApplicationName.
     * 
     * @param encryptedName
     * @return
     */
    @Override
    public String decryptApplicationName(final String encryptedName) {

        /**
         * checks encryptedName should not be empty or null.
         */
        if (StringUtils.isEmpty(encryptedName)) {
            throw new ValidationException(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getErrorCode(),
                                          CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe());
        }

        /**
         * checks encryptedName should have more than 2 chars. Last 2 value should be digit that is the secretCode for
         * decrypt the application name
         */
        if (encryptedName.length() <= Constants.APPLICATION_CODE) {
            throw new ValidationException(CommonErrorCodes.ENCRYPTED_NAME_NOT_VALID.getErrorCode(),
                                          CommonErrorCodes.ENCRYPTED_NAME_NOT_VALID.getDescDe());
        }

        String secretCode = encryptedName.substring(encryptedName.length() - 2);
        String encodedAppName = encryptedName.substring(0, encryptedName.length() - 2);

        LOGGER.info("SecretCode: " + secretCode + "EncodedAppName: " + encodedAppName);

        if (!StringUtils.isEmpty(secretCode)) {
            DbApplicationIdentifier dbApplicationIdentifier =
                    dbApplicationIdentifierRepository.findBySecretCode(secretCode);

            if (dbApplicationIdentifier != null && !StringUtils.isEmpty(dbApplicationIdentifier.getSecretKey())) {
                return new DataEncrypter(dbApplicationIdentifier.getSecretKey()).decrypt(encodedAppName);
            }
            else {
                throw new AuthorizationException(CommonErrorCodes.NOT_ALLOWED_TO_THIS_APPLICATION.getErrorCode(),
                                                 CommonErrorCodes.NOT_ALLOWED_TO_THIS_APPLICATION.getDescDe());
            }
        }
        else {
            throw new AuthorizationException(CommonErrorCodes.SECRET_NULL_OR_EMPTY.getErrorCode(),
                                             CommonErrorCodes.SECRET_NULL_OR_EMPTY.getDescDe());
        }

    }

    /**
     * Get SecreteKey By given ApplicationName from database. It finds from the DbApplicationIdentifier collection.
     * 
     * @param applicationName
     * @return
     */
    @Override
    public String getSecreteKeyByApplicationName(String applicationName) {

        LOGGER.info("ApplicationName: " + applicationName);

        DbApplicationIdentifier dbApplicationIdentifier = null;
        try {
            dbApplicationIdentifier = dbApplicationIdentifierRepository.findByApplicationName(applicationName);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }
        if (dbApplicationIdentifier == null) {
            throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                        CommonErrorCodes.COMMON001.getDescDe());
        }
        ApplicationIdentifierV1 applicationIdentifier =
                ConversionUtil.convertFromDbApplicationIdentifier(dbApplicationIdentifier);
        return applicationIdentifier.getSecretKey();
    }

    /**
     * return get list of Authorized Groups for given Application name.
     * 
     * @param applicationName
     * @return
     */
    @Override
    public List<ApplicationAutherizationGroupV1> getListOfAuthorizedGroups(String applicationName) {

        LOGGER.info("ApplicationName: " + applicationName);

        DbApplicationIdentifier dbApplicationIdentifier = null;
        try {
            dbApplicationIdentifier = dbApplicationIdentifierRepository.findByApplicationName(applicationName);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }
        if (dbApplicationIdentifier == null) {
            throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                        CommonErrorCodes.COMMON001.getDescDe());
        }

        ApplicationIdentifierV1 applicationIdentifier =
                ConversionUtil.convertFromDbApplicationIdentifier(dbApplicationIdentifier);

        if (applicationIdentifier != null
                && !CollectionUtils.isEmpty(applicationIdentifier.getApplicationAutherizationGrp())) {
            return applicationIdentifier.getApplicationAutherizationGrp();
        }

        return null;
    }

    /**
     * Get SecreteKey By given ApplicationName from database. It finds from the DbApplicationIdentifier collection.
     * 
     * @param applicationName
     * @return
     */
    @Override
    public ApplicationIdentifierV1 findByApplicationName(String applicationName) {

        LOGGER.info("ApplicationName: " + applicationName);

        DbApplicationIdentifier dbApplicationIdentifier = null;
        try {
            dbApplicationIdentifier = dbApplicationIdentifierRepository.findByApplicationName(applicationName);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }
        if (dbApplicationIdentifier == null) {
            throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                        CommonErrorCodes.COMMON001.getDescDe());
        }
        ApplicationIdentifierV1 applicationIdentifier =
                ConversionUtil.convertFromDbApplicationIdentifier(dbApplicationIdentifier);
        return applicationIdentifier;
    }

}
