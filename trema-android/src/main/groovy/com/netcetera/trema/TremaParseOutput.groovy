package com.netcetera.trema;

import com.netcetera.trema.core.api.ITextNode

class TremaParseOutput {

  final List<String> languages
  final String masterLanguage
  final List<ITextNode> translations

  TremaParseOutput(List<String> languages, String masterLanguage, List<ITextNode> translations) {
    this.languages = languages
    this.masterLanguage = masterLanguage
    this.translations = translations
  }
}
