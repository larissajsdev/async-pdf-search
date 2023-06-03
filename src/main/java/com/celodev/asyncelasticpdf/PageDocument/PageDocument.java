package com.celodev.asyncelasticpdf.PageDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "e-index-pdf")
public class PageDocument {

  @Id
  private String id;

  @Field(type = FieldType.Integer)
  private int page;

  @Field(type = FieldType.Date)
  private LocalDate publishDate;

  @Field(type = FieldType.Text)
  private String type;

  @Field(type = FieldType.Text, analyzer = "brazilian")
  private String content;

  public PageDocument(int pageNum, LocalDate publishDate, String type, String content) {
    this.page = pageNum;
    this.publishDate = publishDate;
    this.type = type;
    this.content = content;
  }

  public PageDocument(int pageNum, String content) {
    this.page = pageNum;
    this.content = content;
  }
}
