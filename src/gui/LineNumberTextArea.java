package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position.Bias;

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
    
    public void clearHighlights() {
        SwingUtilities.invokeLater(() -> {
            Highlighter h = textArea.getHighlighter();
            h.removeAllHighlights();
        });
    }

    /** Destaca toda a linha (1-based). */
    public void highlightLine(int line) {
        SwingUtilities.invokeLater(() -> {
            try {
                int start = textArea.getLineStartOffset(line - 1);
                int end = textArea.getLineEndOffset(line - 1);
                Highlighter.HighlightPainter painter =
                        new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 200, 200)); // fundo rosado
                textArea.getHighlighter().addHighlight(start, end, painter);
                // rolar para a linha
                scrollToOffset(start);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Destaca um intervalo baseado em linha (1-based), coluna (1-based) e comprimento.
     * Se length <= 0, destaca só o caractere em column.
     */
    public void highlightRange(int line, int column, int length) {
        SwingUtilities.invokeLater(() -> {
            try {
                int lineStart = textArea.getLineStartOffset(line - 1);
                int start = Math.max(0, lineStart + (column - 1));
                int end = start + Math.max(1, length); // pelo menos 1
                int docLen = textArea.getDocument().getLength();
                if (start >= docLen) start = docLen - 1;
                if (end > docLen) end = docLen;
                Highlighter.HighlightPainter painter =
                        new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 200, 200));
                textArea.getHighlighter().addHighlight(start, end, painter);
                scrollToOffset(start);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }

    /** Destaca com underline ondulado (squiggle) — mais "IDE-like". */
    public void highlightSquiggle(int line, int column, int length) {
        SwingUtilities.invokeLater(() -> {
            try {
                int lineStart = textArea.getLineStartOffset(line - 1);
                int start = lineStart + (column - 1);
                int end = start + Math.max(1, length);
                Highlighter h = textArea.getHighlighter();
                h.addHighlight(start, end, new SquigglePainter(Color.RED));
                scrollToOffset(start);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }

    /** Scroll para que o offset fique visível */
    private void scrollToOffset(int offset) {
        try {
            Rectangle r = textArea.modelToView(offset);
            if (r != null) {
                textArea.scrollRectToVisible(r);
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
    
    private static class SquigglePainter extends DefaultHighlighter.DefaultHighlightPainter {
        public SquigglePainter(Color c) { super(c); }

        @Override
        public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
            try {
                // obtém retângulos para desenho
                TextUI ui = c.getUI();
                Rectangle r0 = ui.modelToView(c, p0, Bias.Forward);
                Rectangle r1 = ui.modelToView(c, p1, Bias.Backward);
                if (r0 == null || r1 == null) return;

                int x = r0.x;
                int y = Math.max(r0.y, r1.y) + r0.height - 2; // posição do underline
                int w = (r1.x + r1.width) - r0.x;
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(getColor());
                // desenha uma linha ondulada simples
                int waveHeight = 3;
                for (int i = x; i < x + w; i += 4) {
                    g2.drawLine(i, y, i + 2, y - waveHeight);
                    g2.drawLine(i + 2, y - waveHeight, i + 4, y);
                }
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}


