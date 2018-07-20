package com.daimler.duke.document.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

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

import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.dto.DocumentMetaDataTokenMapV1;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.jwt.service.IJwtAuthService;
import com.daimler.duke.document.repository.DbTokenStatusRepository;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  TokenStatusServiceTest.class
})
public class TokenStatusServiceTest {

    private DbTokenStatusRepository dbTokenStatusRepository;

    private IJwtAuthService         iJwtAuthUtilService;

    private TokenStatusService      tokenStatusService;

    @Before
    public void init() throws Exception {

        dbTokenStatusRepository = PowerMockito.mock(DbTokenStatusRepository.class);
        iJwtAuthUtilService = PowerMockito.mock(IJwtAuthService.class);

        tokenStatusService = new TokenStatusService();
        Whitebox.setInternalState(tokenStatusService, "dbTokenStatusRepository", dbTokenStatusRepository);
        Whitebox.setInternalState(tokenStatusService, "iJwtAuthUtilService", iJwtAuthUtilService);
    }

    @Test
    public void testCreateTokenStatus() {
        PowerMockito.when(dbTokenStatusRepository.insert(Mockito.any(DbTokenStatus.class)))
                    .thenReturn(createDbTokenStatus());
        DbTokenStatus tokenStatus = createDbTokenStatus();
        tokenStatus.setTokenStatusId(null);
        // method under test
        String tokenStatusId = tokenStatusService.createTokenStatus(tokenStatus);

        // Assertions here.
        Assert.assertNotNull(tokenStatusId);

    }

    @Test
    public void testCreateTokenStatusIsNotExist() {

        try {
            PowerMockito.when(dbTokenStatusRepository.insert(Mockito.any(DbTokenStatus.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            DbTokenStatus tokenStatus = createDbTokenStatus();
            tokenStatus.setTokenStatusId(null);
            // method under test
            tokenStatusService.createTokenStatus(tokenStatus);
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

    //

    @Test
    public void testFindTokenStatus() {
        PowerMockito.when(dbTokenStatusRepository.findOne(Mockito.any(String.class))).thenReturn(createDbTokenStatus());

        // method under test
        DbTokenStatus DbTokenStatusObj = tokenStatusService.findTokenStatus("75675678");

        // Assertions here.
        Assert.assertNotNull(DbTokenStatusObj);

    }

    @Test
    public void testFindTokenStatusIsNotExist() {

        try {
            PowerMockito.when(dbTokenStatusRepository.findOne(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            tokenStatusService.findTokenStatus("75676576");
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
    public void testFindTokenStatusIsNull() {

        try {
            PowerMockito.when(dbTokenStatusRepository.findOne(Mockito.any(String.class))).thenReturn(null);

            // method under test
            tokenStatusService.findTokenStatus("75676576");
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

    //

    @Test
    public void testFindTokenStatusByToken() {
        PowerMockito.when(dbTokenStatusRepository.findByTokenValue(Mockito.any(String.class)))
                    .thenReturn(createDbTokenStatus());

        // method under test
        DbTokenStatus DbTokenStatusObj = tokenStatusService.findTokenStatusByToken("75675678");

        // Assertions here.
        Assert.assertNotNull(DbTokenStatusObj);

    }

    @Test
    public void testFindTokenStatusByTokenIsNotExist() {

        try {
            PowerMockito.when(dbTokenStatusRepository.findByTokenValue(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            tokenStatusService.findTokenStatusByToken("75676576");
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
    public void testFindTokenStatusByTokenIsNull() {

        try {
            PowerMockito.when(dbTokenStatusRepository.findByTokenValue(Mockito.any(String.class))).thenReturn(null);

            // method under test
            tokenStatusService.findTokenStatusByToken("75676576");
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

    //
    @Test
    public void testUpdateTokenStatus() {

        PowerMockito.when(dbTokenStatusRepository.exists(Mockito.any(String.class))).thenReturn(true);
        DbTokenStatus tokenStatus = createDbTokenStatus();
        PowerMockito.when(dbTokenStatusRepository.save(Mockito.any(DbTokenStatus.class))).thenReturn(tokenStatus);

        // method under test
        String tokenStatusId = tokenStatusService.updateTokenStatus(tokenStatus);

        // Assertions here.
        Assert.assertNotNull(tokenStatusId);

    }

    @Test
    public void testUpdateTokenStatusIfIdNull() {
        try {
            DbTokenStatus tokenStatus = createDbTokenStatus();
            tokenStatus.setTokenStatusId(null);
            // method under test
            tokenStatusService.updateTokenStatus(tokenStatus);
            Assert.fail();
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.TOKEN_STATUS_ID_IS_NULL.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }

    }

    @Test
    public void testUpdateTokenStatusIfInputNull() {
        try {
            // method under test
            tokenStatusService.updateTokenStatus(null);
            Assert.fail();
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.INVALID_TOKEN_STATUS.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }

    }

    @Test
    public void testUpdateTokenStatusIsException() {

        try {
            PowerMockito.when(dbTokenStatusRepository.exists(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            DbTokenStatus tokenStatus = createDbTokenStatus();
            // method under test
            tokenStatusService.updateTokenStatus(tokenStatus);
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
    public void testUpdateTokenStatusIsNotExist() {

        try {
            PowerMockito.when(dbTokenStatusRepository.exists(Mockito.any(String.class))).thenReturn(false);

            DbTokenStatus tokenStatus = createDbTokenStatus();
            // method under test
            tokenStatusService.updateTokenStatus(tokenStatus);
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

    //

    @Test
    public void testUpdateTokenStatusToInActive() {

        DbTokenStatus tokenStatus = createDbTokenStatus();
        PowerMockito.when(dbTokenStatusRepository.findByTokenValue(Mockito.any(String.class))).thenReturn(tokenStatus);

        PowerMockito.when(dbTokenStatusRepository.exists(Mockito.any(String.class))).thenReturn(true);

        PowerMockito.when(dbTokenStatusRepository.save(Mockito.any(DbTokenStatus.class))).thenReturn(tokenStatus);

        // method under test
        String tokenStatusId = tokenStatusService.updateTokenStatusToInActive("75675678");

        // Assertions here.
        Assert.assertNotNull(tokenStatusId);

    }

    @Test
    public void testUpdateTokenStatusToInActiveIfException() {
        try {
            PowerMockito.when(dbTokenStatusRepository.findByTokenValue(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            tokenStatusService.updateTokenStatusToInActive("756768");
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

    //
    @Test
    public void testSaveMutipleToken() {

        Map<Integer,String> docIdsMap = new HashMap<>();
        docIdsMap.put(3435, "5657678");

        DbTokenStatus tokenStatus = createDbTokenStatus();

        PowerMockito.when(dbTokenStatusRepository.insert(Mockito.any(DbTokenStatus.class))).thenReturn(tokenStatus);
        String token = "457uhyhjy78769";
        PowerMockito.when(iJwtAuthUtilService.issueJwt()).thenReturn(token);

        // method under test
        List<DocumentMetaDataTokenMapV1> tokenStatusId = tokenStatusService.createToken(docIdsMap);

        // Assertions here.
        Assert.assertNotNull(tokenStatusId);
        Assert.assertNotNull(tokenStatusId.get(0));

        assertThat(tokenStatusId.get(0).getDocumentId(), equalTo("5657678"));
        assertThat(tokenStatusId.get(0).getIndex(), equalTo(3435));
        assertThat(tokenStatusId.get(0).getToken(), equalTo(token));

    }

    @Test
    public void testSaveMutipleTokenIfInputNull() {

        Map<Integer,String> docIdsMap = null;

        // method under test
        List<DocumentMetaDataTokenMapV1> tokenStatusId = tokenStatusService.createToken(docIdsMap);

        // Assertions here.
        Assert.assertNull(tokenStatusId);
    }

    private DbTokenStatus createDbTokenStatus() {
        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setActive("T");
        dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
        dbTokenStatus.setTokenValue("456464576576");
        dbTokenStatus.setTokenStatusId("dbTokenStatus");
        return dbTokenStatus;

    }

}
