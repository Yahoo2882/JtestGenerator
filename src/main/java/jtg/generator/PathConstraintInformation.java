package jtg.generator;

import soot.Body;

import java.util.HashMap;

public class PathConstraintInformation {
    private String pathConstraint;  // 不包含 jVars 的约束
    private String pathConstraintForZ3;  // z3 可读的约束
    private HashMap<String, String> dictionary;  // 存放 __Change_me__ 变量的真实值 与 参数的类型
    private String expectedResultConstraint;  // 解出 __Expected_result__ 的值
    private String expectedResultConstraintForZ3;
    private Body body;
    private String mtdName;
    private String clsName;

    public PathConstraintInformation(String constraint,
                                     String expectedResultConstraint,
                                     HashMap<String, String> dict,
                                     Body body,
                                     String mtdName,
                                     String clsName) {
        this.pathConstraint = constraint;
        this.expectedResultConstraint = expectedResultConstraint;
        this.expectedResultConstraintForZ3 = "";
        pathConstraintForZ3 = "";
        dictionary = dict;
        this.body=body;
        this.mtdName=mtdName;
        this.clsName=clsName;
    }

    public String getPathConstraint() {
        return pathConstraint;
    }

    public HashMap<String, String> getDictionary() {
        return dictionary;
    }

    public String getPathConstraintForZ3() { return pathConstraintForZ3; }

    public void setPathConstraintForZ3(String z3Constraint) {
        pathConstraintForZ3 = z3Constraint;
    }

    public String getExpectedResultConstraint() {
        return expectedResultConstraint;
    }

    public void setExpectedResultConstraint(String expectedResultConstraint) {
        this.expectedResultConstraint = expectedResultConstraint;
    }

    public String getExpectedResultConstraintForZ3() {
        return expectedResultConstraintForZ3;
    }

    public void setExpectedResultConstraintForZ3(String expectedResultConstraintForZ3) {
        this.expectedResultConstraintForZ3 = expectedResultConstraintForZ3;
    }

    public Body getBody() {
        return body;
    }
    public String getMtdName(){
        return mtdName;
    }
    public String getClsName(){
        return clsName;
    }
}
