package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import java.awt.*;

// Componente customizado que combina JTextArea com números de linha
public class LineNumberTextArea extends JPanel {
    private static final long serialVersionUID = 1L;
	private JTextArea textArea;
    private JTextArea lineNumbers;
    private JScrollPane scrollPane;
    
    public LineNumberTextArea() {
        setLayout(new BorderLayout());
        
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setTabSize(4);
        
        lineNumbers = new JTextArea("1");
        lineNumbers.setFont(new Font("Monospaced", Font.PLAIN, 14));
        lineNumbers.setBackground(new Color(240, 240, 240));
        lineNumbers.setEditable(false);
        lineNumbers.setFocusable(false);
        lineNumbers.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        lineNumbers.setMargin(new Insets(0, 5, 0, 5));
        
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
        });
        
        scrollPane = new JScrollPane(textArea);
        scrollPane.setRowHeaderView(lineNumbers);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void updateLineNumbers() {
        Element root = textArea.getDocument().getDefaultRootElement();
        int lineCount = root.getElementCount();
        
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            sb.append(i).append("\n");
        }
        
        lineNumbers.setText(sb.toString());
    }
    
    public JTextArea getTextArea() {
        return textArea;
    }
    
    public String getText() {
        return textArea.getText();
    }
    
    public void setText(String text) {
        textArea.setText(text);
        updateLineNumbers();
    }
    
    public void clear() {
        textArea.setText("");
        updateLineNumbers();
    }
    
    public int getLineCount() {
        return textArea.getDocument().getDefaultRootElement().getElementCount();
    }
}