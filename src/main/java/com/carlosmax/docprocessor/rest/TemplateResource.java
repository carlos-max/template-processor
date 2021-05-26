package com.carlosmax.docprocessor.rest;

import com.carlosmax.docprocessor.rest.dto.TemplateVariable;
import com.carlosmax.docprocessor.service.TemplateService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/template")
public class TemplateResource {
    Logger log = LoggerFactory.getLogger(TemplateResource.class);

    @Autowired
    private TemplateService templateService;

    @ApiOperation(value="Create a new template", notes="Create a new template by uploading a .docx file")
    @PostMapping("/create{templateName}")
    public ResponseEntity<Void> createTemplate(@PathVariable String templateName, @RequestParam("modelFile") MultipartFile modelFile) throws IOException, InvalidFormatException, SQLException {
        templateService.createTemplate(templateName, modelFile);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ResponseBody
    @ApiOperation(value="Get a saved template", notes="Get a template by name, passing the desired variables on the body")
    @PostMapping("/print/{templateName}")
    public ResponseEntity<byte[]> printTemplate(@PathVariable String templateName, @RequestBody List<TemplateVariable> templateVariables) throws Exception {
        if(templateName == null) {
            throw new Exception("templateName can't be null!!");
        }

        templateName = templateName.trim().toLowerCase().replaceAll("\\s","");

        byte[] template = templateService.getTemplate(templateName, templateVariables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Content-Disposition", "attachment; filename=".concat(templateName).concat(".docx"));
        return new ResponseEntity<>(template, headers, HttpStatus.OK);
    }

}
