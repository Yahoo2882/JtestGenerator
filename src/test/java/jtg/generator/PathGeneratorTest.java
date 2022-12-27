package jtg.generator;

import org.junit.jupiter.api.Test;
import soot.Unit;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathGeneratorTest {

    @Test
    void simplePath() {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "oneDiamond";

        MethodInformation method = new MethodInformation(clspath, clsName, methodName);

        ArrayList<List<Unit>> ans = PathGenerator.simplePath(method);

        System.out.println(ans.size());
    }

    @Test
    void primerPath() {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "primerPathPPT3_42";

        MethodInformation method = new MethodInformation(clspath, clsName, methodName);

        ArrayList<List<Unit>> ans = PathGenerator.primerPath(method);

        for (List<Unit> path : ans) {
            System.out.println(path);
        }
    }

    @Test
    void completePrimerPath() {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "oneWhile";

        MethodInformation method = new MethodInformation(clspath, clsName, methodName);

        ArrayList<List<Unit>> ans = PathGenerator.completePrimerPath(method);

        for (List<Unit> path : ans) {
            System.out.println(path);
        }
    }

    @Test
    void showWhile() {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "oneWhile";

        MethodInformation method = new MethodInformation(clspath, clsName, methodName);

        ArrayList<List<Unit>> ans = PathGenerator.primerPath(method);
    }
}