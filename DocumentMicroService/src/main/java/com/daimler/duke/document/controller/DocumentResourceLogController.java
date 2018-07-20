package com.daimler.duke.document.controller;

import javax.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.util.RequestResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author SANDGUP..
 *
 */
@RestController
// @Api(value = "Document Log Control Resource", description = "Document Logging
// Operation")
public class DocumentResourceLogController {

  /**
   * logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentResourceLogController.class);

  /**
   * logRequest boolean value
   */
  private static boolean      logRequest;

  /**
   * @param toggle.
   * @return
   */
  @GetMapping(value = "updateRequestLoggerSwitch/{toggle}", produces = MediaType.APPLICATION_JSON_VALUE)
  public RestResponseObject setInputRequestLoggerSwitch(@PathParam("toggle") final String toggle) {
    if (toggle != null && toggle.equals("1")) {
      logRequest = true;
    } else {
      logRequest = false;
    }
    return RequestResponseUtil.convertToRestResponseObject(logRequest ? "ON" : "OFF");

  }

  /**
   * @param request.
   */
  public static void logJsonRequest(final Object request) {
    if (logRequest) {
      final ObjectMapper mapper = new ObjectMapper();
      try {
        LOGGER.error(mapper.writeValueAsString(request));
        // System.out.println(mapper.writeValueAsString(request));
      } catch (JsonProcessingException e) {
        LOGGER.error("", "Error in logging input request");
      }
    }
  }
}
