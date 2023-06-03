package com.celodev.asyncelasticpdf.PageDocument;

import com.celodev.asyncelasticpdf.File.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pdf/pages")
public class PageDocumentResource {

  private final PageDocumentService service;
  private final FileService fileService;

  @PostMapping
  public Mono<Void> index(@RequestPart("file") Mono<FilePart> filePartMono) {
    return filePartMono
      .flatMap(fileService::saveFile)
      .flatMap(service::indexPageDocuments)
      .then();
  }

  @GetMapping
  public void indexAllPageDocuments() throws IOException {
    service.indexAllPageDocuments().subscribe();
  }

  @DeleteMapping
  public Mono<Void> deleteAll() {
    return service.deleteAll().then();
  }
}
