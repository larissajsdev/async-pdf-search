package com.celodev.asyncelasticpdf.Search;

import com.celodev.asyncelasticpdf.PageDocument.PageDocument;
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
  private int page;
  private List<String> highlights;

  public SearchResponse(SearchHit<PageDocument> searchHit) {
    this.id = searchHit.getId();
    this.page = searchHit.getContent().getPage();
    this.highlights = searchHit.getHighlightFields().get("content");
  }


}
