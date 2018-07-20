package com.daimler.duke.document.service;


import java.util.List;

import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.DocumentChunkTransferredStatus;
import com.daimler.duke.document.dto.DocumentChunkV1;



public interface IDocumentUploadChunkService {


    /**
     * It gets chunk size from the server where it is already set.
     * @return
     */
    public DbUploadFileChunkSize getUploadChunkSize();
    /**
     * It sets chunk size on server with client application will bulk file upload.
     * Constants.FETCH_KEY is just a key in custom entity.
     * Constants.FETCH_VALUE is just a value for key.
     * @param chunkSize
     * @return
     */
    public DbUploadFileChunkSize saveUploadChunkSize(String chunkSize);
    
	/** To  Save a single chunk
	 * @param documentChunkV1
	 * @return status of save
	 */
	public boolean saveChunk(DocumentChunkV1 documentChunkV1, String token);

	/**
	 * To get the  uploaded chunk id's to the given metadataid
	 * @param metaDataId
	 * @return  the list of chunks already uploaded
	 */
	public DocumentChunkTransferredStatus getChunkIdList(String metaDataId);
    /**
     * To merge all chunks uploaded to the given metadataid
     * @param metaDataId
     * @return
     */
    boolean mergeChunks(String metaDataId);
    /**
     * 
     * @param metaDataId
     * @return
     */
    
    boolean cancelFileUpload(String metaDataId);
    
}
