package com.celodev.asyncelasticpdf.PdfReader;

import com.celodev.asyncelasticpdf.PageDocument.PageDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
public class PdfReaderService {

  public Flux<PageDocument> processPdfPages(PdfParserData pdfParserData) {
    return Mono.just(pdfParserData).flatMapMany(this::extractAllPages);
  }

  public Mono<PdfParserData> toPDFReader(File pdfFile) {
    return Mono.fromCallable(() -> {
        PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        return new PdfParserData(reader, parser);
      })
      .onErrorResume(ex -> Mono.empty());
  }

  private Flux<PageDocument> extractAllPages(PdfParserData parserData) {
    return Flux.range(1, parserData.getPdfReader().getNumberOfPages())
      .concatMap(pageNum -> extractTextFromInstance(parserData.getPdfReaderContentParser(), pageNum).map(content -> new PageDocument(pageNum, content)))
      .doFinally(signalType -> parserData.getPdfReader().close());
  }

  public Mono<String> extractTextFromInstance(PdfReaderContentParser parser, int pageNum) {
    return Mono.fromCallable(() -> parser
        .processContent(pageNum, new SimpleTextExtractionStrategy())
        .getResultantText()
        .replaceAll("[-\n$]|(^ +| +$|( )+)", " ")
      )

//      .subscribeOn(Schedulers.single())
      .onErrorResume(ex -> Mono.empty());
  }
}
