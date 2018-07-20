package com.daimler.duke.document.dto;

import java.io.Serializable;

/**
 * @author SANDGUP.
 *
 */
public class UniqueId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5652569221879584852L;
  /**
   * 
   */
  private String            root;
  /**
   * 
   */
  private String            extension;

  /**
   * @param inputRoot.
   * @param inputExtension
   */
  public UniqueId(final String inputRoot, final String inputExtension) {
    super();
    this.root = inputRoot;
    this.extension = inputExtension;
  }

  /**
   * default constructor.
   */
  public UniqueId() {
    // default constructor
  }

  /**
   * 
   * @return
   */
  public String getRoot() {
    return root;
  }

  /**
   * 
   * @param input
   */
  public void setRoot(final String input) {
    this.root = input;
  }

  /**
   * 
   * @return
   */
  public String getExtension() {
    return extension;
  }

  /**
   * 
   * @param input
   */
  public void setExtension(final String input) {
    this.extension = input;
  }

}
