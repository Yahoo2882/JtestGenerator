package jtg.CoverageGenerator;

import jtg.generator.*;
import jtg.solver.Z3Solver;
import org.junit.jupiter.api.Test;
import soot.Unit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoverageGenerator {

    private String clspath;
    private String clsName;
    private String methodName;

    public CoverageGenerator(String clspath, String clsName, String methodName) throws Exception {
        this.clsName=clsName;
        this.clspath=clspath;
        this.methodName=methodName;
    }

    public void pathConstrainGenerator() throws Exception {
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
                //System.out.println(pathConstraintInformation.getExpectedResultConstraint());
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

    public void MCDCGenerator() throws Exception {
        double sum = 0;
        double sub = 0;
        MethodInformation method = new MethodInformation(clspath, clsName, methodName);
        ArrayList<String> fianlAns=new ArrayList<String>();
        ArrayList<List<Unit>> paths = PathGenerator.completePrimerPath(method);
        z3Generator z3generator=new z3Generator();
        for (List<Unit> path : paths) {
            PathConstraintInformation pathConstraintInformation =
                    PathConstraintGenerator.getPathConstraint(path, method);
            System.out.println(pathConstraintInformation.getPathConstraint());
            System.out.println(pathConstraintInformation.getDictionary());
            ArrayList<PathConstraintInformation> infos = MCDCGenerator.translate(pathConstraintInformation);
            System.out.println("After MCDC");
            for (PathConstraintInformation info : infos) {
                sum++;
                System.out.println(info.getPathConstraint());
                String ans=z3generator.Solver2(info);
                if(!ans.isEmpty()){
                    sub++;
                    int flag=1;
                    for(int i=0;i<fianlAns.size();i++){
                        if(ans.equals(fianlAns.get(i)))
                            flag=0;
                    }
                    if(flag==1)
                        fianlAns.add(ans);
                }
            }
        }
        System.out.println("input:");
        for(int i=0;i<fianlAns.size();i++){
            System.out.println("("+(i+1)+")"+" "+fianlAns.get(i));
        }
        System.out.println("MCDC覆盖率："+sub/sum);
    }
    public void path(){
        MethodInformation method = new MethodInformation(clspath, clsName, methodName);
        String res = "";
        ArrayList<String> fianlAns = new ArrayList<String>();
        double sum = 0;
        double sub = 0;
        ArrayList<List<Unit>> paths = PathGenerator.completePrimerPath(method);
        for (List<Unit> path : paths) {
            System.out.println(paths);
        }
    }
}