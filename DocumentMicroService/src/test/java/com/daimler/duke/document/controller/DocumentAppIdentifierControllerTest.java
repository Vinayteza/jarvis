package com.daimler.duke.document.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daimler.duke.document.dto.ApplicationAutherizationGroupV1;
import com.daimler.duke.document.dto.ApplicationIdentifierV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.dto.RestResponseValue;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;

/**
 * unit test for DocumentAppIdentifierController class.
 * 
 * @author NAYASAR
 *
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentAppIdentifierController.class, DocumentResourceLogController.class
})
public class DocumentAppIdentifierControllerTest {

    private IDocumentAppIdentifierService   documentAppIdentifierService;
    private DocumentAppIdentifierController documentAppIdentifierController;

    @Before
    public void init() throws Exception {
        PowerMockito.mockStatic(DocumentResourceLogController.class);
        PowerMockito.doNothing().when(DocumentResourceLogController.class,
                                      "logJsonRequest",
                                      Mockito.any(ApplicationIdentifierV1.class));
        documentAppIdentifierService = PowerMockito.mock(IDocumentAppIdentifierService.class);

        documentAppIdentifierController = new DocumentAppIdentifierController();
        Whitebox.setInternalState(documentAppIdentifierController,
                                  "documentAppIdentifierService",
                                  documentAppIdentifierService);

    }

    @Test
    public void testCreateDocumentApplicationIdentifier() {

        ApplicationIdentifierV1 applicationIdentifier = createApplicationIdentifier();

        PowerMockito.when(documentAppIdentifierService.createDocumentApplicationIdentifier(Mockito.any(ApplicationIdentifierV1.class)))
                    .thenReturn("5a223f3e5953c7136439beb5");

        // method under test
        RestResponseObject response =
                documentAppIdentifierController.createDocumentApplicationIdentifier(applicationIdentifier);

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be " + "5a223f3e5953c7136439beb5",
                   response.getResult(),
                   equalTo("5a223f3e5953c7136439beb5"));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));

    }

    @Test
    public void testGetDocumentApplicationIdentifier() {

        String documentId = "5a223f3e5953c7136439beb5";
        ApplicationIdentifierV1 applicationIdentifier = createApplicationIdentifier();

        PowerMockito.when(documentAppIdentifierService.getDocumentApplicationIdentifier(Mockito.any(String.class)))
                    .thenReturn(applicationIdentifier);

        // method under test
        RestResponseObject response = documentAppIdentifierController.getDocumentApplicationIdentifier(documentId);
        ApplicationIdentifierV1 result = (ApplicationIdentifierV1) response.getResult();

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be " + documentId, result.getApplicationId(), equalTo(documentId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));

    }

    @Test
    public void testGetDocumentApplicationIdentifierPath() {
        try {

            PowerMockito.when(documentAppIdentifierService.getDocumentApplicationIdentifier(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Object not found"));
            // method under test
            documentAppIdentifierController.getDocumentApplicationIdentifier("75678568687");
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Object not found"));
        }
    }

    @Test
    public void testDeleteApplicationIdentifier() throws Exception {

        String appId = "5a223f3e5953c7136439beb566";
        PowerMockito.doNothing().when(documentAppIdentifierService,
                                      "deleteDocumentApplicationIdentifier",
                                      Mockito.any(String.class));

        // method under test
        documentAppIdentifierController.deleteDocumentApplicationIdentifier(appId);
    }

    //

    @Test
    public void testUpdateDocumentApplicationIdentifier() {

        String documentId = "5a223f3e5953c7136439beb5";
        ApplicationIdentifierV1 applicationIdentifier = createApplicationIdentifier();

        PowerMockito.when(documentAppIdentifierService.updateDocumentApplicationIdentifier(Mockito.any(ApplicationIdentifierV1.class)))
                    .thenReturn(documentId);

        // method under test
        RestResponseObject response =
                documentAppIdentifierController.updateDocumentApplicationIdentifier(applicationIdentifier);
        String result = (String) response.getResult();

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be " + documentId, result, equalTo(documentId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));

    }

    @Test
    public void testUpdateDocumentAppIdentifierIfException() {
        try {
            ApplicationIdentifierV1 applicationIdentifier = createApplicationIdentifier();

            PowerMockito.when(documentAppIdentifierService.updateDocumentApplicationIdentifier(Mockito.any(ApplicationIdentifierV1.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            documentAppIdentifierController.updateDocumentApplicationIdentifier(applicationIdentifier);
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Something went wrong"));
        }
    }

    //

    @Test
    public void testGetSecreteKeyByApplicationName() {

        String documentId = "5a223f3e5953c7136439beb5";
        ApplicationIdentifierV1 applicationIdentifier = createApplicationIdentifier();

        PowerMockito.when(documentAppIdentifierService.getSecreteKeyByApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        // method under test
        RestResponseValue response = documentAppIdentifierController.getSecreteKeyByApplicationName("Duke");
        String secretKey = (String) response.getValue();

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be ", secretKey, equalTo("Duke"));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));

    }

    @Test
    public void testGetSecreteKeyByApplicationNameIfException() {
        try {
            PowerMockito.when(documentAppIdentifierService.getSecreteKeyByApplicationName(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            documentAppIdentifierController.getSecreteKeyByApplicationName("Duke");
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Something went wrong"));
        }
    }

    //

    @Test
    public void testGetListOfAuthorizedGroups() {

        List<ApplicationAutherizationGroupV1> listOfAuthorizedGroups = new ArrayList();
        PowerMockito.when(documentAppIdentifierService.getListOfAuthorizedGroups(Mockito.any(String.class)))
                    .thenReturn(listOfAuthorizedGroups);

        // method under test
        RestResponseObject response = documentAppIdentifierController.getListOfAuthorizedGroups("Duke");
        List<ApplicationAutherizationGroupV1> list = (List<ApplicationAutherizationGroupV1>) response.getResult();

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        Assert.assertNotNull(list);
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));

    }

    @Test
    public void testGetListOfAuthorizedGroupsIfException() {
        try {
            PowerMockito.when(documentAppIdentifierService.getListOfAuthorizedGroups(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            documentAppIdentifierController.getListOfAuthorizedGroups("Duke");
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Something went wrong"));
        }
    }

    private ApplicationIdentifierV1 createApplicationIdentifier() {
        ApplicationIdentifierV1 applicationIdentifier = new ApplicationIdentifierV1();
        applicationIdentifier.setApplicationId("5a223f3e5953c7136439beb5");
        applicationIdentifier.setApplicationName("Duke");
        applicationIdentifier.setSecretCode("12");
        applicationIdentifier.setSecretKey("Duke");
        applicationIdentifier.setFirstName("Andreas");
        applicationIdentifier.setLastName("Gollmann");
        applicationIdentifier.setDepartment("ITP/DT");
        return applicationIdentifier;
    }

}
