package jtg.generator;

import jtg.solver.Z3Solver;
import soot.Body;

import java.util.ArrayList;
import java.util.Arrays;

public class MCDCGenerator {
    public static ArrayList<PathConstraintInformation> translate(PathConstraintInformation constraint) {
        String logicalExpression = constraint.getPathConstraint();
        String mtdName=constraint.getMtdName();
        String clsName=constraint.getClsName();
        String[] logicalExpressionSplit = logicalExpression.split("&&|\\|\\|");
        Body body= constraint.getBody();
        for (int i = 0; i < logicalExpressionSplit.length; i++) {
            String s = logicalExpressionSplit[i].trim();
            if (s.contains("(") && !s.contains(")")) {
                s = s.substring(1);
            }
            if (s.contains(")") && !s.contains("(")) {
                s = s.substring(0, s.length() - 1);
            }
            logicalExpressionSplit[i] = s;
        }
        System.out.println("convertedLogicalExpressionSplit: " + Arrays.toString(logicalExpressionSplit));

        ArrayList<PathConstraintInformation> constraints = new ArrayList<>();
        for (int i = 0; i < logicalExpressionSplit.length; i++) {
            String expression = logicalExpressionSplit[i];
            String replaceWithTrue = "( " + logicalExpression.replace(expression, "1 == 1") + " )";
            String replaceWithFalse = "( " + logicalExpression.replace(expression, "1 == 0") + " )";

            String constraintStr1 = "( " + replaceWithTrue + " !|| " + replaceWithFalse + " )" + " && " + expression;
            PathConstraintInformation constraint1 = new PathConstraintInformation(constraintStr1, constraint.getExpectedResultConstraint(), constraint.getDictionary(),body,mtdName,clsName);
            expression = "!(" + expression + ")";
            String constraintStr2 = "( " + replaceWithTrue + " !|| " + replaceWithFalse + " )" + " && " + expression;
            PathConstraintInformation constraint2 = new PathConstraintInformation(constraintStr2, constraint.getExpectedResultConstraint(), constraint.getDictionary(),body,mtdName,clsName);
            constraints.add(constraint1);
            constraints.add(constraint2);
        }
        return constraints;
    }
}
