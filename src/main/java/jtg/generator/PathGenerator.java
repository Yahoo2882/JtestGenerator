package jtg.generator;

import jtg.graphics.SootCFG;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PathGenerator {

    public static ArrayList<List<Unit>> simplePath(MethodInformation method) {


        ArrayList<List<Unit>> simplePathList = new ArrayList<>();

        ArrayList<List<Unit>> list = new ArrayList<>();


        Iterator<Unit> iterator = method.unitGraph().iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            ArrayList<Unit> temp = new ArrayList<>();
            temp.add(unit);
            //后继节点
            List<Unit> successor = method.unitGraph().getSuccsOf(unit);
            if (successor == null || successor.size() == 0) {
                simplePathList.add(temp);
            }
            else {
                list.add(temp);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            List<Unit> path = list.get(i);
            Unit lastNode = path.get(path.size() - 1);
            List<Unit> successor = method.unitGraph().getSuccsOf(lastNode);
            boolean allCase2dot2 = true;
            for (Unit node : successor) {
                ArrayList<Unit> extendedPath = new ArrayList<>(path);
                extendedPath.add(node);
                List<Unit> succ = method.unitGraph().getSuccsOf(node);
                if (succ == null || succ.size() == 0) {
                    allCase2dot2 = false;
                    simplePathList.add(extendedPath);
                }
                else {
                    if (path.contains(node)) {
                        if (node.equals(path.get(0))) {
                            allCase2dot2 = false;
                            simplePathList.add(extendedPath);
                        }
                        else {

                        }
                    }

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

        return simplePathList;
    }

    public static ArrayList<List<Unit>> primerPath(MethodInformation method) {
        ArrayList<List<Unit>> simplePathList = simplePath(method);

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

        basicPathList.sort((o1, o2) -> {
            return o2.size() - o1.size();
        });

        return basicPathList;
    }

    /**
     * 覆盖基路径的完整路径
     * @return
     */
    public static ArrayList<List<Unit>> completePrimerPath(MethodInformation method) {
        ArrayList<List<Unit>> completePathList = new ArrayList<>();
        ArrayList<List<Unit>> basicPathListCopy = primerPath(method);

        for (int i = 0; i < basicPathListCopy.size(); i++) {
            List<Unit> path = basicPathListCopy.get(i);
            if (path == null) {
                continue;
            }
            //扩展得到一条完整路径
            extendToHead(path, method);
            extendToTail(path, method);
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

        return completePathList;
    }

    /**
     * Path1 是否是 Path2 的子路径
     * @param path1
     * @param path2
     * @return
     */
    private static boolean isSubPath(List<Unit> path1, List<Unit> path2) {
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


    static boolean headTargetFound = false;

    private static void headBackTracking(List<Unit> path,
                                  List<Unit> addedNodes,
                                  MethodInformation method) {
        Unit firstNode = path.get(0);
        List<Unit> fronts = method.unitGraph().getPredsOf(firstNode);
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
                headBackTracking(path, addedNodes, method);
                if (headTargetFound) {
                    return;
                }
                path.remove(0);
                addedNodes.remove(addedNodes.size() - 1);
            }
        }
    }

    private static void extendToHead(List<Unit> path, MethodInformation method) {
        headBackTracking(path, new ArrayList<>(), method);
        headTargetFound = false;
    }

    static boolean tailTargetFound = false;

    private static void tailBackTracking(List<Unit> path,
                                  List<Unit> addedNodes,
                                  MethodInformation method) {
        Unit lastNode = path.get(path.size() - 1);
        List<Unit> successors = method.unitGraph().getSuccsOf(lastNode);
        if (successors == null || successors.size() == 0) {
            tailTargetFound = true;
            return;
        }
        for (Unit successor : successors) {
            if (tailTargetFound) {
                return;
            }
            if (!addedNodes.contains(successor)) {
                path.add(successor);
                addedNodes.add(successor);
                tailBackTracking(path, addedNodes, method);
                if (tailTargetFound) {
                    return;
                }
                path.remove(path.size() - 1);
                addedNodes.remove(addedNodes.size() - 1);
            }
        }
    }

    private static void extendToTail(List<Unit> path, MethodInformation method) {
        tailBackTracking(path, new ArrayList<>(), method);
        tailTargetFound = false;
    }

}
