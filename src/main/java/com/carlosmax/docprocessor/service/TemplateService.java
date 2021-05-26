package com.carlosmax.docprocessor.service;


import com.carlosmax.docprocessor.rest.dto.TemplateVariable;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface TemplateService {

    void createTemplate(String templateName, MultipartFile modelFile) throws IOException, InvalidFormatException, SQLException;

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    byte[] getTemplate(String templateName, List<TemplateVariable> templateVariables) throws Exception;
}
