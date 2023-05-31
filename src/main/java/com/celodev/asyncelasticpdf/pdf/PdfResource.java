package com.celodev.asyncelasticpdf.pdf;

import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;


@RestController
@RequestMapping("api/v1/pdf")
@RequiredArgsConstructor
public class PdfResource {

  private final PdfService service;

  @PostMapping
  public Mono<Void> index(@RequestPart("file") Mono<FilePart> filePartMono) {
    return service.indexPdfPages(filePartMono);
  }

  @GetMapping
  public Mono<Void> indexAll() throws IOException {
    return service.indexAllPdfDocuments();
  }
}
