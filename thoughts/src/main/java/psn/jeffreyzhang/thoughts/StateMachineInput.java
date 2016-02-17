package psn.jeffreyzhang.thoughts;

import java.util.Random;

public enum StateMachineInput {
    NICKEL(5), DIME(10), QUARTER(25), DOLLAR(100),
    TOOTHPASTE(200), CHIPS(75), SODA(100), SOAP(50),
    ABOUT_TRANSACTION {
        @Override
        int amount() {
            throw new RuntimeException("ABOUT_TRANSACTION");
        }
    },
    STOP {
        @Override
        int amount() {
            throw new RuntimeException("SHUT_DOWN.amount()");
        }
    };

    /**
     * 随机生成输入
     */
    static Random random = new Random();
    private int value;

    private StateMachineInput() {
    }

    private StateMachineInput(int value) {
        this.value = value;
    }

    public static StateMachineInput randomSelection() {
        /**
         * 产生 0 ~ values().length区间内的数 ， 包括0 不包括values().length
         */
        return values()[random.nextInt(values().length)];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(StateMachineInput.randomSelection());
        }
    }

    int amount() {
        return value;
    }
}