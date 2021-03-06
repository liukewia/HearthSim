package com.hearthsim.card;

import com.hearthsim.model.PlayerSide;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oyachai on 6/15/15.
 */
public enum CharacterIndex {

    // 但是，如果不小心修改了枚举的顺序，编译器是无法检查出这种逻辑错误的。要编写健壮的代码，就不要依靠ordinal()的返回值。
    // 因为enum本身是class，所以我们可以定义private的构造方法，并且，给每个枚举常量添加字段
    // assign value for all attributes https://www.cnblogs.com/mimosading/archive/2013/12/13/3472578.html
    HERO(0),
    MINION_1(1), // each of it is a CharacterIndex
    MINION_2(2),
    MINION_3(3),
    MINION_4(4),
    MINION_5(5),
    MINION_6(6),
    MINION_7(7),
    MINION_8(8),
    MINION_9(9),
    UNKNOWN(99);

    private final int index;
    // the 'index' that passes to the argument of the constructor is the HERO('index')
    CharacterIndex(int index) {
        this.index = index;
    }

    public static CharacterIndex fromInteger(int flag) {
        CharacterIndex type = intToTypeMap.get(flag);
        if (type == null)
            return CharacterIndex.UNKNOWN;
        return type;
    }

    public int getInt() {
        return index;
    }

    private static final Map<Integer, CharacterIndex> intToTypeMap = new HashMap<>();
    static {
        for (CharacterIndex type : CharacterIndex.values()) {
            intToTypeMap.put(type.index, type);
        }
    }

    public static class CharacterLocation extends Location<CharacterIndex> {

        public CharacterLocation(PlayerSide playerSide, CharacterIndex index) {
            super(playerSide, index);
        }

    }

    public CharacterIndex indexToLeft() {
        if (this == HERO)
            return UNKNOWN;
        return CharacterIndex.fromInteger(this.getInt() - 1);
    }

    public CharacterIndex indexToRight() {
        if (this == MINION_7)
            return UNKNOWN;
        return CharacterIndex.fromInteger(this.getInt() + 1);
    }
}
