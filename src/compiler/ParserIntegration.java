package compiler;

import gui.TrucoGUI;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.StringReader;
import recovery.*;

public class ParserIntegration {
    
	private static TrucoCompiler parser = null;
	
    public static void compileCode(String code, TrucoGUI gui) {
        gui.appendToken("=== INICIANDO COMPILAÇÃO ===\n");
        ErrorHandler.setGUI(gui);
        try {
            StringReader reader = new StringReader(code);
            
            if (parser == null) {
                parser = new TrucoCompiler(reader);
            } else {
                TrucoCompiler.ReInit(reader);
            }
            
            TrucoCompiler.lastError = null;
            TrucoCompiler.eof = false;
            
            SimpleNode root = TrucoCompiler.Start();
            
            extractTokens(parser, gui);
            
            buildSyntaxTree(root, gui);
            
            if (ErrorHandler.hasErrors()) {
                gui.setAccepted(false);
                gui.appendError("\n=== COMPILAÇÃO FINALIZADA COM ERROS ===");
            } else {
                gui.setAccepted(true);
                gui.appendToken("\n=== COMPILAÇÃO CONCLUÍDA COM SUCESSO ===");
            }  
        } catch (ParseException e) {
            handleParseException(e, gui);
            gui.setAccepted(false);
            
        } catch (TokenMgrError e) {
            gui.appendError("ERRO LÉXICO na linha " + extractLineFromError(e.getMessage()));
            gui.appendError("  " + e.getMessage());
            gui.setAccepted(false);
            
        } catch (Exception e) {
            gui.appendError("ERRO INESPERADO: " + e.getMessage());
            e.printStackTrace();
            gui.setAccepted(false);
        }
    }
    
    private static void extractTokens(TrucoCompiler parser, TrucoGUI gui) {
        try {
            Token token = parser.token;
            if (token == null) {
                token = parser.getNextToken();
            }
            
            int lastLine = -1;
            
            while (token != null) {
                if (token.kind == TrucoCompilerConstants.EOF) {
                    break;
                }
                
                String tokenName = getTokenName(token.kind);
                String tokenValue = token.image;
                int line = token.beginLine;
                int column = token.beginColumn;
                
                if (line != lastLine && lastLine != -1) {
                    gui.appendToken("");
                }
                lastLine = line;
                
                gui.appendToken(String.format("%-20s | Valor: %-15s | Linha: %3d | Coluna: %3d",
                    tokenName, tokenValue, line, column));
                
                token = token.next;
                if (token == null) break;
            }
        } catch (Exception e) {
            gui.appendError("Erro ao extrair tokens: " + e.getMessage());
        }
    }
    
    private static String getTokenName(int kind) {
        try {
            String[] tokenNames = TrucoCompilerConstants.tokenImage;
            if (kind >= 0 && kind < tokenNames.length) {
                String name = tokenNames[kind];
                name = name.replace("\"", "");
                return name;
            }
        } catch (Exception e) {
        }
        return "UNKNOWN";
    }
    
    private static void buildSyntaxTree(SimpleNode root, TrucoGUI gui) {
        if (root == null) {
            gui.appendError("Árvore sintática não foi gerada");
            return;
        }
        
        DefaultMutableTreeNode treeRoot = convertNodeToTreeNode(root);
        gui.setTree(treeRoot);
    }
    
    private static DefaultMutableTreeNode convertNodeToTreeNode(SimpleNode node) {
        String nodeName = node.toString();
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nodeName);
        
        int numChildren = node.jjtGetNumChildren();
        for (int i = 0; i < numChildren; i++) {
            Node child = node.jjtGetChild(i);
            if (child instanceof SimpleNode) {
                DefaultMutableTreeNode childTreeNode = convertNodeToTreeNode((SimpleNode) child);
                treeNode.add(childTreeNode);
            }
        }
        
        return treeNode;
    }
    
    private static void handleParseException(ParseException e, TrucoGUI gui) {
        Token token = e.currentToken;
        
        if (token != null && token.next != null) {
            gui.appendError(String.format(
                "ERRO SINTÁTICO na Linha %d, Coluna %d:",
                token.next.beginLine,
                token.next.beginColumn
            ));
            gui.appendError("  Token encontrado: " + token.next.image);
        } else if (token != null) {
            gui.appendError(String.format(
                "ERRO SINTÁTICO na Linha %d, Coluna %d:",
                token.beginLine,
                token.beginColumn
            ));
        } else {
            gui.appendError("ERRO SINTÁTICO:");
        }
        
        gui.appendError("  " + cleanErrorMessage(e.getMessage()));
        
        if (e.expectedTokenSequences != null && e.expectedTokenSequences.length > 0) {
            gui.appendError("\n  Esperava um dos seguintes tokens:");
            for (int[] sequence : e.expectedTokenSequences) {
                if (sequence.length > 0) {
                    String tokenName = getTokenName(sequence[0]);
                    gui.appendError("    - " + tokenName);
                }
            }
        }
    }
    
    private static String cleanErrorMessage(String message) {
        message = message.replaceAll("Encountered.*at line", "Erro na linha");
        message = message.replaceAll("Was expecting:", "Esperava:");
        return message;
    }
    
    private static String extractLineFromError(String errorMsg) {
        try {
            if (errorMsg.contains("line")) {
                int start = errorMsg.indexOf("line") + 5;
                int end = errorMsg.indexOf(",", start);
                if (end == -1) end = errorMsg.indexOf(".", start);
                if (end == -1) end = start + 5;
                return errorMsg.substring(start, end).trim();
            }
        } catch (Exception e) {
        }
        return "desconhecida";
    }
}