package com.carlosmax.docprocessor.service.impl;

import com.carlosmax.docprocessor.domain.*;
import com.carlosmax.docprocessor.domain.enumeration.HeaderFooterType;
import com.carlosmax.docprocessor.repository.TemplateRepository;
import com.carlosmax.docprocessor.rest.dto.TemplateVariable;
import com.carlosmax.docprocessor.service.TemplateService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createTemplate(String templateName, MultipartFile modelFile) throws IOException, InvalidFormatException, SQLException {
        InputStream fis = modelFile.getInputStream();
        XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));

        Template template = new Template();
        template.setUuid(UUID.randomUUID().toString());
        template.setName(templateName.trim().toLowerCase().replaceAll("\\s",""));
        template.setCreationDate(LocalDate.now());

        //Margins
        CTSectPr sectPr = xdoc.getDocument().getBody().getSectPr();
        CTPageMar pageMar = sectPr.getPgMar();
        template.setMarginTop(pageMar.getTop().intValue());
        template.setMarginBottom(pageMar.getBottom().intValue());
        template.setMarginLeft(pageMar.getLeft().intValue());
        template.setMarginRight(pageMar.getRight().intValue());

        //Watermark
        if(!xdoc.getHeaderFooterPolicy().getDefaultHeader().getAllPictures().isEmpty()) {
            Blob blob = new javax.sql.rowset.serial.SerialBlob(xdoc.getAllPictures().get(0).getData());
            template.setWatermarkBlob(blob);
        }

        //Template Paragraphs
        List<Paragraph> templateParagraphs = getParagraphs(xdoc.getParagraphs());
        if(!templateParagraphs.isEmpty()) {
            template.setParagraphList(templateParagraphs.stream().map(p -> {
                TemplateParagraph templateParagraph = new TemplateParagraph();
                templateParagraph.setTemplate(template);
                templateParagraph.setParagraph(p);
                return templateParagraph;
            }).collect(Collectors.toList()));
        }

        //Header
        setHeaderList(xdoc, template);
        //Footer
        setFooterList(xdoc, template);

        //Tables
        List<TableDoc> tables = new ArrayList<>();
        for(XWPFTable fileTable : xdoc.getTables()) {
            TableDoc tableDoc = new TableDoc();
            tableDoc.setTemplate(template);
            tableDoc.setCursorPos(xdoc.getParagraphs().get(22).getCTP().newCursor().toString());

            List<TableRow> rows = new ArrayList<>();
            int rowOrder = 0;
            for(XWPFTableRow fileTableRow : fileTable.getRows()) {
                TableRow tableRow = new TableRow();
                tableRow.setPrintOrder(rowOrder);
                tableRow.setTableDoc(tableDoc);

                List<TableCell> cells = new ArrayList<>();
                int cellOrder = 0;
                for(XWPFTableCell fileTableCell : fileTableRow.getTableCells()) {
                    TableCell tableCell = new TableCell();
                    tableCell.setPrintOrder(cellOrder);
                    tableCell.setTableRow(tableRow);
                    tableCell.setParagraph(getParagraphs(fileTableCell.getParagraphs()).get(0));
                    cells.add(tableCell);
                    cellOrder++;
                }
                tableRow.setCells(cells);
                rows.add(tableRow);
                rowOrder++;
            }
            tableDoc.setRows(rows);
            tables.add(tableDoc);
        }
        template.setTables(tables);

        templateRepository.save(template);
    }

    private void setHeaderList(XWPFDocument xdoc, Template template) {
        List<Header> headerList = new ArrayList<>();

        if(!xdoc.getHeaderList().isEmpty() && xdoc.getHeaderFooterPolicy().getDefaultHeader() != null) {
            int headerIndex = 0;
            createHeader(xdoc.getHeaderFooterPolicy().getDefaultHeader(), HeaderFooterType.DEFAULT, template, headerList, headerIndex);
        }
        if(!xdoc.getHeaderList().isEmpty() && xdoc.getHeaderFooterPolicy().getFirstPageHeader() != null) {
            int headerIndex = 1;
            createHeader(xdoc.getHeaderFooterPolicy().getFirstPageHeader(), HeaderFooterType.FIRST, template, headerList, headerIndex);
        }
        if(!xdoc.getHeaderList().isEmpty() && xdoc.getHeaderFooterPolicy().getEvenPageHeader() != null) {
            int headerIndex = 2;
            createHeader(xdoc.getHeaderFooterPolicy().getEvenPageHeader(), HeaderFooterType.EVEN, template, headerList, headerIndex);
        }

        template.setHeaderList(headerList);
    }

    private void createHeader(XWPFHeader fileHeader, HeaderFooterType type, Template template, List<Header> headerList, int headerIndex) {
        Header header = new Header();
        header.setPrintOrder(headerIndex);
        header.setTemplate(template);
        header.setType(type);
        //Header Paragraphs
        List<Paragraph> headerParagraphs = getParagraphs(fileHeader.getParagraphs());
        if(!headerParagraphs.isEmpty()) {
            header.setParagraphList(headerParagraphs.stream().map(p -> {
                HeaderParagraph headerParagraph = new HeaderParagraph();
                headerParagraph.setHeader(header);
                headerParagraph.setParagraph(p);
                return headerParagraph;
            }).collect(Collectors.toList()));
        }
        headerList.add(header);
    }

    private void setFooterList(XWPFDocument xdoc, Template template) {
        List<Footer> footerList = new ArrayList<>();

        if(!xdoc.getFooterList().isEmpty() && xdoc.getHeaderFooterPolicy().getDefaultFooter() != null) {
            int footerIndex = 0;
            createFooter(xdoc.getHeaderFooterPolicy().getDefaultFooter(), HeaderFooterType.DEFAULT, template, footerList, footerIndex);
        }
        if(!xdoc.getHeaderList().isEmpty() && xdoc.getHeaderFooterPolicy().getFirstPageFooter() != null) {
            int footerIndex = 1;
            createFooter(xdoc.getHeaderFooterPolicy().getFirstPageFooter(), HeaderFooterType.FIRST, template, footerList, footerIndex);
        }
        if(!xdoc.getHeaderList().isEmpty() && xdoc.getHeaderFooterPolicy().getEvenPageFooter() != null) {
            int footerIndex = 2;
            createFooter(xdoc.getHeaderFooterPolicy().getEvenPageFooter(), HeaderFooterType.EVEN, template, footerList, footerIndex);
        }

        template.setFooterList(footerList);
    }

    private void createFooter(XWPFFooter fileFooter, HeaderFooterType type, Template template, List<Footer> footerList, int footerIndex) {
        Footer footer = new Footer();
        footer.setPrintOrder(footerIndex);
        footer.setTemplate(template);
        footer.setType(type);
        //Footer Paragraphs
        List<Paragraph> footerParagraphs = getParagraphs(fileFooter.getParagraphs());
        if(!footerParagraphs.isEmpty()) {
            footer.setParagraphList(footerParagraphs.stream().map(p -> {
                FooterParagraph footerParagraph = new FooterParagraph();
                footerParagraph.setFooter(footer);
                footerParagraph.setParagraph(p);
                return footerParagraph;
            }).collect(Collectors.toList()));
        }
        footerList.add(footer);
    }

    private List<Paragraph> getParagraphs(List<XWPFParagraph> fileParagraphs) {
        List<Paragraph> paragraphList = new ArrayList<>();

        int pIndex = 0;
        for (XWPFParagraph fileParagraph : fileParagraphs) {
            Paragraph paragraph = new Paragraph();
            paragraph.setPrintOrder(pIndex);
            paragraph.setAlignment(fileParagraph.getAlignment().toString());
            paragraph.setBorderBetween(fileParagraph.getBorderBetween().toString());
            paragraph.setBorderTop(fileParagraph.getBorderTop().toString());
            paragraph.setBorderBottom(fileParagraph.getBorderBottom().toString());
            paragraph.setBorderLeft(fileParagraph.getBorderLeft().toString());
            paragraph.setBorderRight(fileParagraph.getBorderRight().toString());
            paragraph.setSpacingLineRule(fileParagraph.getSpacingLineRule().toString());
            paragraph.setSpacingBetween(fileParagraph.getSpacingBetween());
            paragraph.setSpacingBefore(fileParagraph.getSpacingBefore());
            paragraph.setSpacingBeforeLines(fileParagraph.getSpacingBeforeLines());
            paragraph.setSpacingAfter(fileParagraph.getSpacingAfter());
            paragraph.setSpacingAfterLines(fileParagraph.getSpacingAfterLines());
            paragraph.setVerticalAlignment(fileParagraph.getVerticalAlignment().toString());

            List<Content> contentList = getContents(fileParagraph, paragraph);
            paragraph.setContents(contentList);
            paragraphList.add(paragraph);
            pIndex++;
        }

        return paragraphList;
    }

    private List<Content> getContents(XWPFParagraph fileParagraph, Paragraph paragraph) {
        List<Content> contentList = new ArrayList<>();
        int cIndex = 0;
        Content varContent = null;
        for (XWPFRun rn : fileParagraph.getRuns()) {
            Content content = new Content();
            if(varContent == null) {
                content.setPrintOrder(cIndex);
                content.setFontName(rn.getFontName());
                content.setFontSize(rn.getFontSize() == -1 ? 12 : rn.getFontSize());
                content.setColor(rn.getColor());
                content.setBold(rn.isBold());
                content.setUnderline(rn.getUnderline() != null ? rn.getUnderline().toString() : "NONE");
                content.setCapitalized(rn.isCapitalized());
                content.setStrikeThrough(rn.isStrikeThrough());
                content.setItalic(rn.isItalic());
                content.setVerticalAlignment(rn.getVerticalAlignment().toString());
                content.setValue(rn.toString());

                //Check variable
                if(rn.toString().equals("[")) {
                    varContent = content;
                    continue;
                }else if(rn.toString().contains("[") && rn.toString().contains("]")) {
                    String[] vars = rn.toString().split("]");
                    for(int i = 0; i < vars.length; i++) {
                        Content contentVar = new Content();
                        contentVar.setPrintOrder(cIndex);
                        contentVar.setFontName(content.getFontName());
                        contentVar.setFontSize(content.getFontSize());
                        contentVar.setColor(content.getColor());
                        contentVar.setBold(content.isBold());
                        contentVar.setUnderline(content.getUnderline());
                        contentVar.setCapitalized(rn.isCapitalized());
                        contentVar.setStrikeThrough(content.isStrikeThrough());
                        contentVar.setItalic(content.isItalic());
                        contentVar.setVerticalAlignment(content.getVerticalAlignment());
                        contentVar.setValue(vars[i].concat("]"));
                        contentVar.setParagraph(paragraph);
                        contentList.add(contentVar);
                        cIndex++;
                    }
                    continue;
                }
            }else if(rn.toString().equals("]")) {
                varContent.setValue(varContent.getValue().concat(rn.toString()));
                content = varContent;
                varContent = null;
            }else {
                varContent.setValue(varContent.getValue().concat(rn.toString()));
                continue;
            }

            //Image
            if(!rn.getEmbeddedPictures().isEmpty()) {
                Long imageWidth = rn.getEmbeddedPictures().get(0).getCTPicture().getSpPr().getXfrm().getExt().getCx();
                Long imageHeight = rn.getEmbeddedPictures().get(0).getCTPicture().getSpPr().getXfrm().getExt().getCy();
                try {
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(rn.getEmbeddedPictures().get(0).getPictureData().getData());

                    content.setImageValue(blob);
                    content.setImageWidthEmu(imageWidth);
                    content.setImageHeightEmu(imageHeight);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            content.setParagraph(paragraph);
            contentList.add(content);
            cIndex++;
        }
        return contentList;
    }

    @Override
    @Transactional
    public byte[] getTemplate(String templateName, List<TemplateVariable> templateVariables) throws Exception {
        Template template = templateRepository.getByName(templateName);

        if(template == null) {
            throw new Exception("Template not found!");
        }

        XWPFDocument xdoc = new XWPFDocument();

        //Margins
        CTSectPr sectPr = xdoc.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(template.getMarginLeft()));
        pageMar.setTop(BigInteger.valueOf(template.getMarginTop()));
        pageMar.setRight(BigInteger.valueOf(template.getMarginRight()));
        pageMar.setBottom(BigInteger.valueOf(template.getMarginBottom()));

        //Watermark
        if(template.getWatermarkBlob() != null) {
            Blob blob = template.getWatermarkBlob();
            XWPFHeader h = xdoc.createHeader(org.apache.poi.wp.usermodel.HeaderFooterType.DEFAULT);

            XWPFParagraph headerP = h.createParagraph();
            XWPFRun imgRun = headerP.createRun();
            //imgRun.addPicture(blob.getBinaryStream(), Document.PICTURE_TYPE_PNG, "watermark.png", Units.toEMU(200), Units.toEMU(200));
        }//

        //Template Paragraphs
        if(!template.getParagraphList().isEmpty()) {
            for(TemplateParagraph tp : template.getSortedParagraphs()) {
                Paragraph paragraph = tp.getParagraph();
                XWPFParagraph fileParagraph = xdoc.createParagraph();
                setFileParagraphs(fileParagraph, paragraph);

                //Paragraph Contents
                if(!paragraph.getContents().isEmpty()) {
                    for(Content content : paragraph.getSortedContents()) {
                        XWPFRun rn = fileParagraph.createRun();
                        setFileContent(rn, content, templateVariables);
                    }
                }
            }
        }

        //Header
        if(!template.getHeaderList().isEmpty()) {
            for(Header header : template.getSortedHeaders()) {
                XWPFHeader fileHeader = xdoc.createHeader(org.apache.poi.wp.usermodel.HeaderFooterType.valueOf(header.getType().name()));
                if(!header.getParagraphList().isEmpty()) {
                    for(HeaderParagraph hp : header.getSorterdParagraphs()) {
                        XWPFParagraph fileParagraph = fileHeader.createParagraph();
                        setFileParagraphs(fileParagraph, hp.getParagraph());

                        //Paragraph Contents
                        if(!hp.getParagraph().getContents().isEmpty()) {
                            for(Content content : hp.getParagraph().getSortedContents()) {
                                XWPFRun rn = fileParagraph.createRun();
                                setFileContent(rn, content, templateVariables);
                            }
                        }
                    }
                }
            }
        }

        //Footer
        if(!template.getFooterList().isEmpty()) {
            for(Footer footer : template.getSortedFooters()) {
                XWPFFooter fileFooter = xdoc.createFooter(org.apache.poi.wp.usermodel.HeaderFooterType.valueOf(footer.getType().name()));
                if(!footer.getParagraphList().isEmpty()) {
                    for(FooterParagraph fp : footer.getSortedParagraphs()) {
                        XWPFParagraph fileParagraph = fileFooter.createParagraph();
                        setFileParagraphs(fileParagraph, fp.getParagraph());

                        //Paragraph Contents
                        if(!fp.getParagraph().getContents().isEmpty()) {
                            for(Content content : fp.getParagraph().getSortedContents()) {
                                XWPFRun rn = fileParagraph.createRun();
                                setFileContent(rn, content, templateVariables);
                            }
                        }
                    }
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        xdoc.write(baos);
        return baos.toByteArray();
    }

    private void setFileParagraphs(XWPFParagraph fileParagraph, Paragraph paragraph) {
        fileParagraph.setAlignment(ParagraphAlignment.valueOf(paragraph.getAlignment()));
        fileParagraph.setVerticalAlignment(TextAlignment.valueOf(paragraph.getVerticalAlignment()));
        fileParagraph.setBorderBetween(Borders.valueOf(paragraph.getBorderBetween()));
        fileParagraph.setBorderTop(Borders.valueOf(paragraph.getBorderTop()));
        fileParagraph.setBorderBottom(Borders.valueOf(paragraph.getBorderBottom()));
        fileParagraph.setBorderLeft(Borders.valueOf(paragraph.getBorderLeft()));
        fileParagraph.setBorderRight(Borders.valueOf(paragraph.getBorderRight()));
        fileParagraph.setSpacingLineRule(LineSpacingRule.valueOf(paragraph.getSpacingLineRule()));

//        fileParagraph.setSpacingBetween(paragraph.getSpacingBetween());
        fileParagraph.setSpacingBefore(paragraph.getSpacingBefore());
//        fileParagraph.setSpacingBeforeLines(paragraph.getSpacingBeforeLines());
        fileParagraph.setSpacingAfter(paragraph.getSpacingAfter());
//        fileParagraph.setSpacingAfterLines(paragraph.getSpacingAfterLines());
    }

    private void setFileContent(XWPFRun rn, Content content, List<TemplateVariable> templateVariables) {
        rn.setFontSize(content.getFontSize());
        if (content.getFontName() != null) {
            rn.setFontFamily(content.getFontName());
        }
        if (content.getColor() != null) {
            rn.setColor(content.getColor());
        }
        rn.setBold(content.isBold());
        rn.setCapitalized(content.isCapitalized());
        rn.setStrikeThrough(content.isStrikeThrough());
        rn.setItalic(content.isItalic());
        rn.setUnderline(UnderlinePatterns.valueOf(content.getUnderline()));
        rn.setVerticalAlignment(content.getVerticalAlignment());

        Boolean isVariable = false;
        if(content.getValue() != null && !content.getValue().isEmpty()) {
            for(TemplateVariable var : templateVariables) {
                if(var.getName().equals(content.getValue().toUpperCase().trim())) {
                    rn.setText(var.getValue());
                    isVariable = true;
                    break;
                }
            }
        }
        if(!isVariable) {
            rn.setText(content.getValue());
            if(content.getValue().equals("\t")) {
                rn.getCTR().addNewTab();
            }else if(content.getValue().equals("\n")) {
                rn.getCTR().addNewBr();
            }else if(1 == 2) {
                rn.addBreak(BreakType.PAGE);
                rn.addCarriageReturn();
            }
        }

        //Image
        if(content.getImageValue() != null) {
            Blob blob = content.getImageValue();
            try {
                rn.addPicture(blob.getBinaryStream(), Document.PICTURE_TYPE_PNG, content.getParagraph().getPrintOrder()+".image.png", Integer.parseInt(content.getImageWidthEmu().toString()), Integer.parseInt(content.getImageHeightEmu().toString()));
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private byte[] readBlob(Blob blob) {
        byte[] value = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            InputStream in = blob.getBinaryStream();
            int n = 0;
            while (true)
            {
                try {
                    if (!((n=in.read(buf))>=0)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                baos.write(buf, 0, n);
            }
            in.close();
             value = baos.toByteArray();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
