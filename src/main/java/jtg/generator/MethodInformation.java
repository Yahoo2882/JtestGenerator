package jtg.generator;

import jtg.graphics.SootCFG;
import soot.Body;
import soot.toolkits.graph.UnitGraph;

public class MethodInformation {
    private String clsPath;
    private String clsName;
    private String mtdName;
    private UnitGraph ug;
    private Body body;

    public MethodInformation(String classPath, String className, String methodName) {
        clsPath = classPath;
        clsName = className;
        mtdName = methodName;
        ug = SootCFG.getMethodCFG(clsPath, clsName, mtdName);
        body = SootCFG.getMethodBody(clsPath, clsName, mtdName);
    }

    public String classPath() {
        return clsPath;
    }

    public String className() {
        return clsName;
    }

    public String methodName() {
        return mtdName;
    }

    public UnitGraph unitGraph() {
        return ug;
    }

    public Body body() {
        return body;
    }
}
