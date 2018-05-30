package com.netcetera.trema.gradle

import com.netcetera.trema.TremaParseOutput
import com.netcetera.trema.TremaParser
import com.netcetera.trema.core.api.IExporter
import com.netcetera.trema.core.api.ITextNode
import com.netcetera.trema.core.exporting.AndroidExporter
import com.netcetera.trema.core.exporting.FileOutputStreamFactory
import com.netcetera.trema.core.exporting.OutputStreamFactory
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils

class TremaExtractor {

  private static final String ENCODING = 'UTF-8'
  final TremaParser tremaParser
  final OutputStreamFactory outputStreamFactory

  TremaExtractor() {
    tremaParser = new TremaParser()
    outputStreamFactory = new FileOutputStreamFactory()
  }

  void extractTrema(List<String> tremaPaths, String stringXmlsPath, Set<String> languages, String defaultLanguage) {
    List<ITextNode> translations = new ArrayList<>()
    Set<String> languagesFromTrema = new HashSet<>()
    Set<String> masterLanguagesFromTrema = new HashSet<>()


    for (String tremaPath : tremaPaths) {
      File tremaFile = new File(tremaPath)
      def tremaText = tremaFile.getText(ENCODING)

      TremaParseOutput parseOutput = tremaParser.parse(tremaText)
      translations.addAll(parseOutput.translations)
      languagesFromTrema.addAll(parseOutput.languages)
      masterLanguagesFromTrema.add(parseOutput.masterLanguage)
    }

    String defaultLanguageToUse = StringUtils.defaultIfBlank(defaultLanguage, languagesFromTrema[0])
    Set<String> languagesToExport = ObjectUtils.defaultIfNull(languages, languagesFromTrema)

    if (!languagesToExport.contains(defaultLanguageToUse)) {
      throw new RuntimeException("Language $defaultLanguage is set as masterLang in the trema or plugin configuration, "
        + "but not translations for it were found.")
    }

    // filter duplicate translations
    List<ITextNode> filteredTranslations = translations.unique(false) {t1, t2 -> t1.key <=> t2.key}

    writeTranslations(filteredTranslations, languagesToExport, defaultLanguageToUse, stringXmlsPath)
  }

  private void writeTranslations(List<ITextNode> translations, languages, String defaultLanguage, String stringXmlsPath) {
    writeTranslation translations, defaultLanguage, stringXmlsPath
    languages.remove(defaultLanguage)
    languages.each { language ->
      writeTranslation(translations, language, "$stringXmlsPath-$language")
    }
  }

  private void writeTranslation(List<ITextNode> translations, String language, String stringXmlPath) {
    File folder = new File(stringXmlPath)
    if (!folder.exists()) {
      folder.mkdirs()
    }

    File outputStringFile = new File("$stringXmlPath/strings.xml")
    IExporter androidTremaExporter = new AndroidExporter(outputStringFile, outputStreamFactory)
    androidTremaExporter.export(translations.toArray(new ITextNode[0]), null, language, null)
  }

}