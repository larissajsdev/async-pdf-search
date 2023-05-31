package com.celodev.asyncelasticpdf.pdf;

import com.celodev.asyncelasticpdf.PageDocument.Page;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.File;

@Service
public class PdfReaderService {

    public Flux<Page> processPdfPages(Tuple2<PdfReader, PdfReaderContentParser> tuple) {
        PdfReader reader = tuple.getT1();
        PdfReaderContentParser parser = tuple.getT2();
        return Flux.range(1, reader.getNumberOfPages())
                .flatMap(pageNum -> extractTextFromPage(parser, pageNum).map(content -> new Page(pageNum, content)))
                .doFinally(signalType -> reader.close());
    }

    public Mono<Tuple2<PdfReader, PdfReaderContentParser>> toPDFReader(File pdfFile) {
        return Mono.fromCallable(() -> {
                    PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
                    PdfReaderContentParser parser = new PdfReaderContentParser(reader);
                    return Tuples.of(reader, parser);
                })
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<String> extractTextFromPage(PdfReaderContentParser parser, int pageNum) {
        return Mono.fromCallable(() -> parser
                .processContent(pageNum, new SimpleTextExtractionStrategy())
                .getResultantText()
                .replaceAll("[-\n$]|(^ +| +$|( )+)", " ")

        ).onErrorResume(ex -> Mono.empty());
    }
}
