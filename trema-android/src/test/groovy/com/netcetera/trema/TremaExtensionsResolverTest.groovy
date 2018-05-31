package com.netcetera.trema

import com.netcetera.trema.gradle.TremaPluginExtension
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TremaExtensionsResolverTest {
  static String PROJECT_DIR_PATH = "projectDir"


  Project project
  TremaPluginExtension tremaExtension
  File projectDir

  TremaExtensionsResolver resolver

  @Before
  void setUp() {
    projectDir = new File(PROJECT_DIR_PATH)
    project = ProjectBuilder.builder()
      .withProjectDir(projectDir)
      .build()
    tremaExtension = new TremaPluginExtension()
    project.getExtensions().add("trema", tremaExtension)

    resolver = new TremaExtensionsResolver()
  }

  @Test
  void shouldReturnConfiguredLanguages() {
    // Given
    List<String> expected = ["mk", "en"]
    tremaExtension.languages = expected

    // When
    Set<String> result = resolver.getLanguages(project)

    // Then
    Assert.assertEquals(expected.toSet(), result)
  }

  @Test
  void shouldReturnEmptyLanguagesWhenNotConfigured() {
    // Given
    List<String> expected = null
    tremaExtension.languages = expected

    // When
    Set<String> result = resolver.getLanguages(project)

    // Then
    Assert.assertTrue(result.isEmpty())
  }

  @Test
  void shouldReturnDefaultInputPath() {
    // Given
    def expected = "${projectDir.absolutePath}/extras/texts.trm"

    // When
    List<String> result = resolver.getInputFilePath(project)

    // Then
    Assert.assertEquals([expected], result)
  }

  @Test
  void shouldReturnConfiguredPath() {
    // Given
    String expected = "expected"
    tremaExtension.inputFilePath = expected

    // When
    List<String> result = resolver.getInputFilePath(project)

    // Then
    Assert.assertEquals([expected], result)
  }

  @Test
  void shouldReturnConfiguredPaths() {
    // Given
    List<String> expected = ["path1", "path2"]
    tremaExtension.inputFilePaths = expected
    tremaExtension.inputFilePath = "fake"

    // When
    List<String> result = resolver.getInputFilePath(project)

    // Then
    Assert.assertEquals(expected, result)
  }

  @Test
  void shouldReturnConfiguredOutputPath() {
    // Given
    String expected = "outputPath"
    tremaExtension.outputDirectoryPath = expected

    // When
    def result = resolver.getOutputDirectoryPath(project)

    // Then
    Assert.assertEquals(expected, result)
  }

  @Test
  void shouldReturnDefaultOutputPath() {
    // Given
    String expected = "${projectDir.absolutePath}/src/main/res/values"

    // When
    def result = resolver.getOutputDirectoryPath(project)

    // Then
    Assert.assertEquals(expected, result)
  }

  @Test
  void shouldReturnConfiguredDefaultLanguage() {
    // Given
    String expected = "mk"
    tremaExtension.defaultLanguage = expected

    // When
    def result = resolver.getDefaultLanguage(project)

    // Then
    Assert.assertEquals(expected, result)
  }
}