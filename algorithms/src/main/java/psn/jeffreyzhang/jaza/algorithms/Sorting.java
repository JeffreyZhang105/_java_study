package psn.jeffreyzhang.jaza.algorithms;

public class Sorting {

    public static <T extends Comparable<? super T>> void shellSorting(T[] input) {
        for (int gap = input.length / 2; gap > 0; gap /= 2) {
            for (int groupIdx = 0; groupIdx <= input.length / gap; groupIdx++) {
                //for(int itemIdx=)
            }
        }
    }
}