package jtg.generator;

import org.junit.jupiter.api.Test;
import soot.Unit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PathConstraintGeneratorTest {

    @Test
    void getPathConstraint() throws Exception {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "multipleIf";
        //System.out.println(clspath);

        MethodInformation method = new MethodInformation(clspath, clsName, methodName);
        String res = "";
        ArrayList<String> fianlAns = new ArrayList<String>();
        double sum = 0;
        double sub = 0;
        ArrayList<List<Unit>> paths = PathGenerator.completePrimerPath(method);
        for (List<Unit> path : paths) {
            PathConstraintInformation pathConstraintInformation = PathConstraintGenerator.getPathConstraint(path, method);
            if (!(pathConstraintInformation.getPathConstraint() == null)) {
                System.out.println(pathConstraintInformation.getPathConstraint());
                System.out.println(pathConstraintInformation.getDictionary());
                System.out.println(pathConstraintInformation.getExpectedResultConstraint());
                String ans = z3Generator.Solver1(pathConstraintInformation);
                if (!ans.isEmpty()) {
                    fianlAns.add(ans);
                    sub += 1;
                }
                sum += 1;
            }
        }
        System.out.println("input:");
        for (int i = 0; i < fianlAns.size(); i++) {
            System.out.println("(" + (i + 1) + ")" + " " + fianlAns.get(i));
        }
        System.out.println("基路径覆盖率为：" + sub / sum);
    }
    @Test
    void translate() throws Exception {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "multipleIf";
        MethodInformation method = new MethodInformation(clspath, clsName, methodName);
        ArrayList<String> fianlAns=new ArrayList<String>();
        ArrayList<List<Unit>> paths = PathGenerator.completePrimerPath(method);
        for (List<Unit> path : paths) {
            PathConstraintInformation pathConstraintInformation =
                    PathConstraintGenerator.getPathConstraint(path, method);
            System.out.println(pathConstraintInformation.getPathConstraint());
            System.out.println(pathConstraintInformation.getDictionary());
            ArrayList<PathConstraintInformation> infos = MCDCGenerator.translate(pathConstraintInformation);
            System.out.println("After MCDC");
            for (PathConstraintInformation info : infos) {
                System.out.println(info.getPathConstraint());
                String ans=z3Generator.Solver1(info);
                if(!ans.isEmpty()){
                    fianlAns.add(ans);
                }
            }
        }
        System.out.println("input:");
        for(int i=0;i<fianlAns.size();i++){
            System.out.println("("+(i+1)+")"+" "+fianlAns.get(i));
        }
    }

    private void func(String str) {
        str = "apple";
    }

    @Test
    void testRef() {
        String str = "world";
        str = str.replace("or", "a");
        System.out.println(str);
    }

    @Test
    void testReplacement() {
        String str = "";
        System.out.println(str.isEmpty());
    }

    @Test
    void testRegex() {
        String string = "virtualinvoke r0.<cut.CallClass: int num()>()";
        Matcher matcher = Pattern.compile(
                "(?<=(.{0,1024}invoke.{0,1024}<.{0,1024}>\\())(.+?)(?=\\)$)"
        ).matcher(string);

        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }

    @Test
    void testGetVariableInStatement() {
        String putIn = "(i0 + 5) * __Change_me__r5 + $c8 + (virtualinvoke r0.<>())(i3, i2)";
//        ArrayList<String> answer = PathConstraintGenerator.getVariableInStatement(putIn);
//        System.out.println(answer);
    }
}