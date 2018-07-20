package com.daimler.duke.document.util;

import java.util.Map;

import com.daimler.duke.document.dto.CreateContentRestResponseV1;
import com.daimler.duke.document.dto.CreateMetadataIdIndexRestResponseV1;
import com.daimler.duke.document.dto.CreateMetadataIdTokenRestResponseV1;
import com.daimler.duke.document.dto.CreateMetatadaRestResponseV1;
import com.daimler.duke.document.dto.CreateUploadChunkSizeResponseV1;
import com.daimler.duke.document.dto.GetContentRestResponseV1;
import com.daimler.duke.document.dto.GetDocumentRestResponseV1;
import com.daimler.duke.document.dto.GetMetadataRestResponseV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.dto.RestResponseValue;

/**
 * @author SANDGUP.
 *
 */
public final class RequestResponseUtil {
    /**
     * . RequestResponseUtil
     */
    private RequestResponseUtil() {
    }

    /**
     * @param payload
     * @return RestResponseObject
     */
    public static RestResponseObject convertToRestResponseObject(final Object payload) {
        return new RestResponseObject(payload);
    }

    /**
     * @param value
     * @return RestResponseValue
     */
    public static RestResponseValue convertToRestResponseValue(final Object value) {
        return new RestResponseValue(value);
    }

    /**
     * 
     * @param metadataId
     * @return CreateMetatadaRestResponse
     */
    public static CreateMetatadaRestResponseV1 convertToCreateMetatadaRestResponse(final String metadataId) {
        return new CreateMetatadaRestResponseV1(metadataId);
    }

    /**
     * 
     * @param documentMetaData
     * @return GetMetadataRestResponse
     */
    public static GetMetadataRestResponseV1 convertToGetMetadataRestResponse(final Object documentMetaData) {
        return new GetMetadataRestResponseV1(documentMetaData);
    }

    /**
     * 
     * @param metaDataTokenMap
     * @return CreateMetadataIdTokenRestResponse
     */
    public static CreateMetadataIdTokenRestResponseV1 convertToCreateMetadataIdTokenRestResponse(final Object metaDataTokenMap) {
        return new CreateMetadataIdTokenRestResponseV1(metaDataTokenMap);
    }

    /**
     * 
     * @param documentContent
     * @return GetContentRestResponse
     */
    public static GetContentRestResponseV1 convertToGetContentRestResponse(final Object documentContent) {
        return new GetContentRestResponseV1(documentContent);
    }

    /**
     * 
     * @param documentContent
     * @return GetDocumentRestResponse
     */
    public static GetDocumentRestResponseV1 convertToGetDocumentRestResponse(final Object documentContent) {
        return new GetDocumentRestResponseV1(documentContent);
    }

    /**
     * 
     * @param documentContent
     * @return CreateMetadataIdIndexRestResponse
     */
    public static CreateMetadataIdIndexRestResponseV1 convertToCreateMetadataIdIndexRestResponse(final Map<Integer,String> documentContent) {
        return new CreateMetadataIdIndexRestResponseV1(documentContent);
    }

    /**
     * 
     * @param documentContent
     * @return
     */
    public static CreateContentRestResponseV1 convertToCreateContentRestResponseV1(final String documentContent) {
        return new CreateContentRestResponseV1(documentContent);
    }

    /**
     * 
     * @param uploadCunkSize
     * @return
     */
    public static CreateUploadChunkSizeResponseV1 convertToCreateUploadCunkSizeResponseV1(final String uploadCunkSize) {
        return new CreateUploadChunkSizeResponseV1(uploadCunkSize);
    }

}
