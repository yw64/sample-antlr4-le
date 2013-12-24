package com.wisetop.antlr4.le;

import java.util.Collection;

import com.wisetop.antlr4.le.LogicExprParser.AndContext;
import com.wisetop.antlr4.le.LogicExprParser.GroupContext;
import com.wisetop.antlr4.le.LogicExprParser.OrContext;
import com.wisetop.antlr4.le.LogicExprParser.VarContext;

/**
 * 重写逻辑表达式。逻辑表达式是由逻辑变量（var）、逻辑操作符（{@code and}、{@code or}）和组（group，即左右加括号）组成的字符串，
 * 其中逻辑变量是用小括号括起来的字符串，并且称这个被括起来的字符串为逻辑键。
 *
 * <p>例如，逻辑表达式{@code (a) or (b) and (c)}含有{@code (a)}、{@code (b)}和{@code (c)}三个逻辑变量，
 * 其逻辑键分别是{@code a}、{@code b}和{@code c}。
 *
 * <p>本对象将视逻辑变量是否有效来重写逻辑表达式。例如，
 * <p>对于逻辑表达式{@code (a) or (b) and (c)}，
 * <ul>
 * <li>如果逻辑变量{@code (a)}无效则转换为{@code (b) and (c)}。</li>
 * <li>如果逻辑变量{@code (b)}无效则转换为{@code (a) or (c)}。</li>
 * <li>如果逻辑变量{@code (c)}无效则转换为{@code (a) or (b)}。</li>
 * <li>如果逻辑变量{@code (b)}和{@code (c)}都无效则转换为{@code (a)}。</li>
 * <li>如果所有的逻辑变量都无效则转换为{@code null}。</li>
 * </ul>
 *
 * <p>也就是说，如果一个变量无效，则要删除该变量以及其左边或右边的操作符，至于要删除哪个操作符则要视其语义来决定。
 * 例如上例中，对于变量{@code (b)}，其语义是与右边的{@code (c)}相结合，因此删除{@code (b)}时要同时删除与{@code (c)}的操作符，
 * 其最终结果为{@code (a) or (c)}。又如，对于{@code ((a) or (b)) and (c)}，则当删除{@code (b)}时要同时删除与{@code (a)}的操作符，
 * 其结果为{@code ((a)) and (c)}。
 *
 * @author yangwu
 *
 */
public class LogicExprRewriter extends LogicExprBaseVisitor<String> {

	/** 有效的逻辑键集合 */
	private final Collection<String> validKeys;

	/**
	 * 创建{@link LogicExprRewriter}对象。
	 *
	 * @param validKeys 有效的逻辑键集合，仅在该集合里的键值才是有效的逻辑键。
	 */
	public LogicExprRewriter(Collection<String> validKeys) {
		this.validKeys = validKeys;
	}

	@Override
	public String visitOr(OrContext ctx) {
		String left = visit(ctx.expr(0));
		String right = visit(ctx.expr(1));

		if (left == null) {
			return (right == null) ? null : right;
		}
		else {
			return (right == null) ? left : left + " or " + right;
		}
	}

	@Override
	public String visitAnd(AndContext ctx) {
		String left = visit(ctx.expr(0));
		String right = visit(ctx.expr(1));

		if (left == null) {
			return (right == null) ? null : right;
		}
		else {
			return (right == null) ? left : left + " and " + right;
		}
	}

	@Override
	public String visitVar(VarContext ctx) {
		// 去掉左右的括号。例如(a) => a
		String key = ctx.getText().substring(1, ctx.getText().length() - 1);
		boolean valid = this.isValidKey(key);
		return (valid) ? ctx.getText() : null;
	}

	@Override
	public String visitGroup(GroupContext ctx) {
		String expr = visit(ctx.expr());
		return (expr == null) ? null : "(" + expr + ")";
	}

	private boolean isValidKey(String key) {
		return this.validKeys.contains(key);
	}

}
