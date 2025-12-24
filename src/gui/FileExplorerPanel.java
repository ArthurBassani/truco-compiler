package gui;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;

public class FileExplorerPanel extends JPanel {

    private static final long serialVersionUID = 1L;
	private JTree tree;
    private DefaultTreeModel model;
    public interface FileExplorerListener {
        void onFileSelected(File file);
    }

    public FileExplorerPanel(FileExplorerListener listener) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnNovo = new JButton("Novo arquivo");
        JButton btnExcluir = new JButton("Excluir");

        topPanel.add(btnNovo);
        topPanel.add(btnExcluir);
        
        btnNovo.addActionListener(e -> {
            TreePath path = tree.getSelectionPath();
            if (path == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma pasta primeiro.");
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object obj = node.getUserObject();
            File selected;

            if (obj instanceof FileExplorerPanel.FileNode) {
                selected = ((FileExplorerPanel.FileNode) obj).file;
            } else if (obj instanceof File) {
                selected = (File) obj;
            } else {
                return; // não deveria acontecer
            }
            File directory = selected.isDirectory() ? selected : selected.getParentFile();

            String name = JOptionPane.showInputDialog(this, "Nome do novo arquivo:");
            if (name == null || name.isBlank()) return;

            File newFile = new File(directory, name);

            try {
                if (newFile.createNewFile()) {
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileNode(newFile));
                    node.add(newNode);
                    model.reload(node);
                } else {
                    JOptionPane.showMessageDialog(this, "Arquivo já existe!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao criar arquivo: " + ex.getMessage());
            }
        });
        
        btnExcluir.addActionListener(e -> {
            TreePath path = tree.getSelectionPath();
            if (path == null) {
                JOptionPane.showMessageDialog(this, "Selecione um arquivo ou pasta.");
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object obj = node.getUserObject();
            File selected;

            if (obj instanceof FileExplorerPanel.FileNode) {
                selected = ((FileExplorerPanel.FileNode) obj).file;
            } else if (obj instanceof File) {
                selected = (File) obj;
            } else {
                return; // não deveria acontecer
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Excluir " + selected.getName() + "?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                deleteRecursively(selected);
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                parent.remove(node);
                model.reload(parent);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        });


        add(topPanel, BorderLayout.NORTH);

        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Nenhuma pasta aberta");
        model = new DefaultTreeModel(root);

        tree = new JTree(model);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;

            Object obj = node.getUserObject();
            if (obj instanceof FileNode) {
                File file = ((FileNode)obj).file;
                if (file.isFile()) {
                    listener.onFileSelected(file);
                }
            }
        });

        add(new JScrollPane(tree), BorderLayout.CENTER);
    }
    
    private void deleteRecursively(File file) throws Exception {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursively(child);
            }
        }
        if (!file.delete()) {
            throw new Exception("Não foi possível excluir: " + file.getName());
        }
    }

    public void loadDirectory(File directory) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileNode(directory));
        addChildren(root, directory);
        model.setRoot(root);
        tree.expandRow(0);
    }

    private void addChildren(DefaultMutableTreeNode node, File file) {
        File[] files = file.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (!f.isHidden()) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(new FileNode(f));
                node.add(child);

                if (f.isDirectory()) {
                    addChildren(child, f);
                }
            }
        }
    }
    private static class FileNode {
        File file;

        FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return file.getName(); // APENAS O NOME
        }
    }
}
