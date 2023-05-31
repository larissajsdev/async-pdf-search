package com.celodev.asyncelasticpdf.Search;

import com.celodev.asyncelasticpdf.pdf.PdfDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {

  private String id;
  private List<String> highlights;

  public SearchResponse(SearchHit<PdfDocument> searchHit) {
    this.id = searchHit.getId();
    this.highlights = searchHit.getHighlightFields().get("pageDocuments.content");
  }
}
