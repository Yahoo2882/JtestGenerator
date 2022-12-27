package jtg.generator;

import fj.P;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JReturnStmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathConstraintGenerator {

    public static PathConstraintInformation getPathConstraint(List<Unit> path, MethodInformation method) {

        String pathConstraint = "";
        String expectedResult = "";
        String expectedResultConstraint = "";
        Body body=method.body();
        String clsName=method.className();
        String mtdName=method.methodName();

        HashMap<String, String> assignList = new HashMap<>();
        HashMap<String, String> dictionary = new HashMap<>();
        ArrayList<String> stepConditions = new ArrayList<String>();

        int stmtIndex = 0;
        for (Unit stmt : path) {

            if (stmt instanceof JAssignStmt) {
                updateAssign((JAssignStmt)stmt, assignList);
            }
            if (stmt instanceof JIdentityStmt) {
                dictionary.put(((JIdentityStmt) stmt).getLeftOp().toString(),
                        ((JIdentityStmt) stmt).getRightOp().toString());
            }
            if (stmt instanceof JIfStmt) {

                String ifstms = ((JIfStmt) stmt).getCondition().toString();
                int nextUnitIndex = stmtIndex + 1;
                Unit nextUnit = path.get(nextUnitIndex);

                //如果ifstmt的后继语句不是ifstmt中goto语句，说明ifstmt中的条件为假
                if (!((JIfStmt) stmt).getTarget().equals(nextUnit))
                    ifstms = "!( " + ifstms + " )";
                else
                    ifstms = "( " + ifstms + " )";
                ifstms = replaceIfCondition(ifstms, assignList, dictionary);
                stepConditions.add(ifstms);
            }
            if (stmt instanceof JReturnStmt) {
                expectedResult = stmt.toString().replace("return", "").trim();
                expectedResultConstraint = replaceIfCondition(expectedResult, assignList, dictionary);
                expectedResultConstraint = "  __Expected_Result__ = "+expectedResultConstraint;

            }

            ++stmtIndex;
        }

        if (stepConditions.isEmpty())
            return new PathConstraintInformation(pathConstraint, expectedResultConstraint, dictionary,body,mtdName,clsName);
        pathConstraint = stepConditions.get(0);
        int i = 1;
        while (i < stepConditions.size()) {
            pathConstraint = pathConstraint + " && " + stepConditions.get(i);
            i++;
        }
        //System.out.println("The path expression is: " + pathConstraint);
        return new PathConstraintInformation(pathConstraint, expectedResultConstraint, dictionary,body,mtdName,clsName);
    }

    private static List<Local> getJVars(Body body) {
        //Jimple自身增加的Locals，不是被测代码真正的变量
        ArrayList<Local> jimpleVars = new ArrayList<Local>();
        for (Local l : body.getLocals()) {
            if (l.toString().startsWith("$")) jimpleVars.add(l);
        }
        return jimpleVars;
    }

    private static int getUnitIndex(Unit stmt, List<Unit> list) {
        int index = 0;
        Iterator<Unit> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (stmt == iterator.next()) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    private static boolean isChangeMe(String statement) {
        return
                isInvokeStmt(statement) || isClass(statement) || isStaticClassMember(statement);
    }

    private static boolean isInvokeStmt(String statement) {
        return
                statement.matches(".{0,1024}invoke.{0,1024}<.{0,1024}>\\(.{0,1024}\\)$");
    }

    public static boolean isClass(String statement) {
        return
                statement.matches("^(class) \".{0,1024}\"");
    }

    public static boolean isStaticClassMember(String statement) {
        return
                statement.matches("^<.{0,1024}>$");
    }

    private static String replaceIfCondition(String condition,
                                             HashMap<String, String> assignList,
                                             HashMap<String, String> dictionary) {
        if (condition == null || condition.isEmpty()) {
            return "";
        }

        ArrayList<String> variables = getVariableInStatement(condition);
        for (String variable : variables) {
            if (assignList.containsKey(variable)) {
                String replacement = assignList.get(variable);
                replacement = reinforceAssign(replacement, assignList, dictionary);
                if (isChangeMe(replacement)) {
                    // 重新命名：标记前缀 + 删去 '$' 后的默认名
                    String newName = "__Change_me__" + variable.substring(1);
                    replacement = replaceIfCondition(replacement, assignList, dictionary);
                    dictionary.put(newName, replacement);
                    condition = condition.replace(variable, newName);
                    continue;
                }
                condition = condition.replace(variable, "(" + replacement + ")");
            }
        }

        return condition;
    }

    private static ArrayList<String> getVariableInStatement(String statement) {
        // 因为函数名或者变量类型名很有可能包含符合变量格式的字串，首先屏蔽 <> 中的内容
        Matcher angleBracketMatcher = Pattern.compile("(?<=<).{0,1024}(?=>)").matcher(statement);
        if (angleBracketMatcher.find()) {
            String requireShielding = angleBracketMatcher.group();
            statement = statement.replace(requireShielding, "");
        }
        // 变量的格式为:
        // 1.字母开头，后面跟至少一个数字
        // 2.'$'开头，后面跟 1
        // 3. __Change_me__跟 1
        Matcher variableMatcher = Pattern.compile("[a-zA-Z$_]{1,1024}[0-9]{1,1024}").matcher(statement);
        ArrayList<String> variables = new ArrayList<>();
        while (variableMatcher.find()) {
            variables.add(variableMatcher.group());
        }
        return variables;
    }

    private static void updateAssign(JAssignStmt stmt, HashMap<String, String> assignList) {
        String left = stmt.getLeftOp().toString();
        String right = stmt.getRightOp().toString();
        ArrayList<String> variables = getVariableInStatement(right);
        for (String variable : variables) {
            if (assignList.containsKey(variable)) {
                String replacement = assignList.get(variable);
                if (isChangeMe(replacement)) {
                    continue;
                }
                right = right.replace(variable, "(" + replacement + ")");
            }
        }

        assignList.put(left, right);
    }

    private static String reinforceAssign(String constraint,
                                          HashMap<String, String> assignList,
                                          HashMap<String, String> dictionary) {
        ArrayList<String> variables = getVariableInStatement(constraint);
        if (variables.isEmpty()) {
            return constraint;
        }
        for (String variable : variables) {
            if (assignList.containsKey(variable)) {
                String replacement = assignList.get(variable);
                if (isChangeMe(replacement)) {
                    // 重新命名：标记前缀 + 删去 '$' 后的默认名
                    String newName = "__Change_me__" + variable.substring(1);
                    replacement = replaceIfCondition(replacement, assignList, dictionary);
                    dictionary.put(newName, replacement);
                    constraint = constraint.replace(variable, newName);
                    replacement = reinforceAssign(replacement, assignList, dictionary);
                    constraint = constraint.replace(variable, "(" + replacement + ")");
                    continue;
                }
            }
        }

        return constraint;
    }
}
