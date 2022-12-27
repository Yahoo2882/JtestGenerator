package cut;

import java.util.Arrays;

public class Person {
    private String name;
    private int age;
    private String[][] cards;
    private Key[][] keys;
    private Size clothesSize;

    public Person(String name,
                  int age,
                  String[][] cards,
                  Key[][] keys,
                  Size clothesSize) {
        this.name = name;
        this.age = age;
        this.cards = cards;
        this.keys = keys;
        this.clothesSize = clothesSize;
    }

    public String toString() {
        String info = "";
        info += "Name: " + name + "\n";
        info += "Age: " + Integer.toString(age) + "\n";
        if (cards != null && cards.length > 0) {
            info += "Cards: \n";
            for (String[] card : cards) {
                info += Arrays.toString(card) + "\n";
            }
        }
        if (keys != null && keys.length > 0) {
            info += "Keys: \n";
            for (Key[] key : keys) {
                for (Key k : key) {
                    info += Integer.toString(k.getId()) + " ";
                }
                info += "\n";
            }
        }
        info += "Clothes Size: " + clothesSize.toString() + "\n";
        return info;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }
}
