package truco;

public class LexicalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
	private final int linha;
    private final int coluna;
    private final char caractere;

    public LexicalException(int linha, int coluna, char caractere) {
        super( "Erro léxico parceiro!\n" +
               "Linha: " + linha + "\n" +
               "Coluna: " + coluna + "\n" + 
               "Tu mandou essa bomba: '" + caractere + "'");
        this.linha = linha;
        this.coluna = coluna;
        this.caractere = caractere;
    }
    
    public LexicalException(int linha, int coluna, char caractere, String resto) {
        super( "Erro léxico parceiro!\n" +
               "Linha: " + linha + "\n" +
               "Coluna: " + coluna + "\n" + 
               "Tu mandou essa bomba: '" + caractere + "'");
        this.linha = linha;
        this.coluna = coluna;
        this.caractere = caractere;
    }

    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    public char getCaractere() { return caractere; }
}
