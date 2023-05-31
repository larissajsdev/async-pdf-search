package com.celodev.asyncelasticpdf.PageDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "e2-index-pdf")
public class PageDocument {
    @Id
    private String id;
    private int pageNumber;
    private String content;


    public PageDocument(Page page) {
        this.pageNumber = page.getPageNumber();
        this.content = page.getContent();
    }
}
