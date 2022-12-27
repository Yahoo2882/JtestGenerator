package jtg.generator;

import soot.Body;
import soot.Local;

import java.lang.reflect.Method;
import java.util.*;
import java.lang.String;

import static jtg.solver.Z3Solver.solve;


public class z3Generator {

    public static String Solver1(PathConstraintInformation pathConstraintInformation) throws Exception {
        String constrain = pathConstraintInformation.getPathConstraint();
        //constrain=delete(constrain,'$');
        HashMap<String, String> dictionary = pathConstraintInformation.getDictionary();
        String ans = "";
        String finalRes="";
        String exp= pathConstraintInformation.getExpectedResultConstraint();
        ArrayList<Local> paras = new ArrayList<Local>();
        //ArrayList<String> finalRes = new ArrayList<>();
        Body body = pathConstraintInformation.getBody();
        for (Local para : body.getParameterLocals()) {
            paras.add(para);
        }
        if (!(constrain.equals(""))) {
            ans = solve(constrain);
            if (ans.isEmpty())
                System.out.println("求出的解：无解");
            else {
                System.out.println("求出的解：" + ans);
            }
        } else {
            System.out.println("求出的解：" + randomTC(body.getParameterLocals()));
        }

        if(!ans.isEmpty()){
            String[] res = ans.split("\\ ");
            for(int i=0,len=res.length;i<len;i++){
                if(res[i].contains("__Change_me__")){
                finalRes=finalRes+" "+generator(res[i],dictionary);
            }
            else {
                finalRes = finalRes +" "+res[i];
            }
        }
        for (Local para : body.getParameterLocals()){
            String para1=para.toString();
            if(!finalRes.contains(para1)&&dictionary.get(para1).contains("String"))
                finalRes=finalRes+para1+"="+"abc";
            else if (!finalRes.contains(para1)&&dictionary.get(para1).contains("int")) {
                finalRes=finalRes+" "+para1+"="+"1";
            }
            else if (!finalRes.contains(para1)&&dictionary.get(para1).contains("int[]")) {
                finalRes=finalRes+" "+para1+"="+"[1,1,1]";
            }
        }
        System.out.println("实际值："+finalRes);
        if(!exp.isEmpty()&&!exp.contains("me")&&!exp.contains("i")&&!exp.contains("r")){
            finalRes=finalRes+" "+exp;
        }
        else if(!exp.isEmpty()){
            for(int i=0,len=res.length;i<len;i++){
                String[] str=res[i].split("=");
                String temp=str[0];
                if(exp.contains("temp"))
                    exp=exp+"&&"+res[i];
            }
            exp=solve(exp);
            String[]  strs=exp.split(" ");
            for(int i=0,len=strs.length;i<len;i++){
                if(strs[i].contains("__Expected_Result__"))
                    finalRes=finalRes+"  "+strs[i];
            }
        }
        //System.out.println(exp);
        }
        /*System.out.println(randomTC(paras));*/
        return finalRes;
    }


    public static String generator(String change,HashMap<String,String> dictionary) throws Exception {
        String finalRes="";
        String res1;
        String subtemp1 = change.substring(0, 15);//_change__me_i0
        //some problem
        String subtemp2 = change.substring(change.lastIndexOf('=') + 1);//数字
        String cons="";
        //System.out.println(subtemp2);
        String s1=dictionary.get(subtemp1);
        int num=0;
        if (s1.contains("String") && s1.contains("length")) {
            for(String key:dictionary.keySet()){
                String value = dictionary.get(key);
                if (s1.contains(key)){
                    res1=key;
                    res1 = res1 + "=";
                    if(Integer.valueOf(subtemp2).intValue()==0)
                        res1=res1+"\"\"";
                    else {
                        StringGenerator stringGenerator=new StringGenerator();
                        res1=res1+stringGenerator.generateReadableString(Integer.valueOf(subtemp2).intValue());
                    }
                    /*if(res1.length()==0)
                        System.out.println("look here");*/
                    finalRes=finalRes+res1+" ";
                }
            }
        }

        if (s1.contains("virtualinvoke") && !s1.contains("length")) {
            String[] s21 = s1.split(".<");//后半段求值
            //String[] s2 = s1.split(">");//后半段求值
            //System.out.println(s21[0]);
            String[] s2=s21[0].toString().split(" ");
            System.out.println(s2[1]);
            //System.out.println(s2[0]);
            //System.out.println(s2[1] + "hi");
            s2[1] = s2[1] + "=" + subtemp2;

            //System.out.println(s2[1]);
            String ans1 = solve(s2[1]);
            //System.out.println(ans2);
            String[] ans2=ans1.split("\\ ");
            for (int i = 0, len = ans2.length; i < len; i++) {
                if(!ans2[i].contains("__Change_me__"))
                    finalRes=finalRes+ans2[i]+" ";
                else {
                    return finalRes+" "+generator(ans2[i],dictionary);
                }
            }
        }
        if (s1.contains("virtualinvoke") && !s1.contains("length")) {
            String[] s2 = s1.split(">");//后半段求值
            cons=cons+s2[1];
            }
        return finalRes;
    }
    public  String Solver2(PathConstraintInformation pathConstraintInformation) throws Exception {
        String clsName= pathConstraintInformation.getClsName();
        String mtdName= pathConstraintInformation.getMtdName();
        Method method=Helper(Class.forName(clsName),mtdName);
        Object[] answer=new Object[method.getParameterCount()];
        for(int i=0;i<answer.length;i++){
            answer[i]=0;
        }
        String constrain = pathConstraintInformation.getPathConstraint();
        //constrain=delete(constrain,'$');
        HashMap<String, String> dictionary = pathConstraintInformation.getDictionary();
        String ans = "";
        String finalRes="";
        String exp= pathConstraintInformation.getExpectedResultConstraint();
        ArrayList<Local> paras = new ArrayList<Local>();
        //ArrayList<String> finalRes = new ArrayList<>();
        Body body = pathConstraintInformation.getBody();
        for (Local para : body.getParameterLocals()) {
            paras.add(para);
        }
        if (!(constrain.equals(""))) {
            ans = solve(constrain);
            if (ans.isEmpty())
                System.out.println("求出的解：无解");
            else {
                System.out.println("求出的解：" + ans);
            }
        } else {
            System.out.println("求出的解：" + randomTC(body.getParameterLocals()));
        }

        if(!ans.isEmpty()){
            String[] res = ans.split("\\ ");
            for(int i=0,len=res.length;i<len;i++){
                //System.out.println(res[i]+"hello");
                if(res[i].contains("__Change_me__")){
                    finalRes=finalRes+generator(res[i],dictionary);
                }
                else {
                    finalRes = finalRes +" "+res[i];
                    String[] str=res[i].split("=");
                    int Index=dictionary.get(str[0]).charAt(10)-'0';
                    answer[Index]=Integer.valueOf(str[1]);
                }
            }
            for (Local para : body.getParameterLocals()){
                String para1=para.toString();
                if(!finalRes.contains(para1)&&dictionary.get(para1).contains("String"))
                    finalRes=finalRes+para1+"="+"abc";
                else if (!finalRes.contains(para1)&&dictionary.get(para1).contains("int")) {
                    finalRes=finalRes+" "+para1+"="+"1";
                }
                else if (!finalRes.contains(para1)&&dictionary.get(para1).contains("int[]")) {
                    finalRes=finalRes+" "+para1+"="+"[1,1,1]";
                }
            }
            System.out.println("实际值："+finalRes);
            //System.out.println("  __Expected_Result__ = "+(method.invoke(Class.forName(clsName).newInstance(),answer)));
        }
        return finalRes+"  __Expected_Result__ = "+(method.invoke(Class.forName(clsName).newInstance(),answer));
    }

    public static String randomTC(List<Local> parameters) {

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
    private Method Helper(Class c,String mtdName){
        for(Method method:c.getMethods()){
            if(method.getName().equals(mtdName))
                return method;
        }
        return null;
    }
}
