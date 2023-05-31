package com.celodev.asyncelasticpdf.pdf;

import com.celodev.asyncelasticpdf.File.FileService;
import com.celodev.asyncelasticpdf.PageDocument.PageDocument;
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
public class PdfService {

  private final PdfReaderService pdfReaderService;
  private final PdfDocumentRepository repository;
  private final FileService fileService;

  public Mono<Void> indexPdfPages(Mono<FilePart> filePartMono) {
    return filePartMono.
      map(fileService::saveFile)
      .flatMap(this::toPdfDocument)
      .flatMap(repository::save)
      .subscribeOn(Schedulers.boundedElastic())
      .then();
  }

  private Mono<PdfDocument> toPdfDocument(Mono<File> fileMono) {
    return fileMono
      .flatMap(pdfReaderService::toPDFReader)
      .flatMapMany(pdfReaderService::processPdfPages)
      .map(PageDocument::new)
      .collectList()
      .flatMap(pages -> Mono.just(new PdfDocument(pages)))
      .subscribeOn(Schedulers.boundedElastic());
  }

  public Mono<Void> indexAllPdfDocuments() throws IOException {
    return Flux.fromStream(fileService.loadAll())
      .flatMap(x -> toPdfDocument(Mono.just(x)))
      .buffer(50)
      .flatMap(repository::saveAll)
      .then();
  }
}
