package com.daimler.duke.document.util;

import com.daimler.duke.document.db.entity.DbApplicationIdentifier;
import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.dto.CreateMetaDataRequestV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentMetaDataWithSerialNoV1;
import com.daimler.duke.document.dto.DocumentV1;

public class MockObjectTest {

    public static DocumentMetaDataV1 createDocumentMataData(String documentId, String name, String type, Long size) {
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

    public static DocumentMetaDataV1 createDocumentMataDataForNullId() {
        DocumentMetaDataV1 documentMetaData = new DocumentMetaDataV1();
        documentMetaData.setDocumentId(null);
        documentMetaData.setDocumentName("Sample Doc");
        documentMetaData.setDocumentType("pdf");
        documentMetaData.setComment("test");
        documentMetaData.setFirstName("Andreas");
        documentMetaData.setLastName("Gollmann");
        documentMetaData.setDepartment("ITP/DT");
        documentMetaData.setShortId("AGOLLMAN");
        return documentMetaData;
    }

    public static DocumentMetaDataV1 createDocumentMataDataForRandomId() {
        DocumentMetaDataV1 documentMetaData = new DocumentMetaDataV1();
        documentMetaData.setDocumentId("12132222");
        documentMetaData.setDocumentName("Sample Doc");
        documentMetaData.setDocumentType("pdf");
        documentMetaData.setComment("test");
        documentMetaData.setFirstName("Andreas");
        documentMetaData.setLastName("Gollmann");
        documentMetaData.setDepartment("ITP/DT");
        documentMetaData.setShortId("AGOLLMAN");
        return documentMetaData;
    }

    public static DbDocumentMetaData createDbDocumentMetaData(String documentId, String name, String type, Long size) {
        DbDocumentMetaData dbDocumentMetaData = new DbDocumentMetaData();
        dbDocumentMetaData.setDocumentId(documentId);
        dbDocumentMetaData.setDocumentName(name);
        dbDocumentMetaData.setDocumentype(type);
        dbDocumentMetaData.setComment("test");
        dbDocumentMetaData.setFirstName("Andreas");
        dbDocumentMetaData.setLastName("Gollmann");
        dbDocumentMetaData.setDepartment("ITP/DT");
        dbDocumentMetaData.setShortId("AGOLLMAN");
        dbDocumentMetaData.setApplicationId("5a223f3e5953c7136439beb5");
        return dbDocumentMetaData;
    }

    public static DbApplicationIdentifier createDbApplicationIdentifier() {
        DbApplicationIdentifier dbApplicationIdentifier = new DbApplicationIdentifier();
        dbApplicationIdentifier.setApplicationId("5a223f3e5953c7136439beb5");
        dbApplicationIdentifier.setApplicationName("Duke");
        dbApplicationIdentifier.setSecretCode("12");
        dbApplicationIdentifier.setSecretKey("DukeApplication");
        dbApplicationIdentifier.setFirstName("Andreas");
        dbApplicationIdentifier.setLastName("Gollmann");
        dbApplicationIdentifier.setDepartment("ITP/DT");

        return dbApplicationIdentifier;
    }

    public static DocumentContentV1 createDocumentContent() {
        DocumentContentV1 documentContent = new DocumentContentV1();
        documentContent.setData("tyy6yu657687689789870909089090");
        return documentContent;
    }

    public static DocumentV1 createDocument() {
        DocumentV1 document = new DocumentV1();
        document.setDocumentContent(createDocumentContent());
        String documentId = "5a223f3e5953c7136439beb5";
        document.setDocumentMetaData(createDocumentMataData(documentId, "TerstName", "pdf", 1l));

        return document;
    }

    public static DocumentMetaDataWithSerialNoV1 createDocumentMetaDataWithSerialNo(String documentId,
                                                                                    String name,
                                                                                    String type,
                                                                                    Long size) {
        DocumentMetaDataWithSerialNoV1 documentMetaDataWithSerialNo = new DocumentMetaDataWithSerialNoV1();

        CreateMetaDataRequestV1 documentMetaData = new CreateMetaDataRequestV1();
        documentMetaData.setDocumentName(name);
        documentMetaData.setDocumentType(type);
        documentMetaData.setComment("test");
        documentMetaData.setFirstName("Andreas");
        documentMetaData.setLastName("Gollmann");
        documentMetaData.setDepartment("ITP/DT");
        documentMetaData.setShortId("AGOLLMAN");

        documentMetaDataWithSerialNo.setDocumentMetaData(documentMetaData);
        documentMetaDataWithSerialNo.setSerialNo(0);

        return documentMetaDataWithSerialNo;
    }

    public static CreateMetaDataRequestV1 createMetaDataRequest(String documentId,
                                                                String name,
                                                                String type,
                                                                Long size) {

        CreateMetaDataRequestV1 documentMetaData = new CreateMetaDataRequestV1();
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
