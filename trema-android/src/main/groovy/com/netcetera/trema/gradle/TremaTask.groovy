package com.netcetera.trema.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class TremaTask extends DefaultTask {

  @TaskAction
  def generateTexts() {
    List<String> inputFilePath = getInputFilePath()
    String outputDirectoryPath = getOutputDirectoryPath()
    Set<String> languages = getLanguages()
    String defaultLanguage = getDefaultLanguage()

    TremaExtractor tremaExtractor = new TremaExtractor()
    tremaExtractor.extractTrema(inputFilePath, outputDirectoryPath, languages, defaultLanguage)
  }

  private String getOutputDirectoryPath() {
    String path = project.trema.outputDirectoryPath
    String stringXmlsPath = path != null ? path : "$project.projectDir/src/main/res/values";
    stringXmlsPath
  }


  private Set<String> getLanguages() {
    List<String> languagesList = project.trema.languages
    Set languagesSet = null
    if (languagesList != null) {
      languagesSet = languagesList.toSet()
    }
    languagesSet
  }

  private String getDefaultLanguage() {
    project.trema.defaultLanguage
  }

  private List<String> getInputFilePath() {
    def path = project.trema.inputFilePath
    def paths = project.trema.inputFilePaths

    logger.info("path: $path");
    logger.info("paths: $paths");

    List<String> result = null
    if (paths != null && paths instanceof List) {
      result = []
      for (String p : paths) {
        result.add(p)
      }
    } else {
      result = [path]
    }

    if (result == null || result.isEmpty()) {
      result = ["$project.projectDir/extras/texts.trm"]
    }

    result
  }

}