package com.daimler.duke.document.service;

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

import com.daimler.duke.document.db.entity.DbApplicationAutherizationGroup;
import com.daimler.duke.document.db.entity.DbApplicationIdentifier;
import com.daimler.duke.document.dto.ApplicationAutherizationGroupV1;
import com.daimler.duke.document.dto.ApplicationIdentifierV1;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.repository.DbApplicationIdentifierRepository;
import com.daimler.duke.document.util.DataEncrypter;

import io.jsonwebtoken.Claims;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentAppIdentifierService.class
})
public class DocumentAppIdentifierServiceTest {

    private DbApplicationIdentifierRepository dbApplicationIdentifierRepository;
    private DocumentAppIdentifierService      documentAppIdentifierService;

    @Before
    public void init() throws Exception {

        dbApplicationIdentifierRepository = PowerMockito.mock(DbApplicationIdentifierRepository.class);

        documentAppIdentifierService = new DocumentAppIdentifierService();
        Whitebox.setInternalState(documentAppIdentifierService,
                                  "dbApplicationIdentifierRepository",
                                  dbApplicationIdentifierRepository);

    }

    @Test
    public void testGetDocumentApplicationIdentifier() {

        DbApplicationIdentifier dbApplicationIdentifier = createDbApplicationIdentifier();
        PowerMockito.when(dbApplicationIdentifierRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbApplicationIdentifier);

        // method under test
        ApplicationIdentifierV1 applicationIdentifierV1 =
                documentAppIdentifierService.getDocumentApplicationIdentifier("5a223f3e5953c7136439beb5");

        // Assertions here.
        Assert.assertNotNull(applicationIdentifierV1);
        assertThat("ApplicationId", applicationIdentifierV1.getApplicationId(), equalTo("5a223f3e5953c7136439beb5"));
        assertThat("ApplicationName", applicationIdentifierV1.getApplicationName(), equalTo("Duke"));
        assertThat("SecretCode ", applicationIdentifierV1.getSecretCode(), equalTo("12"));

    }

    @Test
    public void testDocumentApplicationIdentifierIsNotExist() {

        try {
            PowerMockito.when(dbApplicationIdentifierRepository.findOne(Mockito.any(String.class))).thenReturn(null);
            // method under test
            documentAppIdentifierService.getDocumentApplicationIdentifier("5a223f3e5953c7136439beb5");
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testDocumentApplicationIdentifierErrorPath() {

        try {
            PowerMockito.when(dbApplicationIdentifierRepository.findOne(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentAppIdentifierService.getDocumentApplicationIdentifier("5a223f3e5953c7136439beb5");
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testCreateDocumentApplicationIdentifier() {
        DbApplicationIdentifier dbApplicationIdentifier = createDbApplicationIdentifier();

        ApplicationIdentifierV1 applicationIdentifierV1 = createApplicationIdentifier();
        applicationIdentifierV1.setApplicationId(null);
        PowerMockito.when(dbApplicationIdentifierRepository.insert(Mockito.any(DbApplicationIdentifier.class)))
                    .thenReturn(dbApplicationIdentifier);

        // method under test
        String applicationId =
                documentAppIdentifierService.createDocumentApplicationIdentifier(applicationIdentifierV1);

        // Assertions here.
        Assert.assertNotNull(applicationId);
        assertThat("ApplicationId", applicationId, equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testCreateDocumentApplicationIdentifierErrorPath() {
        ApplicationIdentifierV1 applicationIdentifierV1 = createApplicationIdentifier();
        applicationIdentifierV1.setApplicationId(null);
        try {
            PowerMockito.when(dbApplicationIdentifierRepository.insert(Mockito.any(DbApplicationIdentifier.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentAppIdentifierService.createDocumentApplicationIdentifier(applicationIdentifierV1);
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testSecretsCodeValidationError() {
        ApplicationIdentifierV1 applicationIdentifierV1 = createApplicationIdentifier();
        applicationIdentifierV1.setApplicationId(null);
        applicationIdentifierV1.setSecretCode(null);
        try {
            // method under test
            documentAppIdentifierService.createDocumentApplicationIdentifier(applicationIdentifierV1);
            Assert.fail();
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo("SecretsCode is required"));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("SecretsCode is required"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testDeleteDocumentApplicationIdentifier() throws Exception {

        // PowerMockito.doNothing().when(dbApplicationIdentifierRepository, "delete", Mockito.any(String.class));
        // method under test
        documentAppIdentifierService.deleteDocumentApplicationIdentifier("65656565656565");
    }

    @Test
    public void testDeleteDocumentApplicationIdentifierErrorPath() {
        try {
            Mockito.doThrow(new RuntimeException("Can not find object"))
                   .doNothing()
                   .when(dbApplicationIdentifierRepository)
                   .delete(Mockito.any(String.class));

            // method under test
            documentAppIdentifierService.deleteDocumentApplicationIdentifier("78678tuijkjkljkln");
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Can not find object"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testUpdateDocumentApplicationIdentifier() {

        DbApplicationIdentifier dbApplicationIdentifier = createDbApplicationIdentifier();
        ApplicationIdentifierV1 applicationIdentifierV1 = createApplicationIdentifier();
        PowerMockito.when(dbApplicationIdentifierRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbApplicationIdentifier);

        PowerMockito.when(dbApplicationIdentifierRepository.save(Mockito.any(DbApplicationIdentifier.class)))
                    .thenReturn(dbApplicationIdentifier);

        // method under test
        String applicationId =
                documentAppIdentifierService.updateDocumentApplicationIdentifier(applicationIdentifierV1);

        // Assertions here.
        Assert.assertNotNull(applicationId);
        assertThat("ApplicationId", applicationId, equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testUpdateDocumentApplicationIdentifierErrorPath() {

        ApplicationIdentifierV1 applicationIdentifierV1 = createApplicationIdentifier();
        try {

            PowerMockito.when(dbApplicationIdentifierRepository.findOne(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            documentAppIdentifierService.updateDocumentApplicationIdentifier(applicationIdentifierV1);
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testUpdateDocumentAppFindOneReturnsNull() {

        ApplicationIdentifierV1 applicationIdentifierV1 = createApplicationIdentifier();
        try {

            PowerMockito.when(dbApplicationIdentifierRepository.findOne(Mockito.any(String.class))).thenReturn(null);

            // method under test
            documentAppIdentifierService.updateDocumentApplicationIdentifier(applicationIdentifierV1);
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testVerifyApplicationName() throws Exception {

        String encodedAppName = "V+jtQF70lKo=12";
        Claims claims = PowerMockito.mock(Claims.class);
        PowerMockito.when(claims.get(Mockito.any(String.class))).thenReturn("authoGroup123");
        DbApplicationIdentifier dbApplicationIdentifier = createDbApplicationIdentifier();
        PowerMockito.when(dbApplicationIdentifierRepository.findBySecretCode(Mockito.any(String.class)))
                    .thenReturn(dbApplicationIdentifier);

        PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                    .thenReturn(dbApplicationIdentifier);

        DataEncrypter dataEncrypter = PowerMockito.mock(DataEncrypter.class);
        PowerMockito.whenNew(DataEncrypter.class).withArguments(Mockito.any(String.class)).thenReturn(dataEncrypter);
        PowerMockito.when(dataEncrypter.decrypt(Mockito.any(String.class))).thenReturn("Duke");

        // method under test
        documentAppIdentifierService.verifyApplicationName(claims, encodedAppName);
    }

    @Test
    public void testListOfAuthorizedGroups() throws Exception {

        PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                    .thenReturn(createDbApplicationIdentifier());

        // method under test
        List<ApplicationAutherizationGroupV1> listOfAuthorizedGroups =
                documentAppIdentifierService.getListOfAuthorizedGroups("Duke");

        // Assertions here.
        Assert.assertNotNull(listOfAuthorizedGroups);
        assertThat("Size ", listOfAuthorizedGroups.size(), equalTo(1));
        assertThat("AutherizationGroup Name ",
                   listOfAuthorizedGroups.get(0).getAutherizationGroup(),
                   equalTo("authoGroup123"));
    }

    @Test
    public void testIfNoAuthorizedGroupsFound() throws Exception {
        try {
            PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                        .thenReturn(null);
            // method under test
            documentAppIdentifierService.getListOfAuthorizedGroups("Duke");
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }

    }

    @Test
    public void testfindAuthorizedGroupsForError() throws Exception {
        try {
            PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentAppIdentifierService.getListOfAuthorizedGroups("Duke");
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }

    }

    /////

    @Test
    public void testSecreteKeyByApplicationName() throws Exception {

        PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                    .thenReturn(createDbApplicationIdentifier());

        // method under test
        String secreteKey = documentAppIdentifierService.getSecreteKeyByApplicationName("Duke");

        // Assertions here.
        Assert.assertNotNull(secreteKey);
        assertThat("secreteKey ", secreteKey, equalTo("DukeApplication"));
    }

    @Test
    public void testIfNoSecreteKeyByApplicationNameFound() throws Exception {
        try {
            PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                        .thenReturn(null);
            // method under test
            documentAppIdentifierService.getSecreteKeyByApplicationName("Duke");
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }

    }

    @Test
    public void testSecreteKeyByApplicationNameForError() throws Exception {
        try {
            PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentAppIdentifierService.getSecreteKeyByApplicationName("Duke");
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }

    }

    @Test
    public void testDecryptApplicationName() throws Exception {

        String encryptedAppName = "V+jtQF70lKo=12";
        DbApplicationIdentifier dbApplicationIdentifier = createDbApplicationIdentifier();
        PowerMockito.when(dbApplicationIdentifierRepository.findBySecretCode(Mockito.any(String.class)))
                    .thenReturn(dbApplicationIdentifier);

        DataEncrypter dataEncrypter = PowerMockito.mock(DataEncrypter.class);
        PowerMockito.whenNew(DataEncrypter.class).withArguments(Mockito.any(String.class)).thenReturn(dataEncrypter);
        PowerMockito.when(dataEncrypter.decrypt(Mockito.any(String.class))).thenReturn("Duke");

        // method under test
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedAppName);

        // Assertions here.
        Assert.assertNotNull(applicationName);
        assertThat("ApplicationName ", applicationName, equalTo("Duke"));
    }

    @Test
    public void testEncryptedNameIsNull() throws Exception {
        try {
            // method under test
            documentAppIdentifierService.decryptApplicationName(null);

        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(117));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testEncryptedNameIsNotValid() throws Exception {
        try {
            String encryptedAppName = "V+";
            // method under test
            documentAppIdentifierService.decryptApplicationName(encryptedAppName);

        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.ENCRYPTED_NAME_NOT_VALID.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(107));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.ENCRYPTED_NAME_NOT_VALID.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    private DbApplicationIdentifier createDbApplicationIdentifier() {
        DbApplicationIdentifier dbApplicationIdentifier = new DbApplicationIdentifier();
        dbApplicationIdentifier.setApplicationId("5a223f3e5953c7136439beb5");
        dbApplicationIdentifier.setApplicationName("Duke");
        dbApplicationIdentifier.setSecretCode("12");
        dbApplicationIdentifier.setSecretKey("DukeApplication");
        dbApplicationIdentifier.setFirstName("Andreas");
        dbApplicationIdentifier.setLastName("Gollmann");
        dbApplicationIdentifier.setDepartment("ITP/DT");
        List<DbApplicationAutherizationGroup> dbApplicationAutherizationGrpList = new ArrayList<>();
        DbApplicationAutherizationGroup dbApplicationAutherizationGroup = new DbApplicationAutherizationGroup();
        dbApplicationAutherizationGroup.setAutherizationGroup("authoGroup123");
        dbApplicationAutherizationGrpList.add(dbApplicationAutherizationGroup);
        dbApplicationIdentifier.setApplicationAutherizationGrp(dbApplicationAutherizationGrpList);

        return dbApplicationIdentifier;
    }

    private ApplicationIdentifierV1 createApplicationIdentifier() {
        ApplicationIdentifierV1 applicationIdentifier = new ApplicationIdentifierV1();
        applicationIdentifier.setApplicationId("5a223f3e5953c7136439beb5");
        applicationIdentifier.setApplicationName("Duke");
        applicationIdentifier.setSecretCode("12");
        applicationIdentifier.setSecretKey("DukeApplication");
        applicationIdentifier.setFirstName("Andreas");
        applicationIdentifier.setLastName("Gollmann");
        applicationIdentifier.setDepartment("ITP/DT");
        return applicationIdentifier;
    }

}
