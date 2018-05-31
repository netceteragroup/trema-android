package com.netcetera.trema

import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.gradle.api.Project

class TremaExtensionsResolver {
  Set<String> getLanguages(Project project) {
    List<String> languagesList = ObjectUtils.defaultIfNull(project.trema.languages, Collections.emptyList())
    return languagesList.toSet()
  }

  List<String> getInputFilePath(Project project) {
    def path = project.trema.inputFilePath
    def paths = project.trema.inputFilePaths

    List<String> result = new ArrayList<>()
    if (paths instanceof Iterable) {
      result.addAll(paths)
    } else if (StringUtils.isNotBlank(path)) {
      result.add(path)
    }

    if (result.isEmpty()) {
      return ["${project.projectDir}/extras/texts.trm"]
    }
    return result
  }

  String getOutputDirectoryPath(Project project) {
    String path = project.trema.outputDirectoryPath
    return StringUtils.defaultIfBlank(path, "${project.projectDir}/src/main/res/values")
  }

  String getDefaultLanguage(Project project) {
    return project.trema.defaultLanguage
  }
}