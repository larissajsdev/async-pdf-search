package com.celodev.asyncelasticpdf.pdf;

import com.celodev.asyncelasticpdf.File.FileService;
import com.celodev.asyncelasticpdf.PageDocument.PageDocumentService;
import com.celodev.asyncelasticpdf.PdfReader.PdfReaderService;
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

  private final PageDocumentService pageDocumentService;

  public Mono<Void> indexPdfPages(Mono<FilePart> filePartMono) {

//    Flux<PageDocument> pageDocumentFlux = filePartMono
//      .flatMap(fileService::saveFile)
//      .flatMapMany(pageDocumentService::toPageDocuments);

    return filePartMono
      .map(FilePart::filename)
      .log()
      .flatMap(filename -> {
        PdfDocument pdfDocument = new PdfDocument(filename);
        return filePartMono
          .flatMap(fileService::saveFile)
          .cache()
          .flatMapMany(pageDocumentService::toPageDocuments)
          .collectList()
          .map(pageDocuments -> {
            pdfDocument.setPageDocuments(pageDocuments);
            return pdfDocument;
          });
      })
      .flatMap(repository::save)
      .then();

  }

  private Mono<PdfDocument> toPdfDocument(File file) {
    return Mono.fromCallable(() -> file)
      .flatMap(pdfReaderService::toPDFReader)
      .flatMapMany(pdfReaderService::processPdfPages)
      .collectList()
      .flatMap(pages -> Mono.just(new PdfDocument(pages)))

      .subscribeOn(Schedulers.boundedElastic());
  }

  public Mono<Void> indexAllPdfDocuments() throws IOException {
    return Flux.fromStream(fileService.loadAll())
      .flatMap(this::toPdfDocument)
      .buffer(50)
      .flatMap(repository::saveAll)
      .subscribeOn(Schedulers.boundedElastic())
      .then();
  }
}
