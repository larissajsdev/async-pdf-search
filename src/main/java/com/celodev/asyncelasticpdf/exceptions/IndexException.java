package com.celodev.asyncelasticpdf.exceptions;

public class IndexException extends RuntimeException {
  public IndexException() {
    super("unable to index documents");
  }
}
