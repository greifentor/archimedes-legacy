/*
 * ComplexForeignKeyFactory.java
 *
 * 25.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.grammar.cfk;

import static corentx.util.Checks.*;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.*;
import org.antlr.v4.runtime.tree.*;

import archimedes.grammar.cfk.ComplexFKParser.*;
import archimedes.model.*;

/**
 * Builds a complex foreign key from a string.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.09.2017 - Added.
 */

public class ComplexForeignKeyFactory {

    /**
     * Creates a complex foreign key for the passed string.
     *
     * @param table The table which the complex foreign key model is to create for.
     * @return An array of complex foreign keys which are stored in the passed String.
     *
     * @changed OLI 25.09.2017 - Added.
     */
    public ComplexForeignKeyModel[] create(TableModel table) {
        ensure(table != null, "table cannot be null.");
        List<ComplexForeignKeyModel> l = new LinkedList<ComplexForeignKeyModel>();
        if ((table.getComplexForeignKeyDefinition() != null)
                && !table.getComplexForeignKeyDefinition().isEmpty()) {
            String cfks = table.getComplexForeignKeyDefinition().replace(" ", "");
            CharStream cs = CharStreams.fromString(cfks);
            ComplexFKLexer lexer = new ComplexFKLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ComplexFKParser parser = new ComplexFKParser(tokens);
            ComplexForeignKeyErrorListener el = new ComplexForeignKeyErrorListener();
            parser.addErrorListener(el);
            ParseTree tree = parser.complexForeignKeyExpressions();
            String errors = el.getErrorString();
            if (errors.length() > 0) {
                throw new IllegalArgumentException(errors);
            }
            for (int i = 0, leni = tree.getChildCount(); i < leni; i++) {
                ParseTree prc = tree.getChild(i);
                if (prc instanceof ComplexForeignKeyExpressionContext) {
                    l.add(this.parseComplexForeignKeyExpression(
                            (ComplexForeignKeyExpressionContext) prc, table.getName()));
                }
            }
        }
        return l.toArray(new ComplexForeignKeyModel[0]);
    }

    private ComplexForeignKeyModel parseComplexForeignKeyExpression(
            ComplexForeignKeyExpressionContext ctx, String tableName) {
        return new ComplexForeignKeyModel(tableName,
                this.parseColumnNames(ctx.columnNameList(0)), ctx.Identifier().getText(),
                this.parseColumnNames(ctx.columnNameList(1)));
    }

    private String[] parseColumnNames(ColumnNameListContext ctx) {
        String[] cn = new String[ctx.Identifier().size()];
        for (int i = 0, leni = ctx.Identifier().size(); i < leni; i++) {
            cn[i] = ctx.Identifier(i).getText();
        }
        return cn;
    }

}


class ComplexForeignKeyErrorListener implements ANTLRErrorListener {

    private List<String> errors = new LinkedList<String>();

    /**
     * Returns a list of error messages if there are some or an empty list otherwise.
     *
     * @return A list of error messages if there are some or an empty list otherwise.
     *
     * @changed OLI 26.09.2017 - Added.
     */
    public String[] getErrors() {
        return this.errors.toArray(new String[0]);
    }

    /**
     * Returns a string with all the error messages if there are some or an string otherwise.
     *
     * @return A string with all the error messages if there are some or an string otherwise.
     *
     * @changed OLI 26.09.2017 - Added.
     */
    public String getErrorString() {
        StringBuffer sb = new StringBuffer();
        for (String s : this.errors) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * @changed OLI 26.09.2017 - Added.
     */
    @Override public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex,
            int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
    }

    /**
     * @changed OLI 26.09.2017 - Added.
     */
    @Override public void reportAttemptingFullContext(Parser recognizer, DFA dfa,
            int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
    }

    /**
     * @changed OLI 26.09.2017 - Added.
     */
    @Override public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex,
            int stopIndex, int prediction, ATNConfigSet configs) {
    }

    /**
     * @changed OLI 26.09.2017 - Added.
     */
    @Override public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
            int line, int charPositionInLine, String msg, RecognitionException e) {
        StringBuffer sb = new StringBuffer("Syntax error at line ").append(line).append(":"
                ).append(charPositionInLine).append(" - ").append(msg);
        this.errors.add(sb.toString());
    }

}