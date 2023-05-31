package com.celodev.asyncelasticpdf.PageDocument;

import com.celodev.asyncelasticpdf.File.FileService;
import com.celodev.asyncelasticpdf.pdf.PdfReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PageDocumentService {

  private final PageDocumentRepository repository;
  private final PdfReaderService pdfReaderService;
  private final FileService fileService;


  public Mono<Void> indexPageDocuments(Mono<FilePart> filePartMono) {
    return filePartMono.flatMap(fileService::saveFile)
      .flatMapMany(this::toPageDocuments)
      .flatMap(repository::save)
      .subscribeOn(Schedulers.boundedElastic())
      .then();
  }

  private Flux<PageDocument> toPageDocuments(File file) {
    return Mono.fromCallable(() -> file)
      .flatMap(pdfReaderService::toPDFReader)
      .flatMapMany(pdfReaderService::processPdfPages)
      .map(PageDocument::new)
      .subscribeOn(Schedulers.boundedElastic())
      ;
  }

  public Mono<Void> indexAllPageDocuments() throws IOException {
    return Flux.fromStream(fileService.loadAll())
      .flatMap(this::toPageDocuments)
      .buffer(50)
      .flatMap(repository::saveAll)
      .then();
  }

  public Mono<Void> deleteAll() {
    return repository.deleteAll().then();
  }
}
