package recovery;

import compiler.*;

public class Follow {
    static public final RecoverySet main = new RecoverySet();
    static public final RecoverySet bloco = new RecoverySet();
    static public final RecoverySet blocos = new RecoverySet();
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
    	
    	bloco.add(TrucoCompiler.FECHAVE);
    	bloco.add(TrucoCompiler.FECHAPAR);
    	bloco.addAll(First.expLog);
    	
    	opArit.add(TrucoCompiler.ABREPAR);
    	opArit.addAll(First.termo);

    	opLog.addAll(First.expArit);
    	
    	opCond.add(TrucoCompiler.ABREPAR);
    	opCond.addAll(First.expLog);
    	
    	chamaFuncao.add(TrucoCompiler.FIMINST);
    	
    	tipoDado.add(TrucoCompiler.IDENT);
    	
    	acessaVetor.addAll(First.atribui);
    	
    	atribui.add(TrucoCompiler.FIMINST);
    	
    	termo.add(TrucoCompiler.FIMINST);
    	termo.add(TrucoCompiler.FECHAVE);
    	termo.add(TrucoCompiler.FECHAPAR);
    	termo.addAll(First.opLog);
    	
    	
    	expArit.addAll(First.opLog);
    	expArit.add(TrucoCompiler.FIMINST);
    	expLog.add(TrucoCompiler.FIMINST);
    	
    	blocos.addAll(First.bloco);
    	blocos.addAll(bloco);
 
    }
}
