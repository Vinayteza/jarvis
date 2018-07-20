package com.daimler.duke.document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author RMAHAKU
 *
 */
@SpringBootApplication
@ServletComponentScan("com.daimler.duke.document.filter")
@ComponentScan("com.daimler.duke.*")
public class DocumentapptestApplication {
    /**
     * 
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(DocumentapptestApplication.class, args);
    }
}
