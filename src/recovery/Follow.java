package recovery;

import compiler.*;

public class Follow {
    static public final RecoverySet main = new RecoverySet();
    static public final RecoverySet opArit = new RecoverySet();
    static public final RecoverySet opLog = new RecoverySet();
    static public final RecoverySet opCond = new RecoverySet();
    static public final RecoverySet tipoVetor = new RecoverySet();
    static public final RecoverySet atribuiVetor = new RecoverySet();
    static public final RecoverySet identificador = new RecoverySet();
    static public final RecoverySet numero = new RecoverySet();
    static public final RecoverySet atribui = new RecoverySet();
    static public final RecoverySet condicional = new RecoverySet();
    static public final RecoverySet lacoRep = new RecoverySet();
    static public final RecoverySet lacoRep3 = new RecoverySet();
    static public final RecoverySet retorna = new RecoverySet();
    static public final RecoverySet chamaFuncao = new RecoverySet();
    static public final RecoverySet declFuncao = new RecoverySet();
    static public final RecoverySet termo = new RecoverySet();
    static public final RecoverySet declVar = new RecoverySet();
    static public final RecoverySet tipoDado = new RecoverySet();
    static public final RecoverySet expArit = new RecoverySet();
    static public final RecoverySet expLog = new RecoverySet();
    static public final RecoverySet expCond = new RecoverySet();
    static public final RecoverySet acessaVetor = new RecoverySet();

    static {
    	main.add(TrucoCompilerConstants.EOF);
    	
    	
    }
}
