package com.netcetera.trema.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

class TremaPluginTest {

  @Test
  void shouldAddGenerateTextsTask() {
    // given
    Project project = ProjectBuilder.builder().build()

    // when
    project.apply plugin: 'com.netcetera.trema'

    // then
    Assert.assertTrue(project.tasks.generateTexts instanceof TremaTask)
  }

}