package com.celodev.asyncelasticpdf.Edition;

public enum EditionType {
  EXECUTIVO("Executivo", "EX"),
  TERCEIROS("Terceiros", "TE"),
  JUDICIARIO("Judiciário", "JU"),
  SUPLEMENTO_EXECUTIVO("Suplemento Exectivo", "SX"),
  SUPLEMENTO_TERCEIROS("Suplemento Terceiros", "ST"),
  SUPLEMENTO_JUDICIARIO("Suplemento Judiciário", "SJ");

  private final String description;
  private final String prefix;

  EditionType(String description, String prefix) {
    this.description = description;
    this.prefix = prefix;
  }

  public String getDescription() {
    return description;
  }

  public String getPrefix() {
    return prefix;
  }
}
