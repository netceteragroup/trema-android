package com.netcetera.trema.gradle

import com.netcetera.trema.core.Status
import com.netcetera.trema.core.XMLTextNode
import com.netcetera.trema.core.XMLValueNode
import com.netcetera.trema.core.api.IExporter
import com.netcetera.trema.core.api.ITextNode
import com.netcetera.trema.core.exporting.AndroidExporter
import com.netcetera.trema.core.exporting.FileOutputStreamFactory
import com.netcetera.trema.core.exporting.OutputStreamFactory

class TremaExtractor {

  private static final String ENCODING = 'UTF-8'

  public void extractTrema(List<String> tremaPaths, String stringXmlsPath, Set<String> languages, String defaultLanguage) {
    def translations = []
    Set languagesFromTrema = []
    def masterLanguagesFromTrema = []


    for (String tremaPath : tremaPaths) {
      paresTrema tremaPath, languagesFromTrema, masterLanguagesFromTrema, translations
    }

    String defaultLanguageToUse;
    if (defaultLanguage != null) {
      defaultLanguageToUse = defaultLanguage
    } else {
      defaultLanguageToUse = languagesFromTrema[0]
    }

    Set<String> languagesToExport
    if (languages != null) {
      languagesToExport = languages
    } else {
      languagesToExport = languagesFromTrema
    }

    if (!languagesToExport.contains(defaultLanguageToUse)) {
      throw new RuntimeException("Language $defaultLanguage is set as masterLang in the trema or plugin configuration, "
        + "but not translations for it were found.")
    }

    // filter duplicate translations
    Map<String, ITextNode> filteredTranslationsMap = new LinkedHashMap<>();
    for (ITextNode translation : translations) {
      def key = translation.getKey()
      if (!filteredTranslationsMap.containsValue(key)) {
        filteredTranslationsMap.put(key, translation)
      }
    }

    def filteredTranslations = []
    filteredTranslations.addAll(filteredTranslationsMap.values())


    writeTranslations filteredTranslations, languagesToExport, defaultLanguageToUse, stringXmlsPath
  }

  private static void paresTrema(String tremaPath, languages, masterLanguages, translations) {
    File tremaFile = new File(tremaPath)
    def tremaText = tremaFile.getText(ENCODING)
    def trema = new XmlParser().parseText(tremaText)
    trema.text.each { text ->
      def translationKey = text.'@key'.replace('.', '_').replace('-', '_')
      def translationText = new XMLTextNode(translationKey, text.'@context')
      text.value.each { value ->
        languages.add(value.'@lang')
        def status = Status.valueOf(value.'@status')
        def translationValue = new XMLValueNode(value.'@lang', status, value.text())
        translationText.addValueNode(translationValue)
      }
      translations.add(translationText)
    }
    masterLanguages.add(trema.'@masterLang')
  }

  private
  static void writeTranslations(List<ITextNode> translations, languages, String defaultLanguage, String stringXmlsPath) {
    writeTranslation translations, defaultLanguage, stringXmlsPath
    languages.remove(defaultLanguage);
    languages.each { language ->
      writeTranslation translations, language, "$stringXmlsPath-$language"
    }
  }

  private static void writeTranslation(List<ITextNode> translations, String language, String stringXmlPath) {
    def folder = new File(stringXmlPath)
    if (!folder.exists()) {
      folder.mkdirs()
    }

    File outputStringFile = new File("$stringXmlPath/strings.xml")
    OutputStreamFactory outputStreamFactory = new FileOutputStreamFactory()
    IExporter androidTremaExporter = new AndroidExporter(outputStringFile, outputStreamFactory)
    androidTremaExporter.export(translations.toArray(new ITextNode[0]), null, language, null)
  }

}