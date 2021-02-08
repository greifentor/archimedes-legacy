// Generated from ComplexFK.g4 by ANTLR 4.7
package archimedes.grammar.cfk;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ComplexFKParser}.
 */
public interface ComplexFKListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ComplexFKParser#complexForeignKeyExpressions}.
	 * @param ctx the parse tree
	 */
	void enterComplexForeignKeyExpressions(ComplexFKParser.ComplexForeignKeyExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ComplexFKParser#complexForeignKeyExpressions}.
	 * @param ctx the parse tree
	 */
	void exitComplexForeignKeyExpressions(ComplexFKParser.ComplexForeignKeyExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ComplexFKParser#complexForeignKeyExpression}.
	 * @param ctx the parse tree
	 */
	void enterComplexForeignKeyExpression(ComplexFKParser.ComplexForeignKeyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ComplexFKParser#complexForeignKeyExpression}.
	 * @param ctx the parse tree
	 */
	void exitComplexForeignKeyExpression(ComplexFKParser.ComplexForeignKeyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ComplexFKParser#columnNameList}.
	 * @param ctx the parse tree
	 */
	void enterColumnNameList(ComplexFKParser.ColumnNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ComplexFKParser#columnNameList}.
	 * @param ctx the parse tree
	 */
	void exitColumnNameList(ComplexFKParser.ColumnNameListContext ctx);
}