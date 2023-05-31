package com.celodev.asyncelasticpdf.PageDocument;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Page {
  int pageNumber;
  String content;
}
