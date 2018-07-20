package com.daimler.duke.document.interceptor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.daimler.duke.document.dto.ApplicationIdentifierV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.dto.UniqueId;
import com.daimler.duke.document.util.MockObjectTest;
import com.daimler.duke.document.util.OperationType;

public class RequestValidatorTest {

    @Test
    public void testValidateDocumentMetaDataRequestMetaDataNull() {
        List<String> errorList = RequestValidator.validateDocumentMetaDataRequest(null, OperationType.CREATE);
        assertThat("Error List is not Empty", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentMetaDataDocumentIdNotNull() {
        List<String> errorList =
                RequestValidator.validateDocumentMetaDataRequest(MockObjectTest.createDocumentMataDataForNullId(),
                                                                 OperationType.CREATE);
        assertThat("Document Id should be null", errorList.size(), is(0));
    }

    @Test
    public void testValidateDocumentMetaDataDocumentIdNull() {
        List<String> errorList =
                RequestValidator.validateDocumentMetaDataRequest(MockObjectTest.createDocumentMataDataForRandomId(),
                                                                 OperationType.CREATE);
        assertThat("Document Id should be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentMetaDataDocumentIdNullForUpdate() {
        List<String> errorList =
                RequestValidator.validateDocumentMetaDataRequest(MockObjectTest.createDocumentMataDataForNullId(),
                                                                 OperationType.UPDATE);
        assertThat("Document Id should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentMetaDataDocumentIdNullDocumentType() {
        DocumentMetaDataV1 metaData = MockObjectTest.createDocumentMataDataForRandomId();
        metaData.setDocumentType(null);
        List<String> errorList = RequestValidator.validateDocumentMetaDataRequest(metaData, OperationType.UPDATE);
        assertThat("Document type should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentMetaDataDocumentIdNullDocumentName() {
        DocumentMetaDataV1 metaData = MockObjectTest.createDocumentMataDataForRandomId();
        metaData.setDocumentName(null);
        List<String> errorList = RequestValidator.validateDocumentMetaDataRequest(metaData, OperationType.UPDATE);
        assertThat("Document Name should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentRequestNullMetaData() {
        DocumentV1 document = MockObjectTest.createDocument();
        document.setDocumentMetaData(null);
        List<String> errorList = RequestValidator.validateDocumentRequest(document, OperationType.CREATE);
        assertThat("Document Metadata should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentRequestNotNullMetaData() {
        DocumentV1 document = MockObjectTest.createDocument();
        List<String> errorList = RequestValidator.validateDocumentRequest(document, OperationType.CREATE);
        assertThat("Document MetaData should not be null", errorList.size(), is(0));
    }

    @Test
    public void testValidateDocumentRequestNullDocType() {
        DocumentV1 document = MockObjectTest.createDocument();
        document.getDocumentMetaData().setDocumentType(null);
        List<String> errorList = RequestValidator.validateDocumentRequest(document, OperationType.CREATE);
        assertThat("Document MetaData should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentRequestNullDocName() {
        DocumentV1 document = MockObjectTest.createDocument();
        document.getDocumentMetaData().setDocumentName(null);
        List<String> errorList = RequestValidator.validateDocumentRequest(document, OperationType.CREATE);
        assertThat("Document Name should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentRequestNullDocContent() {
        DocumentV1 document = MockObjectTest.createDocument();
        document.setDocumentContent(null);
        List<String> errorList = RequestValidator.validateDocumentRequest(document, OperationType.CREATE);
        assertThat("Document Content should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentRequestNullDocContentData() {
        DocumentV1 document = MockObjectTest.createDocument();
        document.getDocumentContent().setData(null);
        List<String> errorList = RequestValidator.validateDocumentRequest(document, OperationType.CREATE);
        assertThat("Document Content Data should not be null", errorList.size(), not(0));
    }

    @Test
    public void testValidateDocumentRequestNullDocContentDataForUpdate() {
        DocumentV1 document = MockObjectTest.createDocument();
        document.getDocumentContent().setData(null);
        List<String> errorList = RequestValidator.validateDocumentRequest(document, OperationType.UPDATE);
        assertThat("Document Content Data should not be null", errorList.size(), is(1));
    }

    @Test
    public void testIsUniqueIdCorrectContentDataCorrectId() {
        UniqueId uniqueId = new UniqueId();
        uniqueId.setExtension("1234");
        uniqueId.setRoot("1.2.3.4.5.6.7.88");
        assertTrue(RequestValidator.isUniqueIdCorrect(uniqueId));
    }

    @Test
    public void testIsUniqueIdCorrectContentDataNullExt() {
        UniqueId uniqueId = new UniqueId();
        uniqueId.setExtension(null);
        uniqueId.setRoot("1.2.3.4.5.6.7.88");
        assertFalse(RequestValidator.isUniqueIdCorrect(uniqueId));
        uniqueId.setExtension("");
        assertFalse(RequestValidator.isUniqueIdCorrect(uniqueId));
    }

    @Test
    public void testIsUniqueIdCorrectContentDataNullRoot() {
        UniqueId uniqueId = new UniqueId();
        uniqueId.setExtension("12334");
        uniqueId.setRoot(null);
        assertFalse(RequestValidator.isUniqueIdCorrect(uniqueId));

        uniqueId.setRoot("");
        assertFalse(RequestValidator.isUniqueIdCorrect(uniqueId));
    }

    @Test
    public void testValidateApplicationIdentifierRequest() {
        ApplicationIdentifierV1 appId = new ApplicationIdentifierV1();
        appId.setApplicationId("DukE");
        List<String> errorList = RequestValidator.validateApplicationIdentifierRequest(appId, OperationType.CREATE);
        assertThat("AppId should not be null", errorList.size(), not(0));

        errorList = RequestValidator.validateApplicationIdentifierRequest(null, OperationType.CREATE);
        assertThat("AppId should not be null", errorList.size(), not(0));

        appId.setApplicationId(null);
        errorList = RequestValidator.validateApplicationIdentifierRequest(appId, OperationType.UPDATE);
        assertThat("AppId should not be null", errorList.size(), not(0));
    }

}
