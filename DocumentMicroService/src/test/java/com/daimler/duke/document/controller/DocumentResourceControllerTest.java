package com.daimler.duke.document.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daimler.duke.document.dto.CreateContentRequestV1;
import com.daimler.duke.document.dto.CreateDocumentRequestV1;
import com.daimler.duke.document.dto.CreateMetadataIdIndexRestResponseV1;
import com.daimler.duke.document.dto.CreateMetatadaRestResponseV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.dto.GetContentRestResponseV1;
import com.daimler.duke.document.dto.GetDocumentRestResponseV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;
import com.daimler.duke.document.service.IDocumentService;
import com.daimler.duke.document.util.ConversionUtil;

/**
 * unit test for DocumentResourceController class.
 * 
 * @author NAYASAR
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentResourceController.class, DocumentResourceLogController.class
})
public class DocumentResourceControllerTest {

    private DocumentResourceController    documentResourceController;
    private IDocumentService              documentService;
    private IDocumentAppIdentifierService documentAppIdentifierService;

    @Before
    public void init() throws Exception {

        PowerMockito.mockStatic(DocumentResourceLogController.class);
        PowerMockito.doNothing()
                    .when(DocumentResourceLogController.class, "logJsonRequest", Mockito.any(DocumentV1.class));

        documentService = PowerMockito.mock(IDocumentService.class);
        documentAppIdentifierService = PowerMockito.mock(IDocumentAppIdentifierService.class);
        documentResourceController = new DocumentResourceController();
        Whitebox.setInternalState(documentResourceController, "documentService", documentService);
        Whitebox.setInternalState(documentResourceController,
                                  "documentAppIdentifierService",
                                  documentAppIdentifierService);

    }

    @Test
    public void testCreateDocument() {

        String docId = "5a223f3e5953c7136439beb566";

        DocumentMetaDataV1 documentMetaData = createDocumentMataData("", "TerstName", "pdf", 1l);
        CreateContentRequestV1 documentContent = new CreateContentRequestV1();
        documentContent.setData("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd");
        CreateDocumentRequestV1 document = new CreateDocumentRequestV1();
        document.setDocumentMetaData(ConversionUtil.convertFromDocumentMetaDataV1(documentMetaData));
        document.setDocumentContent(documentContent);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");
        PowerMockito.when(documentService.createDocument(Mockito.any(String.class), Mockito.any(DocumentV1.class)))
                    .thenReturn(docId);

        // method under test
        CreateMetatadaRestResponseV1 response = documentResourceController.createDocument("token123", "DukE", document);

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be " + docId, response.getMetadataId(), equalTo(docId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testCreateDocumentErrorPath() {
        try {
            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.createDocument(Mockito.any(String.class), Mockito.any(DocumentV1.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentResourceController.createDocument("token123", "DukE", new CreateDocumentRequestV1());
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    @Test
    public void testDeleteDocument() throws Exception {

        String docId = "5a223f3e5953c7136439beb566";

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");
        PowerMockito.doNothing()
                    .when(documentService, "deleteDocument", Mockito.any(String.class), Mockito.any(String.class));

        // method under test
        documentResourceController.deleteDocument("token123", "DukE", docId);
    }

    @Test
    public void testDeleteDocumentException() {
        try {
            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            Mockito.doThrow(new RuntimeException("Can not find object"))
                   .doNothing()
                   .when(documentService)
                   .deleteDocument(Mockito.any(String.class), Mockito.any(String.class));

            // method under test
            documentResourceController.deleteDocument("token123", "DukE", "5a223f3e5953c7136439beb566");
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Can not find object"));
        }
    }

    @Test
    public void testGetDocument() {

        String documentId = "5a223f3e5953c7136439beb566";

        DocumentMetaDataV1 documentMetaData = createDocumentMataData("", "TerstName", "pdf", 1l);
        DocumentContentV1 documentContent = new DocumentContentV1();
        documentContent.setData("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd");
        DocumentV1 document = new DocumentV1();
        document.setDocumentMetaData(documentMetaData);
        document.setDocumentContent(documentContent);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        PowerMockito.when(documentService.getDocument(Mockito.any(String.class), Mockito.any(String.class)))
                    .thenReturn(document);

        // method under test
        GetDocumentRestResponseV1 restResponseObject =
                documentResourceController.getDocument("token123", "DukE", documentId);

        DocumentV1 documentV1 = (DocumentV1) restResponseObject.getDocument();

        DocumentMetaDataV1 matadata = documentV1.getDocumentMetaData();
        DocumentContentV1 content = documentV1.getDocumentContent();

        // Assertions here.
        Assert.assertNotNull(restResponseObject);
        Assert.assertNotNull(restResponseObject.getStatusCode());
        assertThat("Content data", content.getData(), equalTo("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd"));
        assertThat("DocumentType", matadata.getDocumentType(), equalTo("pdf"));
    }

    @Test
    public void testGetDocumentException() throws Exception {

        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.getDocument(Mockito.any(String.class), Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentResourceController.getDocument("token123", "DukE", "564564575767");
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    @Test
    public void testGetDocumentContent() {

        String documentId = "5a223f3e5953c7136439beb566";

        DocumentContentV1 documentContent = new DocumentContentV1();
        documentContent.setData("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd");

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");
        PowerMockito.when(documentService.getDocumentContent(Mockito.any(String.class), Mockito.any(String.class)))
                    .thenReturn(documentContent);

        // method under test
        GetContentRestResponseV1 restResponseObject =
                documentResourceController.getDocumentContent("token123", "DukE", documentId);

        DocumentContentV1 documentContentV1 = (DocumentContentV1) restResponseObject.getDocumentContent();

        // Assertions here.
        Assert.assertNotNull(restResponseObject);
        Assert.assertNotNull(restResponseObject.getStatusCode());
        assertThat("Content data",
                   documentContentV1.getData(),
                   equalTo("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd"));
    }

    @Test
    public void testGetDocumentContentException() throws Exception {

        try {
            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.getDocumentContent(Mockito.any(String.class), Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentResourceController.getDocumentContent("token123", "DukE", "564564575767");
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    //

    @Test
    public void testDeleteAllDocuments() throws Exception {

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");
        PowerMockito.doNothing()
                    .when(documentService, "deleteDocument", Mockito.any(String.class), Mockito.any(String.class));

        // method under test
        documentResourceController.deleteAllDocuments("token123", "DukE", new ArrayList<String>());
    }

    @Test
    public void testDeleteAllDocumentsException() {
        try {
            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            Mockito.doThrow(new RuntimeException("Can not find object"))
                   .doNothing()
                   .when(documentService)
                   .deleteAllDocuments(Mockito.any(String.class), Mockito.any(List.class));

            // method under test
            documentResourceController.deleteAllDocuments("token123", "DukE", new ArrayList<String>());
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Can not find object"));
        }
    }

    //
    @Test
    public void testCreateMultipleDocumentContent() throws Exception {

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        List<String> metaDataIds = new ArrayList<>();
        PowerMockito.when(documentService.createMultipleDocumentContent(Mockito.any(String.class),
                                                                        Mockito.any(List.class)))
                    .thenReturn(metaDataIds);

        // method under test
        RestResponseObject response =
                documentResourceController.createMultipleDocumentContent("token123",
                                                                         "DukE",
                                                                         new ArrayList<DocumentContentV1>());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
    }

    @Test
    public void testCreateMultipleDocumentContentException() {
        try {
            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");

            PowerMockito.when(documentService.createMultipleDocumentContent(Mockito.any(String.class),
                                                                            Mockito.any(List.class)))
                        .thenThrow(new RuntimeException("Can not find object"));

            // method under test
            documentResourceController.createMultipleDocumentContent("token123",
                                                                     "DukE",
                                                                     new ArrayList<DocumentContentV1>());
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Can not find object"));
        }
    }

    ///

    @Test
    public void testCreateMultipleDocuments() {

        String docId = "5a223f3e5953c7136439beb566";

        DocumentMetaDataV1 documentMetaData = createDocumentMataData("", "TerstName", "pdf", 1l);
        DocumentContentV1 documentContent = new DocumentContentV1();
        documentContent.setData("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd");
        DocumentV1 document = new DocumentV1();
        document.setDocumentMetaData(documentMetaData);
        document.setDocumentContent(documentContent);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");
        Map<Integer,String> docIds = new HashMap<Integer,String>();
        docIds.put(1, docId);

        PowerMockito.when(documentService.createMultipleDocuments(Mockito.any(String.class), Mockito.any(List.class)))
                    .thenReturn(docIds);

        List<CreateDocumentRequestV1> documents = new ArrayList<>();

        CreateContentRequestV1 createContentRequest = new CreateContentRequestV1();
        documentContent.setData("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd");

        CreateDocumentRequestV1 req = new CreateDocumentRequestV1();
        req.setDocumentMetaData(ConversionUtil.convertFromDocumentMetaDataV1(documentMetaData));
        req.setDocumentContent(createContentRequest);
        documents.add(req);

        // method under test
        CreateMetadataIdIndexRestResponseV1 response =
                documentResourceController.createMultipleDocuments("token123", "DukE", documents);

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        // assertThat("Response payload should be " + docId, response.getResult(), equalTo(docId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testCreateMultipleDocumentsErrorPath() {
        try {
            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.createMultipleDocuments(Mockito.any(String.class),
                                                                      Mockito.any(List.class)))
                        .thenThrow(new RuntimeException("something went wrong"));

            List<CreateDocumentRequestV1> documents = new ArrayList();
            // method under test
            documentResourceController.createMultipleDocuments("token123", "DukE", documents);
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    ///

    @Test
    public void testFetchMultipleDocuments() {

        String documentId = "5a223f3e5953c7136439beb566";

        DocumentMetaDataV1 documentMetaData = createDocumentMataData("", "TerstName", "pdf", 1l);
        DocumentContentV1 documentContent = new DocumentContentV1();
        documentContent.setData("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd");
        DocumentV1 document = new DocumentV1();
        document.setDocumentMetaData(documentMetaData);
        document.setDocumentContent(documentContent);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        List<DocumentV1> documents = new ArrayList<>();
        documents.add(document);
        PowerMockito.when(documentService.fetchMultipleDocuments(Mockito.any(String.class), Mockito.any(List.class)))
                    .thenReturn(documents);

        List<String> documentIds = new ArrayList();

        // method under test
        RestResponseObject restResponseObject =
                documentResourceController.fetchMultipleDocuments("token123", "DukE", documentIds);

        List<DocumentV1> documentList = (List) restResponseObject.getResult();

        DocumentMetaDataV1 matadata = documentList.get(0).getDocumentMetaData();
        DocumentContentV1 content = documentList.get(0).getDocumentContent();

        // Assertions here.
        Assert.assertNotNull(restResponseObject);
        Assert.assertNotNull(restResponseObject.getStatusCode());
        assertThat("Content data", content.getData(), equalTo("45b6756ututyjgyjfggdgfjhgkghfgdfgdfgdfgdfgdvgdfgd"));
        assertThat("DocumentType", matadata.getDocumentType(), equalTo("pdf"));
    }

    @Test
    public void testFetchMultipleDocumentsException() throws Exception {

        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.fetchMultipleDocuments(Mockito.any(String.class),
                                                                     Mockito.any(List.class)))
                        .thenThrow(new RuntimeException("something went wrong"));

            List<String> documents = new ArrayList();
            // method under test
            documentResourceController.fetchMultipleDocuments("token123", "DukE", documents);
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    private DocumentMetaDataV1 createDocumentMataData(String documentId, String name, String type, Long size) {
        DocumentMetaDataV1 documentMetaData = new DocumentMetaDataV1();
        documentMetaData.setDocumentId(documentId);
        documentMetaData.setDocumentName(name);
        documentMetaData.setDocumentType(type);
        documentMetaData.setComment("test");
        documentMetaData.setFirstName("Andreas");
        documentMetaData.setLastName("Gollmann");
        documentMetaData.setDepartment("ITP/DT");
        documentMetaData.setShortId("AGOLLMAN");
        return documentMetaData;
    }
}
