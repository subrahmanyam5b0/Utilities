/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.procurement.utils;

import java.io.StringWriter;
import java.io.Writer;
import net.htmlparser.jericho.*;


/**
 *Utility Class to Convert HTML Text to Plain Text
 * 
 * @author USER
 */
public class TextProcessor {

    private int parseInt(String text, int defaultValue) {        
        if (text == null) {
            return defaultValue;
        }        
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
    
    /**
     * Converts HTML Text to Plain String
     * @param sourceText
     * @return plain Text equivalent to the HTML <code>sourceText</code>
     * @throws Exception 
     */

    public String convertHTMLText_TO_PlainText(String sourceText) throws Exception {
        Writer responseWriter;
        String output = "";
        String parserLog = "";
        boolean initialise = sourceText == null;
        int maxLineLength = 250;
        int hrLineLength = 250;
        boolean includeHyperlinkURLs = true;
        boolean includeAlternateText = true;
        boolean decorateFontStyles = false;
        int blockIndentSize = 4;
        int listIndentSize = 6;
        if (sourceText == null) {
            sourceText = "";            
        } else {
            Source source = new Source(sourceText);
            Writer logWriter = new StringWriter();           
            String rawOutput = source.getRenderer().setMaxLineLength(maxLineLength).setHRLineLength(hrLineLength).setIncludeHyperlinkURLs(includeHyperlinkURLs).setIncludeAlternateText(includeAlternateText).setDecorateFontStyles(decorateFontStyles).setBlockIndentSize(blockIndentSize).setListIndentSize(listIndentSize).toString();
            output = CharacterReference.encode(rawOutput);
            parserLog = CharacterReference.encodeWithWhiteSpaceFormatting(logWriter.toString());
        }
        return output;
    }
}
