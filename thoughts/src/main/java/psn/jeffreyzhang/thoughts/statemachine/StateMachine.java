package psn.jeffreyzhang.thoughts.statemachine;

import java.util.EnumMap;
/**
 * 状态转换
 * <p>
 * 模拟自动售货机的运行
 */


/**
 * 该类是为了获得输入命令的类别
 */
enum Category {
    MONEY(StateMachineInput.NICKEL, StateMachineInput.DIME, StateMachineInput.QUARTER, StateMachineInput.DOLLAR),
    ITEM_SELECTION(StateMachineInput.TOOTHPASTE, StateMachineInput.CHIPS, StateMachineInput.SODA, StateMachineInput.SOAP),
    QUIT_TRANSACTION(StateMachineInput.ABOUT_TRANSACTION),
    SHUT_DOWN(StateMachineInput.STOP);
    private static EnumMap<StateMachineInput, Category> categories = new EnumMap<StateMachineInput, Category>(StateMachineInput.class);

    static {
        for (Category c : Category.class.getEnumConstants()) {
            for (StateMachineInput i : c.values) {
                categories.put(i, c);
            }
        }
    }

    private StateMachineInput[] values;

    private Category(StateMachineInput... inputs) {
        values = inputs;
    }

    public static Category getCategory(StateMachineInput input) {
        return categories.get(input);
    }
}

interface Generator<T> {
    T next();
}

public class StateMachine {
    /**
     * 模拟状态之间的转换
     *
     */
    private static int amount = 0;
    private static State state = State.RESTING;
    private static StateMachineInput selection = null;

    public static void run(Generator<StateMachineInput> gen) {
        /**
         * 如果在前面执行的命令中是state变为terminal，则程序结束。
         * 让其变为terminal的情况为输入的命令为STOP即SHUT_DOWN类别
         */
        while (state != State.TERMINAL) {
            /**
             * 输入随机产生命令 ， 当输入的命令非transaction类型的命令时，抛出异常 ，这在默认的next中设定
             * 这里的state不可能是TERMINAL，也不可能是Transaction（因为下面那个while无限循环），
             * 所以永远不会执行那个会抛出异常的next方法。
             */
            state.next(gen.next());
            /**
             * 判断该命令的剩下执行是否还需要命令，Transaction是true表示下面将要执行的任务不需要再输入命令
             * 所以使用无限循环将该命令执行完，然后再输出余额
             */
            while (state.isTransaction) {
                state.next();
            }
            state.output();
        }
    }

    public static void main(String[] args) {
        Generator<StateMachineInput> gen = new RandomInputGenerator();
        run(gen);
    }

    enum StateDuration {TRANSIENT}

    enum State {
        RESTING {
            void next(StateMachineInput input) {
                switch (Category.getCategory(input)) {
                    case MONEY:
                        amount += input.amount();
                        state = ADDING_MONEY;
                        break;
                    case SHUT_DOWN:
                        state = TERMINAL;
                        break;
                    default:
                }
            }
        },
        ADDING_MONEY {
            void next(StateMachineInput input) {
                switch (Category.getCategory(input)) {
                    case MONEY:
                        amount += input.amount();
                        /**
                         * 这里为什么不要设置state的值？
                         * 因为当前已经是ADDING_MONEY状态，设置了以后还是这个状态，所以不需要设置
                         */
                        break;
                    case ITEM_SELECTION:
                        selection = input;
                        if (amount < input.amount()) {
                            System.out.println("Insufficient money for " + selection);
                        } else {
                            state = DISPENSING;
                        }
                        break;
                    case QUIT_TRANSACTION:
                        state = GIVING_CHANGE;
                        break;
                    case SHUT_DOWN:
                        state = TERMINAL;
                        break;
                    default:
                }
            }
        },
        DISPENSING(StateDuration.TRANSIENT) {
            void next() {
                System.out.println("Here is your " + selection);
                amount -= selection.amount();
                state = GIVING_CHANGE;
            }
        },
        GIVING_CHANGE(StateDuration.TRANSIENT) {
            void next() {
                if (amount > 0) {
                    System.out.println("you change : " + amount);
                    amount = 0;
                }
                state = RESTING;
            }
        },
        TERMINAL {
            void output() {
                System.out.println("Halted!");
            }
        };
        private boolean isTransaction = false;

        State() {
        }

        State(StateDuration trans) {
            this.isTransaction = true;
        }

        void next() {

        }

        void next(StateMachineInput input) {
            throw new RuntimeException("Only call next(Input input) for non-transient states ");
        }

        void output() {
            System.out.println(amount);
        }
    }
}

class RandomInputGenerator implements Generator<StateMachineInput> {
    @Override
    public StateMachineInput next() {
        return StateMachineInput.randomSelection();
    }
}