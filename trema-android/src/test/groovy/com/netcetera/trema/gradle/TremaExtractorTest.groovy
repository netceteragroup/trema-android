package com.netcetera.trema.gradle

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import org.junit.After
import org.junit.Test

import static org.junit.Assert.assertTrue

class TremaExtractorTest {

  private static final String PATH_TO_RESOURCES = 'src/test/resources'
  private static final String PATH_TO_TMP_RESOURCES = "$PATH_TO_RESOURCES/tmp"
  private static final String ENCODING = 'UTF-8'

  @After
  public void cleanup() {
    def tmp = new File(PATH_TO_TMP_RESOURCES)
    tmp.deleteDir()
  }

  @Test
  public void shouldGenerateTexts() {
    // given
    TremaExtractor tremaExtractor = new TremaExtractor();

    // when
    tremaExtractor.extractTrema(["$PATH_TO_RESOURCES/texts.trm"], "$PATH_TO_TMP_RESOURCES/values", null, null)

    // then
    checkGeneratedTexts("") // default language
    checkGeneratedTexts("-en")
    checkGeneratedTexts("-fr")
    checkGeneratedTexts("-it")
  }

  private static void checkGeneratedTexts(String langSuffix) {
    XMLUnit.setIgnoreWhitespace(true)
    XMLUnit.setIgnoreComments(true)
    XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true)
    XMLUnit.setNormalizeWhitespace(true)


    def expected = new File("$PATH_TO_RESOURCES/values$langSuffix/strings.xml").getText(ENCODING)
    def generated = new File("$PATH_TO_TMP_RESOURCES/values$langSuffix/strings.xml").getText(ENCODING)
    Diff diff = new Diff(expected, generated)
    diff.findAll().each {
      println it
    }
    assertTrue(diff.identical())
  }
}