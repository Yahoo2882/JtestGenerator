package jtg.generator;

import jtg.graphics.SootCFG;
import soot.Body;
import soot.Local;
import soot.Type;
import soot.toolkits.graph.UnitGraph;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RadomValueGenerator {

    private static String clsPath;
    private String clsName;
    private String mtdName;
    private UnitGraph ug;
    private Body body;


    public RadomValueGenerator(String classPath, String className, String methodName) {
        clsPath = classPath;
        clsName = className;
        mtdName = methodName;
        ug = SootCFG.getMethodCFG(clsPath, clsName, mtdName);
        body = SootCFG.getMethodBody(clsPath, clsName, mtdName);
    }

    public void Generator() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName(clsName);
        body = SootCFG.getMethodBody(clsPath, clsName, mtdName);
        Object instance = aClass.newInstance();
        List<Type> parameterTypes = body.getMethod().getParameterTypes();
        System.out.println("参数类型: " + parameterTypes);
        randomValue(body.getParameterLocals());

    }
    public String randomValue(List<Local> parameters) throws ClassNotFoundException {

        String varName;
        String varValue = "";
        String testinput = "";
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        Body body;
        int Index=0;
        Class c=Class.forName(clsName);
        Class[] params=Helper(c,mtdName).getParameterTypes();
        for (Local para : parameters) {
            varName = para.getName();
            //System.out.println(para.getType().toString());
            if ("int".equals(para.getType().toString())) {
                IntGenerator intGenerator=new IntGenerator();
                varValue=intGenerator.generate().toString();
                System.out.println("生成数据："+testinput + " " + varName + "=" + varValue);
            }
            else if("char".equals(para.getType().toString())){
                CharGenerator charGenerator=new CharGenerator();
                varValue=charGenerator.generateAlphaAndNumber().toString();
                System.out.println("生成数据："+testinput + " " + varName + "=" + varValue);
            }
            else if ("java.lang.String".equals(para.getType().toString())) {
                StringGenerator stringGenerator=new StringGenerator();
                varValue=stringGenerator.generateReadableString(4);
                System.out.println("生成数据："+testinput + " " + varName + "=" + varValue);
            }
            else if(para.getType().toString().contains(".")&&!(para.getType().toString().contains("java"))&&!params[Index].isEnum()){
                String temp=para.getType().toString();
                Class<?> aClass = Class.forName(temp);
                ClassGenerator classGenerator=new ClassGenerator(aClass);
                Object a =classGenerator.generate(null);
                System.out.println("生成数据：");
                System.out.println(a.toString());
            }
            else if("boolean".equals(para.getType().toString())){
                BooleanGenerator booleanGenerator=new BooleanGenerator();
                varValue=booleanGenerator.generate().toString();
                System.out.println("生成数据："+testinput + " " + varName + "=" + varValue);
            }
            else if(para.getType().toString().contains("[]")) {
                ArrayGenerator arrayGenerator=new ArrayGenerator(params[Index]);
                //arrayGenerator.generate(3);
                Object arr=arrayGenerator.generate(3);
                System.out.println("生成数据：");
                System.out.print("[");
                for(Object o:(Object[]) params[Index].cast(arr)){
                    System.out.print(o.toString()+" ");
                }
                System.out.println("]");
            }
            else {
                EnumerationGenerator enumerationGenerator=new EnumerationGenerator(params[Index]);
                System.out.println("生成数据："+enumerationGenerator.generate().toString());
            }
            Index+=1;
        }

        return testinput;
    }
    private Method Helper(Class c,String mtdName){
        for(Method method:c.getMethods()){
            if(method.getName().equals(mtdName))
                return method;
        }
        return null;
    }
}
