package recovery;

import compiler.*;

public class First {
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
    	main.add(TrucoCompilerConstants.STARTPROGRAMA);
    	
    	opArit.add(TrucoCompiler.SOMA);
    	opArit.add(TrucoCompiler.SUBTRACAO);
    	opArit.add(TrucoCompiler.MULTIPLICACAO);
    	opArit.add(TrucoCompiler.DIVISAO);
    	
    	opLog.add(TrucoCompiler.OPMAIOR);
    	opLog.add(TrucoCompiler.OPMENOR);
    	opLog.add(TrucoCompiler.OPIGUAL);
    	opLog.add(TrucoCompiler.OPDIF);

    	opCond.add(TrucoCompiler.OPAND);
    	opCond.add(TrucoCompiler.OPOR);
    	
    	tipoVetor.add(TrucoCompiler.TIPOVETOR);
    	identificador.add(TrucoCompiler.IDENT);
    	
    	numero.add(TrucoCompiler.SOMA);
    	numero.add(TrucoCompiler.SUBTRACAO);
    	numero.add(TrucoCompiler.DECIMAL);
    	numero.add(TrucoCompiler.INTEIRO);

    	atribui.add(TrucoCompiler.ATRIVAR);
    	condicional.add(TrucoCompiler.COND);
    	lacoRep.add(TrucoCompiler.FOR);
    	lacoRep3.add(TrucoCompiler.REP3);
    	retorna.add(TrucoCompiler.RETURN);
    	chamaFuncao.add(TrucoCompiler.CHAMAFUNC);
    	declFuncao.add(TrucoCompiler.DECLFUNC);
    	
    	atribuiVetor.add(TrucoCompiler.ABRECHA);
    	acessaVetor.add(TrucoCompiler.ACESSVETOR);
    	
    	termo.add(TrucoCompiler.IDENT);
    	termo.add(TrucoCompiler.NULL);
    	termo.add(TrucoCompiler.BOOLTRUE);
    	termo.add(TrucoCompiler.BOOLFALSE);
    	termo.add(TrucoCompiler.STRING);
    	termo.addAll(numero);
    	termo.addAll(chamaFuncao);
    	
    	tipoDado.add(TrucoCompiler.TIPOINT);
    	tipoDado.add(TrucoCompiler.TIPODOUBLE);
    	tipoDado.add(TrucoCompiler.TIPOBOOLEAN);
    	tipoDado.add(TrucoCompiler.TIPOSTRING);
    	tipoDado.addAll(tipoVetor);

    	declVar.addAll(tipoDado);
    	
    	expArit.addAll(termo);
    	expLog.addAll(expArit);
    	expCond.addAll(expLog);
    	
    	
    }
}
