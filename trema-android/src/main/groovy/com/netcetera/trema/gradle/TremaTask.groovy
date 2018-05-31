package com.netcetera.trema.gradle

import com.netcetera.trema.TremaExtensionsResolver
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class TremaTask extends DefaultTask {
  final TremaExtractor tremaExtractor
  final TremaExtensionsResolver extensionsResolvers

  TremaTask() {
    this(new TremaExtractor(), new TremaExtensionsResolver())
  }

  protected TremaTask(TremaExtractor tremaExtractor, TremaExtensionsResolver tremaExtensionsResolver) {
    this.tremaExtractor = tremaExtractor
    this.extensionsResolvers = tremaExtensionsResolver
  }

  @TaskAction
  def generateTexts() {
    List<String> inputFilePath = extensionsResolvers.getInputFilePath(project)
    String outputDirectoryPath = extensionsResolvers.getOutputDirectoryPath(project)
    Set<String> languages = extensionsResolvers.getLanguages(project)
    String defaultLanguage = extensionsResolvers.getDefaultLanguage(project)

    tremaExtractor.extractTrema(inputFilePath, outputDirectoryPath, languages, defaultLanguage)
  }
}