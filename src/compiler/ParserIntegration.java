package compiler;

import gui.TrucoGUI;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.StringReader;
import error.*
;

public class ParserIntegration {
    
	private static TrucoCompiler parser = null;
	
    public static void compileCode(String code, TrucoGUI gui) {
        gui.appendToken("=== INICIANDO COMPILAÇÃO ===\n");
        ErrorHandler.setGUI(gui);
        try {
        	extractTokens(code, gui);
        	
            StringReader reader = new StringReader(code);
            
            if (parser == null) {
                parser = new TrucoCompiler(reader);
            } else {
                TrucoCompiler.ReInit(reader);
            }
            
            TrucoCompiler.lastError = null;
            TrucoCompiler.eof = false;
            
            SimpleNode root = parser.Start();
            
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
            gui.appendError(ErrorMessages.generateLexicalError(e));
        	gui.setAccepted(false);
            
        } catch (Exception e) {
            gui.appendError("ERRO INESPERADO: " + e.getMessage());
            e.printStackTrace();
            gui.setAccepted(false);
        }
    }
    
    private static void extractTokens(String code, TrucoGUI gui) {
        try {
            gui.appendToken("\n--- ANÁLISE LÉXICA ---\n");
            
            // Criar um parser temporário só para tokens
            StringReader reader = new StringReader(code);
            
            if (parser == null) {
                parser = new TrucoCompiler(reader);
            } else {
                TrucoCompiler.ReInit(reader);
            }
            
            Token token;
            int lastLine = -1;
            int tokenCount = 0;
            
            // Iterar sobre todos os tokens
            while (true) {
                token = parser.getNextToken();
                
                // Parar no EOF
                if (token.kind == TrucoCompilerConstants.EOF) {
                    break;
                }
                
                String tokenName = getTokenName(token.kind);
                String tokenValue = token.image;
                int line = token.beginLine;
                int column = token.beginColumn;
                
                // Adicionar linha em branco entre linhas diferentes
                if (lastLine != -1 && line != lastLine) {
                    gui.appendToken("");
                }
                lastLine = line;
                
                gui.appendToken(String.format("%-20s | Valor: %-15s | Linha: %3d | Coluna: %3d",
                    tokenName, tokenValue, line, column));
                
                tokenCount++;
            }
            
            gui.appendToken("\nTotal de tokens: " + tokenCount);
            gui.appendToken("--- FIM DA ANÁLISE LÉXICA ---\n");
            
        } catch (TokenMgrError e) {
        	gui.appendError(ErrorMessages.generateLexicalError(e));
        } catch (Exception e) {
            gui.appendError("\nErro ao extrair tokens: " + e.getMessage());
            e.printStackTrace();
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
        
        gui.clearHighlights();
        if (token != null && token.next != null) {
        	int linha = token.next.beginLine;
            int col = token.next.beginColumn;
            
            gui.appendError(String.format(
                "ERRO SINTÁTICO na Linha %d, Coluna %d:",
                linha,col
            ));
            
            gui.highlightError(linha, col);
            gui.appendError("  Token encontrado: " + token.next.image);
        } else if (token != null) {
        	int linha = token.beginLine;
            int col = token.beginColumn;
        	gui.appendError(String.format(
                "ERRO SINTÁTICO na Linha %d, Coluna %d:",
                linha, col
            ));
            gui.highlightError(linha,col);
        } else {
            gui.appendError("ERRO SINTÁTICO:");
            gui.appendError("  " + cleanErrorMessage(e.getMessage()));
        }
        
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
}