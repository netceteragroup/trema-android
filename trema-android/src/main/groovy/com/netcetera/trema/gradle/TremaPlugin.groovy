package com.netcetera.trema.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class TremaPlugin implements Plugin<Project> {

  void apply(Project project) {
    project.extensions.create('trema', TremaPluginExtension)
    project.task('generateTexts', type: TremaTask)
  }

}