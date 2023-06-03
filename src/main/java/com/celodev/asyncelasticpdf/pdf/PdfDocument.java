package com.celodev.asyncelasticpdf.pdf;


import com.celodev.asyncelasticpdf.PageDocument.PageDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "e-index-pdf")
public class PdfDocument {
  private String id;
  private String title;

  @Field(type = FieldType.Nested, includeInParent = true)
  private List<PageDocument> pageDocuments;

  public PdfDocument(List<PageDocument> pageDocuments) {
    this.pageDocuments = pageDocuments;
  }

  public PdfDocument(String filename) {
    this.title = filename;
  }

}