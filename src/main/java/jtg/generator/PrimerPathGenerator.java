package jtg.generator;

import jtg.graphics.SootCFG;
import jtg.solver.Z3Solver;
import jtg.visualizer.Visualizer;
import soot.*;
import soot.jimple.internal.*;
import soot.toolkits.graph.UnitGraph;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class PrimerPathGenerator {

    private String clsPath;
    private String clsName;
    private String mtdName;
    private UnitGraph ug;
    private Body body;

    ArrayList<Local> paras = new ArrayList<Local>();

    public PrimerPathGenerator(String classPath, String className, String methodName) {
        clsPath = classPath;
        clsName = className;
        mtdName = methodName;
        ug = SootCFG.getMethodCFG(clsPath, clsName, mtdName);
        body = SootCFG.getMethodBody(clsPath, clsName, mtdName);
    }

    private List<Local> getJVars() {
        ArrayList<Local> jimpleVars = new ArrayList<Local>();
        for (Local l : body.getLocals()) {
            if (l.toString().startsWith("$")) jimpleVars.add(l);
        }
        return jimpleVars;
    }

    private List<Local> getParameters() {
        ArrayList<Local> paras = new ArrayList<Local>();
        for (Local para : body.getParameterLocals()) {
            paras.add(para);
        }
        return paras;
    }

    private List<Local> getLocalVars() {
        List<Local> parameters = getParameters();
        List<Local> jVars = getJVars();
        ArrayList<Local> localVars = new ArrayList<Local>();
        for (Local l : body.getLocals()) {
            if (!parameters.contains(l) && !jVars.contains(l)) {
                localVars.add(l);
            }
        }
        return localVars;
    }

    public static String myToString(Object[] args) {
        String res = "[";
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof int[]) {
                int[] temp = (int[]) arg;
                res += Arrays.toString(temp);
            } else {
                res += arg.toString();
            }
            if (i != args.length - 1) {
                res += ", ";
            }
        }
        res += "]";
        return res;
    }

    public List<List<Object[]>> generate() throws Exception {


        ArrayList<List<Unit>> simplePathList = new ArrayList<>();

        ArrayList<List<Unit>> list = new ArrayList<>();

        Iterator<Unit> iterator = ug.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            ArrayList<Unit> temp = new ArrayList<>();
            temp.add(unit);
            //后继节点
            List<Unit> successor = ug.getSuccsOf(unit);
            //case1:无后继
            if (successor == null || successor.size() == 0) {
                simplePathList.add(temp);
            }
            //case2:有后继
            else {
                list.add(temp);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            List<Unit> path = list.get(i);
            Unit lastNode = path.get(path.size() - 1);
            List<Unit> successor = ug.getSuccsOf(lastNode);
            boolean allCase2dot2 = true;
            for (Unit node : successor) {
                ArrayList<Unit> extendedPath = new ArrayList<>(path);
                extendedPath.add(node);
                List<Unit> succ = ug.getSuccsOf(node);
                //case1:
                if (succ == null || succ.size() == 0) {
                    allCase2dot2 = false;
                    simplePathList.add(extendedPath);
                }
                //case2:
                else {
                    if (path.contains(node)) {
                        //case2.1:
                        if (node.equals(path.get(0))) {
                            allCase2dot2 = false;
                            simplePathList.add(extendedPath);
                        }
                        //case2.2:
                        else {
                            //忽略
                        }
                    }
                    //case2.3:
                    else {
                        allCase2dot2 = false;
                        list.add(extendedPath);
                    }
                }
            }
            if (allCase2dot2) {
                simplePathList.add(path);
            }
        }

        System.out.println(simplePathList);
        System.out.println(simplePathList.size());

        ArrayList<List<Unit>> basicPathList = new ArrayList<>();
        for (int i = 0; i < simplePathList.size(); i++) {
            List<Unit> path = simplePathList.get(i);
            boolean isBasic = true;
            for (int j = 0; j < simplePathList.size(); j++) {
                if (i == j) {
                    continue;
                }
                List<Unit> anotherPath = simplePathList.get(j);
                if (isSubPath(path, anotherPath)) {
                    isBasic = false;
                    break;
                }
            }
            if (isBasic) {
                basicPathList.add(path);
            }
        }

        System.out.println(basicPathList);
        System.out.println(basicPathList.size());

        basicPathList.sort((o1, o2) -> {
            return o2.size() - o1.size();
        });

        ArrayList<List<Unit>> completePathList = new ArrayList<>();
        ArrayList<List<Unit>> basicPathListCopy = new ArrayList<>(basicPathList);

        for (int i = 0; i < basicPathListCopy.size(); i++) {
            List<Unit> path = basicPathListCopy.get(i);
            if (path == null) {
                continue;
            }
            extendToHead(path);
            extendToTail(path);
            completePathList.add(path);
            for (int j = 0; j < basicPathListCopy.size(); j++) {
                List<Unit> anotherPath = basicPathListCopy.get(j);
                if (anotherPath == null) {
                    continue;
                }
                if (isSubPath(anotherPath, path)) {
                    basicPathListCopy.set(j, null);
                }
            }
        }

        System.out.println(completePathList);
        System.out.println(completePathList.size());

        ArrayList<String> res = new ArrayList<>();

        HashSet<List<Unit>> coveredBasicPathList = new HashSet<>();

        for (List<Unit> path : completePathList) {

            HashSet<List<Unit>> thisCoveredBasicPath = new HashSet<>();
            for (List<Unit> path2 : basicPathList) {
                if (isSubPath(path2, path)) {
                    thisCoveredBasicPath.add(path2);
                }
            }

            //得到路径约束
            String pathConstraint = calPathConstraint(path);
            System.out.println("约束为：" + pathConstraint);

            if (pathConstraint.isEmpty()) {
                System.out.println("求出的解：" + randomTC(body.getParameterLocals()));
                res.add(randomTC(body.getParameterLocals()));
                coveredBasicPathList.addAll(thisCoveredBasicPath);
            }

            if (!pathConstraint.isEmpty()) {
                String solve = solve(pathConstraint);
                System.out.println("求出的解：" + solve);
                if (!solve.isEmpty()) {
                    coveredBasicPathList.addAll(thisCoveredBasicPath);
                    res.add(solve);
                }
            }


        }

        System.out.println("res: " + res);
        Class<?> aClass = Class.forName(clsName);
        Object instance = aClass.newInstance();
        List<Type> parameterTypes = body.getMethod().getParameterTypes();
        int parameterCount = body.getMethod().getParameterCount();
        Class[] classes = new Class[parameterCount];
        int ptr = 0;
        for (Type type : parameterTypes) {
            String typeName = type.toString();
            if (typeName.equals("int")) {
                classes[ptr++] = int.class;
                continue;
            }
            classes[ptr++] = Class.forName(typeName);
        }
        Method method = aClass.getMethod(mtdName, classes);

        List<Object[]> argList = new ArrayList<>();
        List<Object[]> expectList = new ArrayList<>();

        if (!res.isEmpty()) {
            System.out.println("");
            System.out.println("The generated test case inputs:");

            for (int i = 0; i < res.size(); i++) {
                String tc = res.get(i);

                for (Local local : paras) {
                    String name = local.getName();
                    if (!tc.contains(name)) {
                        if (name.contains("r")) {
                            tc = tc + " " + name + "_?=?";
                        } else {
                            tc = tc + " " + name + "=?";
                        }
                    }
                }

                Object[] args = new Object[parameterCount];
                Object[] args2 = new Object[parameterCount];

                int baseCount = -1;
                int refCount = -1;

                //逐个参数考虑
                for (int j = 0; j < parameterCount; j++) {
                    //Student
                    Class parameterType = classes[j];
                    //System.out.println("parameterType: " + parameterType);
                    List<String> conditionByIndex;
                    if (parameterType.toString().equals("int")) {
                        baseCount++;
                        conditionByIndex = getConditionByIndex(parameterType, tc, baseCount);
                    } else {
                        refCount++;
                        conditionByIndex = getConditionByIndex(parameterType, tc, refCount);
                    }
                    Object randomValue = getValue(parameterType, conditionByIndex);
                    Object randomValue2 = getValue(parameterType, conditionByIndex);
                    args[j] = randomValue;
                    args2[j] = randomValue2;
                }

                argList.add(args);

                Object expected = method.invoke(instance, args2);
                expectList.add(new Object[]{expected});
                if (expected == null) {
                    System.out.println("( " + (i + 1) + " ) " + myToString(args));
                } else if (expected.getClass().getSimpleName().equals("int[]")) {
                    System.out.println("( " + (i + 1) + " ) " + myToString(args) );
                } else {
                    System.out.println("( " + (i + 1) + " ) " + myToString(args) );
                }


            }

        }


        ArrayList<List<Object[]>> finalRes = new ArrayList<>();
        finalRes.add(argList);
        finalRes.add(expectList);

        return finalRes;
    }

    private List<String> getConditionByIndex(Class parameterType, String tc, int index) {

        ArrayList<String> res = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();

        //System.out.println("type: " + parameterType.toString());

        if (parameterType.toString().equals("int")) {
            tc = tc.trim();
            for (String condition : tc.split("\\s+")) {
                if (condition.contains("_")) {
                    continue;
                }
                int i = condition.indexOf("=");
                int number = Integer.valueOf(condition.substring(1, i));
                if (!list.contains(number)) {
                    list.add(number);
                }
            }
        } else {
            tc = tc.trim();
            for (String condition : tc.split("\\s+")) {
                if (!condition.contains("_")) {
                    continue;
                }
                int i = condition.indexOf("_");
                int number = Integer.valueOf(condition.substring(1, i));
                if (!list.contains(number)) {
                    list.add(number);
                }
            }
        }

        Collections.sort(list);
        if (index >= list.size()) {
            return null;
        }
        int targetNumber = list.get(index);

        if (parameterType.toString().equals("int")) {
            for (String condition : tc.split("\\s+")) {
                if (condition.contains("_")) {
                    continue;
                }
                int i = condition.indexOf("=");
                int number = Integer.valueOf(condition.substring(1, i));
                if (number == targetNumber) {
                    res.add(condition);
                }
            }
        } else {
            for (String condition : tc.split("\\s+")) {
                if (!condition.contains("_")) {
                    continue;
                }
                int i = condition.indexOf("_");
                int number = Integer.valueOf(condition.substring(1, i));
                if (number == targetNumber) {
                    res.add(condition);
                }
            }
        }

        return res;

    }

    public Object getValue(Class clz, List<String> condition) throws Exception {
//        System.out.println("=================");
//        System.out.println(clz.toString());
//        System.out.println(condition);
        if (clz.toString().equals("int")) {
            if (condition != null) {
                String cond = condition.get(0);
                if (!cond.contains("?")) {
                    String substring = cond.substring(cond.indexOf("=") + 1).trim();
                    return Integer.valueOf(substring);
                }
            }
            return 1;
        }
        if (clz.toString().equals("class [I")) {
            if (condition != null) {
                int length = 0;
                HashMap<Integer, Integer> hashMap = new HashMap<>();

                for (String cond : condition) {
                    if (cond.contains("?")) {
                        continue;
                    }
                    if (cond.contains("length")) {
                        length = Integer.valueOf(cond.substring(cond.indexOf("=") + 1));
                    } else {
                        Integer index = Integer.valueOf(cond.substring(cond.indexOf("_") + 1, cond.indexOf("=")));
                        Integer value = Integer.valueOf(cond.substring(cond.indexOf("=") + 1).trim());
                        hashMap.put(index, value);
                    }
                }

                int[] res = new int[length];
                for (int i = 0; i < res.length; i++) {
                    if (hashMap.containsKey(i)) {
                        res[i] = hashMap.get(i);
                    } else {
                        res[i] = 1;
                    }
                }

                return res;
            }
            return new int[0];
        }
        Field[] fields = clz.getFields();
        Class[] classes = new Class[fields.length];
        Object[] args = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class<?> fieldType = field.getType();
            classes[i] = fieldType;
            String fieldName = field.getName();
            boolean canStop = false;
            if (condition != null) {
                for (String cond : condition) {
                    if (cond.contains("?")) {
                        continue;
                    }
                    if (cond.contains(fieldName)) {
                        cond = cond.substring(cond.indexOf("=") + 1).trim();
                        //System.out.println("here: " + cond);
                        args[i] = Integer.valueOf(cond);
                        canStop = true;
                        break;
                    }
                }
            }
            if (canStop) continue;
            Object value = getValue(fieldType, null);
            args[i] = value;
        }
        //System.out.println(Helper.myToString(args));
        Constructor constructor = clz.getConstructor(classes);
        Object instance = constructor.newInstance(args);
        //System.out.println(instance);
        return instance;
    }

    public String calPathConstraint(List<Unit> path) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        List<Local> jVars = getJVars();
        List<Local> localVars = getLocalVars();

        String pathConstraint = "";
        String expectedResult = "";

        HashMap<String, String> assignList = new HashMap<>();
        ArrayList<String> stepConditions = new ArrayList<String>();

        HashMap<String, Type> nameToTypeMapper = new HashMap<>();


        for (Unit stmt : path) {
            System.out.println("-------------------------");

            System.out.println(stmt.getClass());
            System.out.println(stmt);



            if (stmt instanceof JIdentityStmt) {
                Value leftOp = ((JIdentityStmt) stmt).getLeftOp();
                Value rightOp = ((JIdentityStmt) stmt).getRightOp();

                assignList.put(
                        leftOp.toString(),
                        rightOp.toString()
                );
                continue;
            }

            //赋值语句

            if (stmt instanceof JAssignStmt) {
                String leftOpStr = ((JAssignStmt) stmt).getLeftOp().toString();
                String rightOpStr = ((JAssignStmt) stmt).getRightOp().toString();

                if (leftOpStr.contains(".")) {
                    leftOpStr = getTempName(leftOpStr);
                }
                if (rightOpStr.contains(".")) {
                    rightOpStr = getTempName(rightOpStr);
                }

                if (leftOpStr.contains("lengthof")) {
                    String varName = leftOpStr.trim().split(" ")[1];
                    leftOpStr = varName + "_length";
                }
                if (rightOpStr.contains("lengthof")) {
                    String varName = rightOpStr.trim().split(" ")[1];
                    rightOpStr = varName + "_length";
                }

                if (leftOpStr.contains("[")) {
                    String trim = leftOpStr.trim();
                    String varName = trim.substring(0, trim.indexOf("["));
                    String index = trim.substring(trim.indexOf("[") + 1, trim.indexOf("]"));
                    leftOpStr = varName + "_" + index;
                }
                if (rightOpStr.contains("[")) {
                    String trim = rightOpStr.trim();
                    String varName = trim.substring(0, trim.indexOf("["));
                    String index = trim.substring(trim.indexOf("[") + 1, trim.indexOf("]"));
                    rightOpStr = varName + "_" + index;
                }


                for (Local lv : jVars) {
                    if (rightOpStr.contains(lv.toString()) && assignList.containsKey(lv.toString())) {
                        System.out.println("here");
                        rightOpStr = rightOpStr.replace(lv.toString(), assignList.get(lv.toString()).trim());
                    }
                }

                for (Local lv : localVars) {
                    if (rightOpStr.contains(lv.toString()) && assignList.containsKey(lv.toString())) {
                        rightOpStr = rightOpStr.replace(lv.toString(), assignList.get(lv.toString()).trim());
                    }
                }


                System.out.println("left: " + leftOpStr);
                System.out.println("right: " + rightOpStr);
                assignList.put(
                        leftOpStr,
                        rightOpStr
                );
                continue;

            }

            if (stmt instanceof JGotoStmt) {
                JGotoStmt gotoStmt = (JGotoStmt) stmt;

            }

            //if语句
            if (stmt instanceof JIfStmt) {

                String ifstmt = ((JIfStmt) stmt).getCondition().toString();

                int nextUnitIndex = path.indexOf(stmt) + 1;
                Unit nextUnit = path.get(nextUnitIndex);

                if (!((JIfStmt) stmt).getTarget()
                        .equals(nextUnit))
                    ifstmt = "!( " + ifstmt + " )";
                else
                    ifstmt = "( " + ifstmt + " )";


                String cond = ifstmt;


                if (jVars.size() != 0) {
                    for (Local lv : jVars) {
                        if (cond.contains(lv.toString())) {
                            cond = cond.replace(lv.toString(), assignList.get(lv.toString()).trim());
                        }
                    }
                }

                //去除局部变量
                if (localVars.size() != 0) {
                    while (true) {
                        boolean canStop = true;
                        for (Local lv : localVars) {
                            if (cond.contains(lv.toString())) {
                                canStop = false;
                                cond = cond.replace(lv.toString(), "( " + assignList.get(lv.toString()).trim() + " )");
                            }
                        }
                        if (canStop) {
                            break;
                        }
                    }
                }
                stepConditions.add(cond);

                continue;
            }

            if (stmt instanceof JReturnStmt) {
                expectedResult = stmt.toString().replace("return", "").trim();
            }

        }


        System.out.println("assignList = " + assignList);

        System.out.println("The step conditions: " + stepConditions);


        if (stepConditions.isEmpty())
            return "";

        pathConstraint = stepConditions.get(0);
        int i = 1;
        while (i < stepConditions.size()) {
            pathConstraint = pathConstraint + " && " + stepConditions.get(i);
            i++;
        }
        System.out.println("The path expression is: " + pathConstraint);
        return pathConstraint;
    }

    //使用 Z3 这个约束求解器，给定一个路径约束，返回满足条件的一组输入
    public String solve(String pathConstraint) throws Exception {
        return Z3Solver.solve(pathConstraint);
    }

    public String randomTC(List<Local> parameters) {

        String varName;
        String varValue = "";

        String testinput = "";

        //遍历每个参数
        for (Local para : parameters) {
            //参数名
            varName = para.getName();
            //如果是int型参数，设置一个随机数作为值
            if ("int".equals(para.getType().toString())) {
                varValue = String.valueOf((int) (Math.random() * 10));
            }
            //如果是String型参数，直接将值写死为'abc'
            if ("String".equals(para.getType().toString())) {
                varValue = "abc";
            }
            //其它的基本类型没写
            //...

            //拼接起来
            testinput = testinput + " " + varName + "=" + varValue;
        }
        return testinput;
    }

    //判断path1是不是path2的子路径
    private boolean isSubPath(List<Unit> path1, List<Unit> path2) {
        if (path2.size() < path1.size()) {
            return false;
        }
        for (int i = 0; i < path2.size(); i++) {
            if (path2.get(i).equals(path1.get(0))) {
                boolean isSub = true;
                for (int j = 0; j < path1.size(); j++) {
                    if (i + j >= path2.size()) {
                        isSub = false;
                        break;
                    }
                    if (!path1.get(j).equals(path2.get(i + j))) {
                        isSub = false;
                        break;
                    }
                }
                if (isSub) {
                    return true;
                }
            }
        }
        return false;
    }


    boolean headTargetFound = false;

    private void headBackTracking(List<Unit> path, List<Unit> addedNodes) {
        Unit firstNode = path.get(0);
        List<Unit> fronts = ug.getPredsOf(firstNode);
        //找到起始节点了
        if (fronts == null || fronts.size() == 0) {
            headTargetFound = true;
            return;
        }
        for (Unit front : fronts) {
            if (headTargetFound) {
                return;
            }
            if (!addedNodes.contains(front)) {
                path.add(0, front);
                addedNodes.add(front);
                headBackTracking(path, addedNodes);
                if (headTargetFound) {
                    return;
                }
                path.remove(0);
                addedNodes.remove(addedNodes.size() - 1);
            }
        }
    }

    private void extendToHead(List<Unit> path) {
        headBackTracking(path, new ArrayList<>());
        headTargetFound = false;
    }

    boolean tailTargetFound = false;

    private void tailBackTracking(List<Unit> path, List<Unit> addedNodes) {
        //System.out.println("here");
        Unit lastNode = path.get(path.size() - 1);
        List<Unit> successors = ug.getSuccsOf(lastNode);
        //System.out.println(successors);
        //找到终止节点了
        if (successors == null || successors.size() == 0) {
            //System.out.println("done");
            tailTargetFound = true;
            return;
        }
        for (Unit successor : successors) {
            if (tailTargetFound) {
                return;
            }
            //没有在addedNodes中出现过的节点才考虑，避免进入死循环
            if (!addedNodes.contains(successor)) {
                path.add(successor);
                addedNodes.add(successor);
                tailBackTracking(path, addedNodes);
                if (tailTargetFound) {
                    return;
                }
                path.remove(path.size() - 1);
                addedNodes.remove(addedNodes.size() - 1);
            }
        }
    }

    private void extendToTail(List<Unit> path) {
        tailBackTracking(path, new ArrayList<>());
        tailTargetFound = false;
    }

    //把 'r0.<jtg.pojo.Student: int age>' 这个形式修改为 'r0_age'

    private String getTempName(String input) {
        String part1 = input.substring(0, input.indexOf('.'));
        String part2 = input.substring(input.lastIndexOf(' ') + 1, input.length() - 1);
        return part1 + "_" + part2;
    }

}

