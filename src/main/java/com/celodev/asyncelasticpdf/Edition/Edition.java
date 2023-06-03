package com.celodev.asyncelasticpdf.Edition;


import com.celodev.asyncelasticpdf.PageDocument.PageDocument;
import com.celodev.asyncelasticpdf.PageDocument.PageDocumentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.time.LocalDate;
import java.util.regex.Pattern;


@Data
@Service
@AllArgsConstructor
@NoArgsConstructor
public class Edition {

  private String type;
  private LocalDate publishDate;
  private Flux<PageDocument> pageDocuments;

  private PageDocumentService documentService;

  public Edition(File file) {
//    if (!isValidEdition(file.getName())) {
//      return null;
//    }
    this.type = this.filenameToEditionType(file.getName());
    this.publishDate = this.filenameToPublishDate(file.getName());
    this.pageDocuments = documentService.toPageDocuments(file);
  }

  public String filenameToEditionType(String filename) {
    return switch (filename.substring(0, 2)) {
      case "EX" -> EditionType.EXECUTIVO.getDescription();
      case "TE" -> EditionType.TERCEIROS.getDescription();
      case "JU" -> EditionType.JUDICIARIO.getDescription();
      case "SX" -> EditionType.SUPLEMENTO_EXECUTIVO.getDescription();
      case "ST" -> EditionType.SUPLEMENTO_TERCEIROS.getDescription();
      case "SJ" -> EditionType.SUPLEMENTO_JUDICIARIO.getDescription();
      default -> null;
    };
  }

  public boolean isValidEdition(String filename) {
    return Pattern.compile(
        "[E+E|E+T|E+X|S+X|J+U|S+J|T+E|S+T]{2}+([0-9][0-9][0-9][0-9])+([01]{1}[0-9]{1})+([0-3]{1}[0-9]{1}).pdf")
      .matcher(filename).find();
  }

  public LocalDate filenameToPublishDate(String filename) {
    int day = Integer.parseInt(filename.substring(8, 10));
    int month = Integer.parseInt(filename.substring(6, 8));
    int year = Integer.parseInt(filename.substring(2, 6));
    return LocalDate.of(year, month, day);
  }
}

