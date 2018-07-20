package com.daimler.duke.document.db.entity;

import java.io.Serializable;
import com.daimler.duke.document.constant.BinaryDataEncoding;

/**
 * @author SANDGUP.
 *
 */
public class DbDocumentContent implements Serializable {

  /**
   * 
   */
  private static final long  serialVersionUID   = -803502876134040080L;
  /**
   * documentContentId.
   */

  private String             documentContentId;
  /**
   * documentMtDtdId.
   */
  private String             documentMtDtdId;
  /**
   * data.
   */
  private byte[]             data;

  /**
   * 
   * @return
   */
  public String getDocumentContentId() {
    return documentContentId;
  }

  /**
   * 
   * @param input
   */

  public void setDocumentContentId(final String input) {
    this.documentContentId = input;
  }

  /**
   * Get the documentMtDtdId.
   * 
   * @return documentMtDtdId
   */
  public String getDocumentMtDtdId() {
    return documentMtDtdId;
  }

  /**
   * Set the documentMtDtdId.
   * 
   * @param input the documentMtDtdId to set
   */
  public void setDocumentMtDtdId(final String input) {
    this.documentMtDtdId = input;
  }

  /**
   * Get the data.
   * 
   * @return data
   */
  public byte[] getData() {
    if (null != data) {
      return data.clone();
    } else
      return null;
  }

  /**
   * Set the data.
   * 
   * @param input the data to set
   */

  public void setData(final byte[] input) {
    if (null != input) {
      this.data = input.clone();
    } else {
      this.data = null;
    }
  }
}
