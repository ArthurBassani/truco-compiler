package gui;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.*;

import compiler.ParserIntegration;

public class TrucoGUI extends JFrame {
    private static final long serialVersionUID = 1L;
	private LineNumberTextArea codeEditor;
    private JTextArea tokensArea;
    private JTextArea errorsArea;
    private TreePanel treePanel;
    private JLabel statusLabel;
    private JLabel lineCountLabel;
    private JFileChooser fileChooser;
    private File currentFile;
    
    public TrucoGUI() {
        setTitle("Compilador Truco");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        initComponents();
        setupLayout();
        setupMenuBar();
    }
    
    private void initComponents() {
        codeEditor = new LineNumberTextArea();
        
        tokensArea = new JTextArea();
        tokensArea.setEditable(false);
        tokensArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        errorsArea = new JTextArea();
        errorsArea.setEditable(false);
        errorsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        errorsArea.setForeground(Color.RED);
        
        treePanel = new TreePanel();
        
        statusLabel = new JLabel("Status: Aguardando análise");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.LIGHT_GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        lineCountLabel = new JLabel("Linhas: 0 | Caracteres: 0");
        lineCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        codeEditor.getTextArea().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateStats(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateStats(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateStats(); }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(5, 5));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createToolbar(), BorderLayout.NORTH);
        topPanel.add(createEditorPanel(), BorderLayout.CENTER);
        
        JPanel bottomPanel = createBottomPanel();
        JPanel rightPanel = createRightPanel();
        
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, topPanel, rightPanel);
        mainSplitPane.setDividerLocation(900);
        mainSplitPane.setResizeWeight(0.7);
        
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainSplitPane, bottomPanel);
        verticalSplitPane.setDividerLocation(550);
        verticalSplitPane.setResizeWeight(0.7);
        
        add(verticalSplitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbar.setBorder(BorderFactory.createEtchedBorder());
        
        JButton btnOpen = new JButton("Abrir Arquivo");
        btnOpen.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        btnOpen.addActionListener(e -> openFile());
        
        JButton btnSave = new JButton("Salvar");
        btnSave.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        btnSave.addActionListener(e -> saveFile());
        
        JButton btnCompile = new JButton("Compilar");
        btnCompile.setIcon(UIManager.getIcon("OptionPane.okIcon"));
        btnCompile.setFont(new Font("Arial", Font.BOLD, 12));
        btnCompile.setBackground(new Color(100, 200, 100));
        btnCompile.addActionListener(e -> compile());
        
        JButton btnClear = new JButton("Limpar");
        btnClear.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        btnClear.addActionListener(e -> clearAll());
        
        toolbar.add(btnOpen);
        toolbar.add(btnSave);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(btnCompile);
        toolbar.add(btnClear);
        
        return toolbar;
    }
    
    private JPanel createEditorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Editor de Código",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        panel.add(codeEditor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JPanel tokensPanel = new JPanel(new BorderLayout());
        tokensPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Tokens Reconhecidos",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        tokensPanel.add(new JScrollPane(tokensArea), BorderLayout.CENTER);
        
        JPanel errorsPanel = new JPanel(new BorderLayout());
        errorsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Mensagens de Erro",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        errorsPanel.add(new JScrollPane(errorsArea), BorderLayout.CENTER);
        
        panel.add(tokensPanel);
        panel.add(errorsPanel);
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Árvore Sintática",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        panel.add(treePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(lineCountLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        openItem.addActionListener(e -> openFile());
        
        JMenuItem saveItem = new JMenuItem("Salvar");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        saveItem.addActionListener(e -> saveFile());
        
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu compileMenu = new JMenu("Compilar");
        JMenuItem compileItem = new JMenuItem("Executar Compilação");
        compileItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
        compileItem.addActionListener(e -> compile());
        
        JMenuItem clearItem = new JMenuItem("Limpar Tudo");
        clearItem.addActionListener(e -> clearAll());
        
        compileMenu.add(compileItem);
        compileMenu.add(clearItem);
        
        JMenu helpMenu = new JMenu("Ajuda");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(compileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void openFile() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(currentFile));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                
                codeEditor.setText(content.toString());
                setTitle("Compilador Truco - " + currentFile.getName());
                updateStatus("Arquivo carregado: " + currentFile.getName(), Color.LIGHT_GRAY);
            } catch (IOException e) {
                showError("Erro ao abrir arquivo: " + e.getMessage());
            }
        }
    }
    
    private void saveFile() {
        if (currentFile == null) {
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
            } else {
                return;
            }
        }
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
            writer.write(codeEditor.getText());
            writer.close();
            updateStatus("Arquivo salvo com sucesso", Color.LIGHT_GRAY);
        } catch (IOException e) {
            showError("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
    
    private void compile() {
        clearOutput();
        updateStatus("Compilando...", Color.YELLOW);
        
        String code = codeEditor.getText();
        
        if (code.trim().isEmpty()) {
            updateStatus("Status: Código vazio", Color.ORANGE);
            errorsArea.append("ERRO: Nenhum código para compilar.\n");
            return;
        }
        
        try {
            ParserIntegration.compileCode(code, this);            
        } catch (Exception e) {
            updateStatus("Status: ERRO NA COMPILAÇÃO", Color.RED);
            errorsArea.append("ERRO: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
    
    public void setTree(DefaultMutableTreeNode root) {
        treePanel.setTree(root);
    }
    
    private void clearAll() {
        codeEditor.clear();
        clearOutput();
        currentFile = null;
        setTitle("Compilador Truco - Análise Léxica e Sintática");
        updateStatus("Status: Aguardando análise", Color.LIGHT_GRAY);
    }
    
    private void clearOutput() {
        tokensArea.setText("");
        errorsArea.setText("");
        treePanel.clear();
    }
    
    private void updateStats() {
        int lines = codeEditor.getLineCount();
        int chars = codeEditor.getText().length();
        lineCountLabel.setText("Linhas: " + lines + " | Caracteres: " + chars);
    }
    
    private void updateStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setBackground(color);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(
            this,
            "Compilador Truco\n" +
            "Análise Léxica e Sintática\n\n" +
            "Desenvolvido para a disciplina de Construção de Compiladores\n" +
            "JavaCC + JJTree\n\n" +
            "2025",
            "Sobre",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public void appendToken(String token) {
        tokensArea.append(token + "\n");
    }
    
    public void appendError(String error) {
        errorsArea.append(error + "\n");
    }
    
    public void setAccepted(boolean accepted) {
        if (accepted) {
            updateStatus("Status: CÓDIGO ACEITO ✓", new Color(100, 200, 100));
        } else {
            updateStatus("Status: CÓDIGO REJEITADO ✗", Color.RED);
        }
    }
}
