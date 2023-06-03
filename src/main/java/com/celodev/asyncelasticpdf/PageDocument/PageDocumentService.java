package com.celodev.asyncelasticpdf.PageDocument;

import com.celodev.asyncelasticpdf.Edition.Edition;
import com.celodev.asyncelasticpdf.File.FileService;
import com.celodev.asyncelasticpdf.PdfReader.PdfReaderService;
import com.celodev.asyncelasticpdf.configurations.ChatMessageProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PageDocumentService {

  private final PageDocumentRepository repository;
  private final PdfReaderService pdfReaderService;
  private final FileService fileService;
  private final ChatMessageProcessor chatMessageProcessor;


  public Mono<Void> indexPageDocuments2(File file) {
    return Mono.just(file)
      .flatMapMany(this::toPageDocuments)
      .buffer()
      .flatMap(repository::saveAll)
      .doOnNext(pageDocument -> chatMessageProcessor.broadcastMessage(pageDocument.getId()))
      .then();
  }

  public Mono<Void> indexPageDocuments(File File) {
    return Mono.just(File)
      .flatMap(this::toEdition)
      .flatMapMany(Edition::getPageDocuments)
      .map(e -> new PageDocument(e.getPage(), e.getPublishDate(), e.getType(), e.getContent()))
      .buffer()
      .flatMap(repository::saveAll)
      .doOnNext(pageDocument -> chatMessageProcessor.broadcastMessage(pageDocument.getId()))
      .then();
  }

  public Flux<PageDocument> toPageDocuments(File file) {
    return Mono.fromCallable(() -> file)
      .flatMap(pdfReaderService::toPDFReader)
      .flatMapMany(pdfReaderService::processPdfPages);
  }


  public Mono<Edition> toEdition(File file) {
    return Mono.fromCallable(() -> file)
      .map(Edition::new);
  }

  public Mono<Void> indexAllPageDocuments() throws IOException {
    return Flux.fromStream(fileService.loadAll())
      .flatMap(this::indexPageDocuments)
      .then();
  }

  public Mono<Void> deleteAll() {
    return repository.deleteAll().then();
  }
}
