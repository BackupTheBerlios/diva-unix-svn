package uk.ulancs.diva.fmp2diva;

import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionNode;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionParser;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionScanner;
import ca.uwaterloo.gp.fmp.constraints.expression.ExpressionParser.ExpressionParseException;
import ca.uwaterloo.gp.fmp.constraints.expression.Symbols.Token;
import ca.uwaterloo.gp.fmp.constraints.expression.Symbols;
import ca.uwaterloo.gp.fmp.system.IdTable;

public class MyExpressionValidator {
	
	protected IdTable idTable;
	public MyExpressionValidator(IdTable table) {
		this.idTable = table;
	}
	
	public String isValid(String input) {
		if (input == null)
			return "Constraint is empty.";
		
		ExpressionScanner scanner = new ExpressionScanner(input);
		ExpressionParser parser = new ExpressionParser();
		try {
			ExpressionNode root = parser.createExpression(scanner);
			String idCheck = traverse(root);
			if (idCheck != null)
				return "";
		} catch (ExpressionParseException e) {
			return "";
		}
		
		return null;
	}
	
	/**
	 * @param n
	 * @return name of the identifier that doesn't exist, null if the expression
	 * tree is correct
	 */
	protected String traverse(ExpressionNode n) {
		Token t = n.getSymbol();
		
		if (t.getId() == Symbols.TokenType.IDENTIFIER) {
			if (!idTable.containsKey(t.getLexeme())) return t.getLexeme();
		}
		
		for (ExpressionNode child : n.getChildren()) {
			String result = traverse(child);
			if (result != null) return result;
		}
		return null;
	}

}
