package gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

// Painel que exibe a árvore sintática de forma hierárquica
public class TreePanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private JScrollPane scrollPane;
    
    public TreePanel() {
        setLayout(new BorderLayout());
        
        rootNode = new DefaultMutableTreeNode("Árvore Sintática");
        tree = new JTree(rootNode);
        tree.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void setTree(DefaultMutableTreeNode newRoot) {
        if (newRoot != null) {
            rootNode = newRoot;
            tree.setModel(new DefaultTreeModel(rootNode));
            expandAllNodes();
        }
    }
    
    public void clear() {
        rootNode = new DefaultMutableTreeNode("Árvore Sintática");
        tree.setModel(new DefaultTreeModel(rootNode));
    }
    
    private void expandAllNodes() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
    
    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }
}