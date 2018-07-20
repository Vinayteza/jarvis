package com.daimler.duke.document.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.dto.CreateContentRestResponseV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.service.IDocumentService;
import com.daimler.duke.document.service.TokenStatusService;
import com.daimler.duke.document.util.MockObjectTest;

/**
 * unit test for DocumentContentResourceController class.
 * 
 * @author NAYASAR
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentContentResourceController.class, DocumentResourceLogController.class
})
public class DocumentContentResourceControllerTest {

    private TokenStatusService                tokenStatusService;

    private IDocumentService                  documentService;

    private DocumentContentResourceController documentContentResourceController;

    @Before
    public void init() throws Exception {

        PowerMockito.mockStatic(DocumentResourceLogController.class);
        PowerMockito.doNothing()
                    .when(DocumentResourceLogController.class, "logJsonRequest", Mockito.any(DocumentV1.class));

        documentService = PowerMockito.mock(IDocumentService.class);
        tokenStatusService = PowerMockito.mock(TokenStatusService.class);
        documentContentResourceController = new DocumentContentResourceController();
        Whitebox.setInternalState(documentContentResourceController, "documentService", documentService);
        Whitebox.setInternalState(documentContentResourceController, "tokenStatusService", tokenStatusService);

    }

    @Test
    public void testCreateDocument() {

        String documentId = "5a223f3e5953c7136439beb566";

        DocumentContentV1 documentContent = MockObjectTest.createDocumentContent();
        documentContent.setDocumentMtDtdId(documentId);

        PowerMockito.when(documentService.createDocumentContent(Mockito.any(DocumentContentV1.class)))
                    .thenReturn("75685687");

        DbTokenStatus tokenObj = new DbTokenStatus();
        tokenObj.setDocumentId(documentId);

        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class))).thenReturn(tokenObj);

        // method under test
        CreateContentRestResponseV1 response =
                documentContentResourceController.createDocumentContent("token123", documentContent);

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be ", response.getDocumentContentId(), equalTo("75685687"));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testCreateDocumentIfNotValidToken() {
        try {
            String documentId = "5a223f3e5953c7136439beb566";

            DocumentContentV1 documentContent = MockObjectTest.createDocumentContent();
            documentContent.setDocumentMtDtdId(documentId);

            PowerMockito.when(documentService.createDocumentContent(Mockito.any(DocumentContentV1.class)))
                        .thenReturn("75685687");

            DbTokenStatus tokenObj = new DbTokenStatus();
            tokenObj.setDocumentId("r67568787979789");

            PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                        .thenReturn(tokenObj);

            // method under test

            documentContentResourceController.createDocumentContent("token123", documentContent);

        }
        catch (Exception ex) {
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.NOT_VALID_FOR_DOCUMENT_ID.getDescDe()));

        }
    }

}
