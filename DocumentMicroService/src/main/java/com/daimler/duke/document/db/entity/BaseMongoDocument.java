package com.daimler.duke.document.db.entity;

import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author SANDGUP.
 *
 */
public abstract class BaseMongoDocument {

  /**
   * ID String
   */
  public static final String ID = "documentId";

  /**
   * documentId
   */

  @Id
  @ApiModelProperty(notes = "The database generated product ID")
  private String             documentId;

  /**
   * Get the document Id.
   * 
   * @return <code>String</code> - document Id
   */

  public String getDocumentId() {
    return documentId;
  }

  /**
   * 
   * @param input
   */
  public void setDocumentId(final String input) {
    this.documentId = input;
  }

}
