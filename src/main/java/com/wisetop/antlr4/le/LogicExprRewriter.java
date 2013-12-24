package com.wisetop.antlr4.le;

import java.util.Collection;

import com.wisetop.antlr4.le.LogicExprParser.AndContext;
import com.wisetop.antlr4.le.LogicExprParser.GroupContext;
import com.wisetop.antlr4.le.LogicExprParser.OrContext;
import com.wisetop.antlr4.le.LogicExprParser.VarContext;

/**
 * ��д�߼����ʽ���߼����ʽ�����߼�������var�����߼���������{@code and}��{@code or}�����飨group�������Ҽ����ţ���ɵ��ַ�����
 * �����߼���������С�������������ַ��������ҳ���������������ַ���Ϊ�߼�����
 *
 * <p>���磬�߼����ʽ{@code (a) or (b) and (c)}����{@code (a)}��{@code (b)}��{@code (c)}�����߼�������
 * ���߼����ֱ���{@code a}��{@code b}��{@code c}��
 *
 * <p>���������߼������Ƿ���Ч����д�߼����ʽ�����磬
 * <p>�����߼����ʽ{@code (a) or (b) and (c)}��
 * <ul>
 * <li>����߼�����{@code (a)}��Ч��ת��Ϊ{@code (b) and (c)}��</li>
 * <li>����߼�����{@code (b)}��Ч��ת��Ϊ{@code (a) or (c)}��</li>
 * <li>����߼�����{@code (c)}��Ч��ת��Ϊ{@code (a) or (b)}��</li>
 * <li>����߼�����{@code (b)}��{@code (c)}����Ч��ת��Ϊ{@code (a)}��</li>
 * <li>������е��߼���������Ч��ת��Ϊ{@code null}��</li>
 * </ul>
 *
 * <p>Ҳ����˵�����һ��������Ч����Ҫɾ���ñ����Լ�����߻��ұߵĲ�����������Ҫɾ���ĸ���������Ҫ����������������
 * ���������У����ڱ���{@code (b)}�������������ұߵ�{@code (c)}���ϣ����ɾ��{@code (b)}ʱҪͬʱɾ����{@code (c)}�Ĳ�������
 * �����ս��Ϊ{@code (a) or (c)}�����磬����{@code ((a) or (b)) and (c)}����ɾ��{@code (b)}ʱҪͬʱɾ����{@code (a)}�Ĳ�������
 * ����Ϊ{@code ((a)) and (c)}��
 *
 * @author yangwu
 *
 */
public class LogicExprRewriter extends LogicExprBaseVisitor<String> {

	/** ��Ч���߼������� */
	private final Collection<String> validKeys;

	/**
	 * ����{@link LogicExprRewriter}����
	 *
	 * @param validKeys ��Ч���߼������ϣ����ڸü�����ļ�ֵ������Ч���߼�����
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
		// ȥ�����ҵ����š�����(a) => a
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
