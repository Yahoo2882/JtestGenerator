package cut;

public class TestSet {

    public int primitiveByte(byte op1, byte op2) {
        if (op1 > op2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int primitiveShort(short op1, short op2) {
        if (op1 > op2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int primitiveInt(int op1, int op2) {
        if (op1 > op2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int primitiveLong(long op1, long op2) {
        if (op1 > op2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int primitiveFloat(float op1, float op2) {
        if (op1 > op2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int primitiveDouble(double op1, double op2) {
        if (op1 > op2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int primitiveBoolean(boolean op1, boolean op2) {
        if (op1 && op2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int primitiveChar(char op1, char op2) {
        if (op1 == op2) {
            return 1;
        } else {
            return 0;
        }
    }

    // String
    public int paramString(String op1,int op2) {
        if (op1.length()==2&&op2==0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int paramArray(Integer[] nums) {
        if (nums != null && nums.length > 0) {
            return nums[0];
        }
        return 0;
    }

    public int paramEnum(Size op) {
        if (op == Size.MEDIUM) {
            return 1;
        }
        return 0;
    }

    public int paramClass(Person person) {
        System.out.println(person.toString());
        if (person.getAge() > 10) {
            return 1;
        } else {
            return 2;
        }
    }

    public int loopFor(int op) {
        for (int i = 0; i < 10; ++i) {
            ++op;
            if (5 == op) return 5;
        }
        return op;
    }

    public int loopWhile(int op1, int op2) {
        while (op1 > 0&&op1<99) {
            ++op1;
            ++op2;
        }
        return op2;
    }

    public String multipleIf(int op) {
        if (op % 15 == 0)
            return "Tom";
        else if (op % 5 == 0)
            return "Buzz";
        else if (op % 3 == 0)
            return "Fuze";
        else
            return Integer.toString(op);
    }

    // 纠错能力
    // 多写 ‘=’
    public int plusEqual(int op) {
        if (op >= 100) {
            return 1;
        } else if (op >= 98) {  // should be [op > 98]
            return 2;
        } else {
            return 3;
        }
    }

    //op1<=50
    public int lackCondition(int op1,int op2){
        if(op1<50||op2>40)
            return 1;
        return 2;
    }
    //覆盖率
    public int unReachable(int op1){
        if(op1>90)
            return 1;
        else if(op1>100)
            return 2;
        return 3;
    }
}
