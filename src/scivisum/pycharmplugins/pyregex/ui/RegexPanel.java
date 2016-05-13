package scivisum.pycharmplugins.pyregex.ui;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.ui.JBColor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RegexPanel extends CenterDialog {

    private JPanel contentPane;
    private JTextField regexField;
    private JLabel errorLabel;
    private JTextArea testText;
    private JTextArea resultText;
    private JButton testButton;
    private JTextArea textArea1;
    private HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(JBColor.GREEN);
    private HighlightPainter highlightPainter2 = new DefaultHighlighter.DefaultHighlightPainter(JBColor.RED);

    public RegexPanel(Window parent) {
        super(parent);
        setTitle("PyRegexTester");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        testText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                clearLabel();
                testText.getHighlighter().removeAllHighlights();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                clearLabel();
                testText.getHighlighter().removeAllHighlights();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                clearLabel();
            }
        });

        testButton.addActionListener(e -> testRegularExpressionPythonically(regexField,testText,resultText));

        this.setSize(parent.getWidth()/2, parent.getHeight()/2);
        centerOnParent();
        this.setVisible(true);
    }

    private void testRegularExpressionPythonically(JTextField regexField, JTextArea testText, JTextArea resultText){
        clearLabel();
        try {
            String regexResult = executeCommand("python", "-c", "import sys,re,json;print json.dumps([dict(zip(['start','end'],match.span(ind+1)),match=group)for match in re.finditer(*sys.argv[1:])for ind, group in list(enumerate(match.groups()))or[(-1,match.group(0))]])", regexField.getText(), testText.getText());

            JsonArray matches = new Gson().fromJson(regexResult, JsonArray.class);

            ArrayList<JsonObject> matchesAsJson = new ArrayList<>();

            for (JsonElement match : matches) {
                JsonObject obj = match.getAsJsonObject();
                matchesAsJson.add(obj);
            }

            displayRegexResult(matchesAsJson);
            displayHighlightedResultsInTestArea(matchesAsJson);

        } catch (InterruptedException|IOException|BadLocationException e) {
            e.printStackTrace();
        } catch (NonZeroExitException e) {
            errorLabel.setText("Invalid python regular expression");
        }


    }

    private void displayRegexResult(List<JsonObject> matches) {
        StringBuilder resultBoxContent = new StringBuilder();
        resultBoxContent.append("Total Matches:").append(matches.size()).append("\n\n");
        int index = 1;
        for (JsonObject match : matches){
            resultBoxContent.append("Match ").append(index).append(":").append("\n")
                    .append(match.get("match")).append("\n\n");
            index++;
        }
        resultText.setText(resultBoxContent.toString());
    }

    private void displayHighlightedResultsInTestArea(List<JsonObject> matches) throws BadLocationException {
        Highlighter highlighter = testText.getHighlighter();
        highlighter.removeAllHighlights();

        boolean odd = false;
        for (JsonObject match : matches)
        {
            odd = !odd;
            HighlightPainter painter = odd ? highlightPainter : highlightPainter2;
            highlighter.addHighlight(match.get("start").getAsInt(), match.get("end").getAsInt(), painter);
        }
    }

    private void clearLabel() {
        this.errorLabel.setText("");
    }

    private String executeCommand(String... command) throws InterruptedException, IOException, NonZeroExitException {
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        if (p.exitValue() != 0) {
            throw new NonZeroExitException();
        }
        return new Scanner(p.getInputStream()).useDelimiter("\\Z").next();
    }
}

class NonZeroExitException extends Exception {}