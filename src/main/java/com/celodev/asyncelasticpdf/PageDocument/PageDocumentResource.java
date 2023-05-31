package com.celodev.asyncelasticpdf.PageDocument;

import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/index-pdf")
public class PageDocumentResource {

  private final PageDocumentService service;

  @PostMapping
  public Mono<Void> index(@RequestPart("file") Mono<FilePart> filePartMono) {
    return service.indexPageDocuments(filePartMono);
  }

  @GetMapping
  public void getAll() throws IOException {
    service.indexAllPageDocuments().subscribe();
  }

  @DeleteMapping
  public Mono<Void> deleteAll() {
    return service.deleteAll();
  }
}
