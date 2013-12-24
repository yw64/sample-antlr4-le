package com.wisetop.antlr4.le;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import com.wisetop.antlr4.le.LogicExprParser.StatContext;

public class LogicExprRewriterTest {

	@Test
	public void test() {
		String le = "(a) and (b) and ((c) or (d) and (e))";
		ANTLRInputStream input = new ANTLRInputStream(le);

		LogicExprLexer lexer = new LogicExprLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		LogicExprParser parser = new LogicExprParser(tokens);
		StatContext tree = parser.stat();

		LogicExprRewriter rewriter;
		String result;

		rewriter = new LogicExprRewriter(Arrays.asList("a", "b", "c", "d", "e"));
		result = rewriter.visit(tree);
		assertEquals("(a) and (b) and ((c) or (d) and (e))", result);

		rewriter = new LogicExprRewriter(Arrays.asList("a", "b", "d"));
		result = rewriter.visit(tree);
		assertEquals("(a) and (b) and ((d))", result);

		rewriter = new LogicExprRewriter(Arrays.asList("c", "d"));
		result = rewriter.visit(tree);
		assertEquals("((c) or (d))", result);

		List<String> emptyList = Collections.emptyList();
		rewriter = new LogicExprRewriter(emptyList);
		result = rewriter.visit(tree);
		assertNull(result);
	}

}
