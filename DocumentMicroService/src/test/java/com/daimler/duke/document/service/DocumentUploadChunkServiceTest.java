package com.daimler.duke.document.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;

import java.io.InputStream;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbDocumentContent;
import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.DocumentChunkTransferredStatus;
import com.daimler.duke.document.dto.DocumentChunkV1;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.repository.DbDocumentMetaDataRepository;
import com.daimler.duke.document.util.MockObjectTest;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DocumentService.class, GridFSDBFile.class, GridFSFile.class })
public class DocumentUploadChunkServiceTest {
	private static String uploadCunkSize = "4";

	private GridFsTemplate gridFsTemplate;

	private GridFSFile gridFSFile;

	private DocumentUploadChunkService documentUploadChunkService;

	private DbDocumentMetaDataRepository dbDocumentMetaDataRepository;

	private DocumentService documentService;

	private MongoTemplate mongoTemplate;
	private TokenStatusService tokenStatusService;

	@Before
	public void init() throws Exception {
		mongoTemplate = PowerMockito.mock(MongoTemplate.class);
		gridFsTemplate = PowerMockito.mock(GridFsTemplate.class);
		gridFSFile = PowerMockito.mock(GridFSFile.class);
		documentService = PowerMockito.mock(DocumentService.class);
		documentUploadChunkService = new DocumentUploadChunkService();
		dbDocumentMetaDataRepository = PowerMockito.mock(DbDocumentMetaDataRepository.class);
		Whitebox.setInternalState(documentUploadChunkService, "gridFsTemplate", gridFsTemplate);
		Whitebox.setInternalState(documentUploadChunkService, "documentService", documentService);
		Whitebox.setInternalState(documentUploadChunkService, "dbDocumentMetaDataRepository",
				dbDocumentMetaDataRepository);
		Whitebox.setInternalState(documentUploadChunkService, "mongoTemplate", mongoTemplate);

		tokenStatusService = PowerMockito.mock(TokenStatusService.class);
		Whitebox.setInternalState(documentUploadChunkService, "tokenStatusService", tokenStatusService);

	}

	@Test
	public void testSetUploadChunkSizeForNewRecord() {
		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(null);
		DBCollection collection = PowerMockito.mock(DBCollection.class);
		DBCursor cursor = PowerMockito.mock(DBCursor.class);
		PowerMockito.when(mongoTemplate.getCollection(Mockito.any(String.class))).thenReturn(collection);
		PowerMockito.when(collection.find()).thenReturn(cursor);
		PowerMockito.when(cursor.hasNext()).thenReturn(false);

		documentUploadChunkService.saveUploadChunkSize(uploadCunkSize);

	}

	@Test
	public void testSetUploadChunkSizeForNonNumeric() {
		try {
			documentUploadChunkService.saveUploadChunkSize("");
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getMessage(), "Chunk size is null or non-numberic");
		}
	}

	@Test
	public void testSetUploadChunkSizeForNull() {
		try {
			documentUploadChunkService.saveUploadChunkSize(null);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getMessage(), "Chunk size is null or non-numberic");
		}
	}

	@Test
	public void testSetUploadChunkSizeForOldRecord() {
		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(createDocumentMataData());
		DBCollection collection = PowerMockito.mock(DBCollection.class);
		DBCursor cursor = PowerMockito.mock(DBCursor.class);
		PowerMockito.when(mongoTemplate.getCollection(Mockito.any(String.class))).thenReturn(collection);
		PowerMockito.when(collection.find()).thenReturn(cursor);
		PowerMockito.when(cursor.hasNext()).thenReturn(false);

		documentUploadChunkService.saveUploadChunkSize(uploadCunkSize);

	}

	@Test
	public void testSetUploadChunkSizeForModifyOldRecord() throws Exception {
		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(createDocumentMataData());
		DBCollection collection = PowerMockito.mock(DBCollection.class);
		DBCursor cursor = PowerMockito.mock(DBCursor.class);
		PowerMockito.when(mongoTemplate.getCollection(Mockito.any(String.class))).thenReturn(collection);
		PowerMockito.when(collection.find()).thenReturn(cursor);
		PowerMockito.when(cursor.hasNext()).thenReturn(false);

		/*
		 * DBObject obj = PowerMockito.mock(DBObject.class, RETURNS_DEEP_STUBS);
		 * PowerMockito.when(cursor.next()).thenReturn(obj); String abc =
		 * PowerMockito.mock(String.class);
		 * PowerMockito.when(obj.get(abc).toString()).thenReturn(abc); ObjectId objId =
		 * PowerMockito.mock(ObjectId.class); PowerMockito.whenNew(ObjectId.class)
		 * .withArguments(Mockito.anyString()) .thenReturn(objId);
		 * PowerMockito.when(obj.put(abc, new ObjectId("abc"))).thenReturn(abc);
		 */
		// PowerMockito.doNothing().when(obj).put(abc, objId));
		WriteResult result = PowerMockito.mock(WriteResult.class);

		documentUploadChunkService.saveUploadChunkSize(uploadCunkSize);

	}

	@Test
	public void testgetUploadChunkSize() {

		PowerMockito.when(mongoTemplate.findOne(Mockito.any(Query.class), anyObject()))
				.thenReturn(createDbUploadFileChunkSize());

		documentUploadChunkService.getUploadChunkSize();

	}

	@Test
	public void getChunkIdListTest() {

		List<GridFSDBFile> files = getDummyFileList();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		DocumentChunkTransferredStatus ids = documentUploadChunkService.getChunkIdList("5a97e24c7a8f8023b8137ade");

		assertEquals("5a97e24c7a8f8023b8137ade", ids.getDocumentMetaDataId());

		assertEquals(new Long(2), (Long) ids.getChunkIds().get(0));
		assertEquals(new Long(1), (Long) ids.getChunkIds().get(1));
		assertEquals(new Long(3), (Long) ids.getChunkIds().get(2));
	}

	@Test
	public void saveChunkTest() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("2");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("3");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(fileChunkSize());

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);
		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);
		DbTokenStatus dbTokenStatus = new DbTokenStatus();
		dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
		PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
				.thenReturn(dbTokenStatus);
		PowerMockito.when(mongoTemplate.findOne(Mockito.any(Query.class), anyObject()))
				.thenReturn(createDbUploadFileChunkSize());
		boolean status = documentUploadChunkService.saveChunk(documentChunk, "tokenFor5a223f3e5953c7136439beb5");
		assertEquals(true, status);

	}

	@Test
	public void saveChunkTestNullChunkId() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId(null);
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("3");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestNullData() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent(null);
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("3");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestInvalidData() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("s");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("3");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestNullMetaDataId() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId(null);
		documentChunk.setTotalNumOfChunks("3");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);
		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestNullNumberOfChunks() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks(null);

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestInvalidNumberOfChunks() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("jfchgfhg");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestInvalidChunkId() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1www");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("4");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestInvalidMetaData() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("4");

		DbDocumentMetaData documentMetaData = null;

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(fileChunkSize());

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestWithDiffrentTotalNumberOfChunks() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("4");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		List<GridFSDBFile> files2 = new ArrayList<>();
		// GridFSDBFile gridFSDBFile = new GridFSDBFile();
		// gridFSDBFile.setMetaData(new BasicDBObject());
		// files.add(gridFSDBFile);

		GridFSDBFile gridFSDBFile2 = new GridFSDBFile();
		BasicDBObject metaData = new BasicDBObject();
		metaData.put(Constants.TOTAL_NUM_OF_CHUNKS, 2L);
		gridFSDBFile2.setMetaData(metaData);
		files2.add(gridFSDBFile2);

		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files).thenReturn(files2);

		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(fileChunkSize());

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestWithDuplicateChunkId() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("4");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		List<GridFSDBFile> files2 = new ArrayList<>();
		GridFSDBFile gridFSDBFile = new GridFSDBFile();
		gridFSDBFile.setMetaData(new BasicDBObject());
		files2.add(gridFSDBFile);
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files2).thenReturn(files2);

		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(fileChunkSize());

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testSaveChunksMoreThanTotalNumberOfChunks() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("2");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		List<GridFSDBFile> files2 = new ArrayList<>();
		// GridFSDBFile gridFSDBFile = new GridFSDBFile();
		// gridFSDBFile.setMetaData(new BasicDBObject());
		// files.add(gridFSDBFile);

		GridFSDBFile gridFSDBFile2 = new GridFSDBFile();
		BasicDBObject metaData = new BasicDBObject();
		metaData.put(Constants.TOTAL_NUM_OF_CHUNKS, 2L);
		gridFSDBFile2.setMetaData(metaData);
		files2.add(gridFSDBFile2);
		files2.add(gridFSDBFile2);

		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files).thenReturn(files2);

		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(fileChunkSize());

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestValidationForIsAlreadyUploadComplete() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("4");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(new GridFSDBFile());

		PowerMockito.when(mongoTemplate.findAll(DbUploadFileChunkSize.class)).thenReturn(fileChunkSize());

		DbTokenStatus dbTokenStatus = new DbTokenStatus();
		dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
		PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
				.thenReturn(dbTokenStatus);
		PowerMockito.when(mongoTemplate.findOne(Mockito.any(Query.class), anyObject()))
				.thenReturn(createDbUploadFileChunkSize());
		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (DocumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void saveChunkTestAlreadyExistingChunk() {

		DocumentChunkV1 documentChunk = new DocumentChunkV1();

		documentChunk.setChunkId("1");
		documentChunk.setContent("String");
		documentChunk.setDocumentMetaDataId("5a97e24c7a8f8023b8137ade");
		documentChunk.setTotalNumOfChunks("4");

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		GridFSFile gridFSFile = new GridFSDBFile();
		PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class),
				Mockito.any(DBObject.class))).thenReturn(gridFSFile);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);

		List<GridFSDBFile> files = new ArrayList<>();
		files.add(new GridFSDBFile());
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			boolean status = documentUploadChunkService.saveChunk(documentChunk, "");
			assertTrue(false);

		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testMergeChunksInvalidMetadata() {

		DbDocumentMetaData documentMetaData = null;

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);
		try {

			documentUploadChunkService.mergeChunks("5a97e24c7a8f8023b8137ade");
			assertTrue(false);
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testMergeChunksAlreadyMergedDocument() {

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);
		PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(new GridFSDBFile());

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);
		try {

			documentUploadChunkService.mergeChunks(documentMetaData.getDocumentId());
			assertTrue(false);
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testMergeChunksWithoutUploadingAnyChunks() {

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);
		List<GridFSDBFile> files = new ArrayList<>();

		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			documentUploadChunkService.mergeChunks(documentMetaData.getDocumentId());
			assertTrue(false);
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testMergeChunksWithoutUploadingAllChunks() {

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);
		List<GridFSDBFile> files = new ArrayList<>();

		GridFSDBFile gridFSDBFile = new GridFSDBFile();
		DBObject metaData = new BasicDBObject();
		metaData.put(Constants.TOTAL_NUM_OF_CHUNKS, 4L);
		gridFSDBFile.setMetaData(metaData);
		files.add(gridFSDBFile);
		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		try {

			documentUploadChunkService.mergeChunks(documentMetaData.getDocumentId());
			assertTrue(false);
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testMergeChunks() {

		DbDocumentMetaData documentMetaData = MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade",
				"name", "null", 2515L);

		PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(documentMetaData);
		List<GridFSDBFile> files = new ArrayList<>();

		GridFSDBFile gridFSDBFile = new GridFSDBFile();
		DBObject metaData = new BasicDBObject();
		metaData.put(Constants.TOTAL_NUM_OF_CHUNKS, 1L);
		gridFSDBFile.setMetaData(metaData);
		files.add(gridFSDBFile);

		PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(files);

		PowerMockito.when(documentService.createDocumentContent(Mockito.any(DbDocumentContent.class))).thenReturn("km");
		PowerMockito.when(dbDocumentMetaDataRepository.save(Mockito.any(DbDocumentMetaData.class)))
				.thenReturn(documentMetaData);

		try {

			documentUploadChunkService.mergeChunks(documentMetaData.getDocumentId());
			assertTrue(true);
		} catch (ValidationException e) {
			assertTrue(false);
		}

	}

	@Test
	public void tesCancelFileUpload() {
		/*
		 * List<GridFSDBFile> files = getDummyFileList(); DbDocumentMetaData
		 * documentMetaData =
		 * MockObjectTest.createDbDocumentMetaData("5a97e24c7a8f8023b8137ade", "name",
		 * "null", 2515L);
		 * PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(
		 * String.class))) .thenReturn(documentMetaData);
		 * PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class)))
		 * .thenReturn(files);
		 * PowerMockito.when(gridFsTemplate.delete(Mockito.any(Query.class)))
		 * .thenReturn(null); documentUploadChunkService.cancelFileUpload(
		 * "5a97ce4ebe6b084804f57a8e");
		 */

	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	private DbDocumentContent getDummyDbDocumentContent() {
		DbDocumentContent dbDocumentContent = new DbDocumentContent();
		dbDocumentContent.setData(hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d"));
		dbDocumentContent.setDocumentMtDtdId("ewewweew");
		return dbDocumentContent;

	}

	private List<GridFSDBFile> getDummyFileList() {
		List<GridFSDBFile> files = new ArrayList<GridFSDBFile>();

		GridFSDBFile file = new GridFSDBFile();
		DBObject metaData = new BasicDBObject();
		metaData.put(Constants.CHUNK_ID, 2L);
		metaData.put(Constants.TOTAL_NUM_OF_CHUNKS, 3L);
		metaData.put(Constants.DOCUMENT_META_DATA_ID, "5a97e24c7a8f8023b8137ade");
		file.setMetaData(metaData);

		files.add(file);

		GridFSDBFile file2 = new GridFSDBFile();
		DBObject metaData2 = new BasicDBObject();
		metaData2.put(Constants.CHUNK_ID, 1L);
		metaData2.put(Constants.TOTAL_NUM_OF_CHUNKS, 3L);
		metaData2.put(Constants.DOCUMENT_META_DATA_ID, "5a97e24c7a8f8023b8137ade");
		file2.setMetaData(metaData2);

		files.add(file2);

		GridFSDBFile file3 = new GridFSDBFile();
		DBObject metaData3 = new BasicDBObject();
		metaData3.put(Constants.CHUNK_ID, 3L);
		metaData3.put(Constants.TOTAL_NUM_OF_CHUNKS, 3L);
		metaData3.put(Constants.DOCUMENT_META_DATA_ID, "5a97e24c7a8f8023b8137ade");
		file3.setMetaData(metaData3);

		files.add(file3);

		return files;

	}

	private List<DbUploadFileChunkSize> fileChunkSize() {
		DbUploadFileChunkSize document = new DbUploadFileChunkSize();
		document.setUploadCunkSize("4");
		List<DbUploadFileChunkSize> list = new ArrayList<DbUploadFileChunkSize>();
		list.add(document);
		return list;
	}

	private DbUploadFileChunkSize createDbUploadFileChunkSize() {
		DbUploadFileChunkSize document = new DbUploadFileChunkSize();
		document.setUploadCunkSize(uploadCunkSize);

		return document;
	}

	private List<DbUploadFileChunkSize> createDocumentMataData() {
		DbUploadFileChunkSize document = new DbUploadFileChunkSize();
		document.setUploadCunkSize(uploadCunkSize);
		List<DbUploadFileChunkSize> list = new ArrayList<DbUploadFileChunkSize>();
		list.add(document);
		return list;
	}
}
