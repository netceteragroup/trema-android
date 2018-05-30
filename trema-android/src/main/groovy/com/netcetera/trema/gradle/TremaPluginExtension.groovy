package com.netcetera.trema.gradle

/**
 * Extension class for the plugin configuration.
 */
class TremaPluginExtension {
  def String outputDirectoryPath;
  def String inputFilePath;
  def List<String> inputFilePaths;
  def List<String> languages;
  def String defaultLanguage;
}
