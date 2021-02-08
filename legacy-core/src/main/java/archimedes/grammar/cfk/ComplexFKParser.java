// Generated from ComplexFK.g4 by ANTLR 4.7
package archimedes.grammar.cfk;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ComplexFKParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, Identifier=6;
	public static final int
		RULE_complexForeignKeyExpressions = 0, RULE_complexForeignKeyExpression = 1, 
		RULE_columnNameList = 2;
	public static final String[] ruleNames = {
		"complexForeignKeyExpressions", "complexForeignKeyExpression", "columnNameList"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'|'", "'->'", "'('", "','", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, "Identifier"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ComplexFK.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ComplexFKParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ComplexForeignKeyExpressionsContext extends ParserRuleContext {
		public List<ComplexForeignKeyExpressionContext> complexForeignKeyExpression() {
			return getRuleContexts(ComplexForeignKeyExpressionContext.class);
		}
		public ComplexForeignKeyExpressionContext complexForeignKeyExpression(int i) {
			return getRuleContext(ComplexForeignKeyExpressionContext.class,i);
		}
		public ComplexForeignKeyExpressionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexForeignKeyExpressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ComplexFKListener ) ((ComplexFKListener)listener).enterComplexForeignKeyExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ComplexFKListener ) ((ComplexFKListener)listener).exitComplexForeignKeyExpressions(this);
		}
	}

	public final ComplexForeignKeyExpressionsContext complexForeignKeyExpressions() throws RecognitionException {
		ComplexForeignKeyExpressionsContext _localctx = new ComplexForeignKeyExpressionsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_complexForeignKeyExpressions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			complexForeignKeyExpression();
			setState(11);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(7);
				match(T__0);
				setState(8);
				complexForeignKeyExpression();
				}
				}
				setState(13);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComplexForeignKeyExpressionContext extends ParserRuleContext {
		public List<ColumnNameListContext> columnNameList() {
			return getRuleContexts(ColumnNameListContext.class);
		}
		public ColumnNameListContext columnNameList(int i) {
			return getRuleContext(ColumnNameListContext.class,i);
		}
		public TerminalNode Identifier() { return getToken(ComplexFKParser.Identifier, 0); }
		public ComplexForeignKeyExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexForeignKeyExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ComplexFKListener ) ((ComplexFKListener)listener).enterComplexForeignKeyExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ComplexFKListener ) ((ComplexFKListener)listener).exitComplexForeignKeyExpression(this);
		}
	}

	public final ComplexForeignKeyExpressionContext complexForeignKeyExpression() throws RecognitionException {
		ComplexForeignKeyExpressionContext _localctx = new ComplexForeignKeyExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_complexForeignKeyExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			columnNameList();
			setState(15);
			match(T__1);
			setState(16);
			match(Identifier);
			setState(17);
			columnNameList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnNameListContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(ComplexFKParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(ComplexFKParser.Identifier, i);
		}
		public ColumnNameListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnNameList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ComplexFKListener ) ((ComplexFKListener)listener).enterColumnNameList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ComplexFKListener ) ((ComplexFKListener)listener).exitColumnNameList(this);
		}
	}

	public final ColumnNameListContext columnNameList() throws RecognitionException {
		ColumnNameListContext _localctx = new ColumnNameListContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_columnNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			match(T__2);
			setState(20);
			match(Identifier);
			setState(25);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(21);
				match(T__3);
				setState(22);
				match(Identifier);
				}
				}
				setState(27);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(28);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\b!\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\3\2\3\2\7\2\f\n\2\f\2\16\2\17\13\2\3\3\3\3\3\3\3\3\3\3"+
		"\3\4\3\4\3\4\3\4\7\4\32\n\4\f\4\16\4\35\13\4\3\4\3\4\3\4\2\2\5\2\4\6\2"+
		"\2\2\37\2\b\3\2\2\2\4\20\3\2\2\2\6\25\3\2\2\2\b\r\5\4\3\2\t\n\7\3\2\2"+
		"\n\f\5\4\3\2\13\t\3\2\2\2\f\17\3\2\2\2\r\13\3\2\2\2\r\16\3\2\2\2\16\3"+
		"\3\2\2\2\17\r\3\2\2\2\20\21\5\6\4\2\21\22\7\4\2\2\22\23\7\b\2\2\23\24"+
		"\5\6\4\2\24\5\3\2\2\2\25\26\7\5\2\2\26\33\7\b\2\2\27\30\7\6\2\2\30\32"+
		"\7\b\2\2\31\27\3\2\2\2\32\35\3\2\2\2\33\31\3\2\2\2\33\34\3\2\2\2\34\36"+
		"\3\2\2\2\35\33\3\2\2\2\36\37\7\7\2\2\37\7\3\2\2\2\4\r\33";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}