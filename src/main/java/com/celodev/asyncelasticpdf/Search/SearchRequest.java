package com.celodev.asyncelasticpdf.Search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
  private int page = 0;
  private int size = 10;
  private String query;
}
