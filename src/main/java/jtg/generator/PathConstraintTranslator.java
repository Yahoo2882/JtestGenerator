package jtg.generator;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import jtg.solver.ExpressionEvaluator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PathConstraintTranslator {

    public static PathConstraintInformation JimpleToZ3(PathConstraintInformation pathConstraint) {
        Set<String> declareBools = new HashSet<>();
        Set<Expr> varList = new HashSet<>();
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        String assertsCondition = "";  // 探测影响 boolean 的变量；转换成 z3 能计算的表达式；每个条件以 assert 标注
        String assertsExpectedResult = "";
        try {
            assertsCondition = expressionEvaluator.buildExpression(pathConstraint.getPathConstraint(), declareBools);
            assertsExpectedResult = expressionEvaluator.buildExpression(pathConstraint.getExpectedResultConstraint(), declareBools);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        pathConstraint.setPathConstraintForZ3(assertsCondition);
        pathConstraint.setExpectedResultConstraintForZ3(assertsExpectedResult);
        return pathConstraint;
    }

    public static String Z3ToFunctionParameter() {

        return null;
    }
}
