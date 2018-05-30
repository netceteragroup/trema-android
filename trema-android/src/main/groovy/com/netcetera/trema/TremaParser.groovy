package com.netcetera.trema

import com.netcetera.trema.core.Status
import com.netcetera.trema.core.XMLTextNode
import com.netcetera.trema.core.XMLValueNode
import com.netcetera.trema.core.api.ITextNode
import com.netcetera.trema.core.api.IValueNode;

class TremaParser {

  TremaParseOutput parse(String tremaText) {
    Set<String> languages = new HashSet<>()
    List<ITextNode> translations = new LinkedList<>()

    def xmlTrema = new XmlParser().parseText(tremaText)
    String masterLanguage = xmlTrema.'@masterLang'
    xmlTrema.text.each { tremaNodeText ->
      String tremaNodeKey = tremaNodeText.'@key'.replace('.', '_').replace('-', '_')
      ITextNode tremaNode = new XMLTextNode(tremaNodeKey, tremaNodeText.'@context')
      tremaNodeText.value.each { value ->
        languages.add(value.'@lang')
        Status status = Status.valueOf(value.'@status')
        IValueNode translationValue = new XMLValueNode(value.'@lang', status, value.text())
        tremaNode.addValueNode(translationValue)
      }
      translations.add(tremaNode)
    }

    return new TremaParseOutput(new ArrayList<>(languages), masterLanguage, new ArrayList<>(translations)
    )
  }

}
