package jtg.generator;

import org.junit.jupiter.api.Test;
import soot.Unit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class PathConstraintTranslatorTest {

    @Test
    void translate() {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "callClass";

        MethodInformation method = new MethodInformation(clspath, clsName, methodName);

        ArrayList<List<Unit>> paths = PathGenerator.completePrimerPath(method);
        for (List<Unit> path : paths) {
            PathConstraintInformation pathConstraintInformation =
                    PathConstraintGenerator.getPathConstraint(path, method);
            PathConstraintTranslator.JimpleToZ3(pathConstraintInformation);
        }
    }
}