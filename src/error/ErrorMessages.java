package error;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessages {
    
    // Mapeamento de tokens para descrições em português
    private static final Map<String, String> TOKEN_NAMES = new HashMap<>();
    
    static {
        // Tokens especiais
        TOKEN_NAMES.put("<EOF>", "fim do arquivo");
        TOKEN_NAMES.put("\"\"", "texto vazio");
        
        // Palavras-chave do programa
        TOKEN_NAMES.put("\"tresehojogo\"", "início do programa");
        TOKEN_NAMES.put("\"desce\"", "abertura de bloco");
        TOKEN_NAMES.put("\"sobe\"", "fechamento de bloco");
        TOKEN_NAMES.put("\"passa\"", "abertura de parênteses");
        TOKEN_NAMES.put("\"sinal\"", "fechamento de parênteses");
        
        // Declarações
        TOKEN_NAMES.put("\"carteado\"", "declaração de classe");
        TOKEN_NAMES.put("\"privado\"", "modificador privado");
        TOKEN_NAMES.put("\"publico\"", "modificador público");
        TOKEN_NAMES.put("\"eu\"", "referência this");
        TOKEN_NAMES.put("\"novinho\"", "criação de objeto");
        
        // Funções
        TOKEN_NAMES.put("\"vai\"", "declaração de função");
        TOKEN_NAMES.put("\"vem\"", "chamada de função");
        TOKEN_NAMES.put("\"facao\"", "retorno");
        
        // Variáveis e instruções
        TOKEN_NAMES.put("\"receba\"", "atribuição");
        TOKEN_NAMES.put("\"pato\"", "fim de instrução");
        
        // Tipos
        TOKEN_NAMES.put("\"copa\"", "tipo inteiro");
        TOKEN_NAMES.put("\"gato\"", "tipo decimal");
        TOKEN_NAMES.put("\"mole\"", "tipo booleano");
        TOKEN_NAMES.put("\"espadilha\"", "tipo texto");
        TOKEN_NAMES.put("\"manilha\"", "tipo vetor");
        
        // Valores booleanos
        TOKEN_NAMES.put("\"joga\"", "valor verdadeiro");
        TOKEN_NAMES.put("\"corre\"", "valor falso");
        TOKEN_NAMES.put("\"nada\"", "valor nulo");
        
        // Operadores aritméticos
        TOKEN_NAMES.put("\"mais\"", "soma");
        TOKEN_NAMES.put("\"menos\"", "subtração");
        TOKEN_NAMES.put("\"div\"", "divisão");
        TOKEN_NAMES.put("\"mul\"", "multiplicação");
        
        // Operadores lógicos
        TOKEN_NAMES.put("\"e\"", "operador E");
        TOKEN_NAMES.put("\"ou\"", "operador OU");
        TOKEN_NAMES.put("\"maior\"", "maior que");
        TOKEN_NAMES.put("\"menor\"", "menor que");
        TOKEN_NAMES.put("\"igual\"", "igual a");
        TOKEN_NAMES.put("\"diferente\"", "diferente de");
        
        // Estruturas de controle
        TOKEN_NAMES.put("\"parceiro\"", "condicional");
        TOKEN_NAMES.put("\"torna\"", "laço for");
        TOKEN_NAMES.put("\"come3\"", "repetição");
        
        // Símbolos
        TOKEN_NAMES.put("\":\"", "acesso a vetor");
        TOKEN_NAMES.put("\".\"", "ponto decimal");
        TOKEN_NAMES.put("\"'\"", "aspas simples");
    }
    
    /**
     * Converte nome técnico de token para descrição amigável em português
     */
    public static String getTokenDescription(String tokenName) {
        return TOKEN_NAMES.getOrDefault(tokenName, tokenName);
    }
    
    /**
     * Gera mensagem de erro léxico em português
     */
    public static String generateLexicalError(int linha, int coluna, char caractere, String resto) {
        
    	StringBuilder msg = new StringBuilder();
        msg.append("Erro léxico parceiro!\n");
        msg.append("Linha: ").append(linha).append("\n");
        msg.append("Coluna: ").append(coluna).append("\n");
        msg.append("Tu mandou essa bomba: '").append(caractere).append("'");
        
        // Adiciona sugestões contextuais
        String sugestao = getSugestaoLexical(caractere);
        if (sugestao != null) {
            msg.append("\n").append(sugestao);
        }
        
        return msg.toString();
    }
    
    public static String generateLexicalError(Error e) {
    	String msg = e.getMessage();
        if(msg.contains("Lexical error")) {
        	return msg.replace("Lexical error", "Erro Léxico\n")
								.replace("at line", "Linha:")
								.replace(", column",".\n Coluna:")
								.replace("Encountered: ", "\n Tu mandou essa bomba(ASCII): ");
        }
        return "Agora fudeu!" + "\n" + e.getMessage();
    }
    
    /**
     * Gera sugestões contextuais para erros léxicos
     */
    private static String getSugestaoLexical(char caractere) {
        switch (caractere) {
            case '{': return "Que tal usar 'desce' ao invés de '{'?";
            case '}': return "Que tal usar 'sobe' ao invés de '}'?";
            case '(': return "Que tal usar 'passa' ao invés de '('?";
            case ')': return "Que tal usar 'sinal' ao invés de ')'?";
            case ';': return "Que tal usar 'pato' ao invés de ';'?";
            case '=': return "Que tal usar 'receba' para atribuição?";
            case '+': return "Que tal usar 'mais' ao invés de '+'?";
            case '-': return "Que tal usar 'menos' ao invés de '-'?";
            case '*': return "Que tal usar 'mul' ao invés de '*'?";
            case '/': return "Que tal usar 'div' ao invés de '/'?";
            case '>': return "Que tal usar 'maior' ao invés de '>'?";
            case '<': return "Que tal usar 'menor' ao invés de '<'?";
            case '!': return "Que tal usar 'diferente' para comparação?";
            case '&': return "Que tal usar 'e' para operação lógica?";
            case '|': return "Que tal usar 'ou' para operação lógica?";
            default: return null;
        }
    }
    
    /**
     * Gera mensagem de erro sintático em português
     */
    public static String generateSyntaxError(int linha, int coluna, String tokenEncontrado, String[] tokensEsperados) {
        StringBuilder msg = new StringBuilder();
        msg.append("Erro sintático parceiro!\n");
        msg.append("Linha: ").append(linha).append("\n");
        msg.append("Coluna: ").append(coluna).append("\n");
        msg.append("Tu mandou essa bomba: ").append(tokenEncontrado).append("\n");
        
        if (tokensEsperados != null && tokensEsperados.length > 0) {
            msg.append("Tava esperando tipo: ");
            for (int i = 0; i < tokensEsperados.length; i++) {
                if (i > 0) msg.append(", ");
                msg.append(getTokenDescription(tokensEsperados[i]));
            }
        }
        
        return msg.toString();
    }
    
    /**
     * Converte array de tokenImage para descrições em português
     */
    public static String[] getPortugueseTokenImage(String[] originalTokenImage) {
        String[] portugueseImage = new String[originalTokenImage.length];
        for (int i = 0; i < originalTokenImage.length; i++) {
            portugueseImage[i] = getTokenDescription(originalTokenImage[i]);
        }
        return portugueseImage;
    }
}
