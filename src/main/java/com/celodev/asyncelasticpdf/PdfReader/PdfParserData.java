package com.celodev.asyncelasticpdf.PdfReader;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfParserData {
    private PdfReader pdfReader;
    private PdfReaderContentParser pdfReaderContentParser;
}
