package jtg.generator;

public class CharGenerator extends BasicTypeGenerator {

    @Override
    public Character generate() {
        return (char)randomIntegerFromRange(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    /**
     * 仅生成 英文大小写字母，数字 0-9
     *
     * @return
     */
    public Character generateAlphaAndNumber() {
        // 0-9: [0x30,0x39]/[48, 57] - 10
        // A-Z: [0x41,0x5a]/[65, 90] - 26
        // a-z: [0x61,0x7a]/[97, 122] - 26
        int numberFrom0To61 = (int)randomIntegerFromRange(0, 61);
        if (0 <= numberFrom0To61 && numberFrom0To61 <= 9) {                 // 0-9
            return (char)(48 + numberFrom0To61);
        } else if (10 <= numberFrom0To61 && numberFrom0To61 <= 35) {        // A-Z
            return (char)(65 + numberFrom0To61 - 10);
        } else {                                                            // a-z
            return (char)(97 + numberFrom0To61 - 36);
        }
    }
}
