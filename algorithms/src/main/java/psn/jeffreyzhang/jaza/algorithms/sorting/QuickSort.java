package psn.jeffreyzhang.jaza.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;

public class QuickSort {

    public static <T extends Comparable<? super T>> void basicQuickSort(List<T> input) {
        if (input.size() > 1) {
            List<T> larger = new ArrayList<T>();
            List<T> equal = new ArrayList<T>();
            List<T> smaller = new ArrayList<T>();

            T pivot = input.get(input.size() / 2);

            for (T item : input) {
                int diff = item.compareTo(pivot);

                if (diff > 0) {
                    larger.add(item);
                } else if (diff < 0) {
                    smaller.add(item);
                } else {
                    equal.add(item);
                }
            }

            basicQuickSort(larger);
            basicQuickSort(smaller);

            input.clear();
            input.addAll(smaller);
            input.addAll(equal);
            input.addAll(larger);
        }
    }

    public static <T extends Comparable<? super T>> void standQuickSort(T[] input) {
        standQuickSort(input, 0, input.length);
    }

    static <T extends Comparable<? super T>> void standQuickSort(T[] input, int from, int to) {
        T pivot = median3(input, from, to);
        int i = from, j = to - 1;

        while (true) {
            while (input[++i].compareTo(pivot) < 0) ;
            while (input[--j].compareTo(pivot) > 0) ;
            if (from < to) {
                swapElements(input, from, to);
            } else {
                break;
            }
        }

        standQuickSort(input, from, i);
        standQuickSort(input, i + 1, to);
    }

    static <T extends Comparable<? super T>> T median3(T[] input, int left, int right) {
        int center = (left + right) / 2;
        if (input[center].compareTo(input[left]) < 0) {
            swapElements(input, left, center);
        }
        if (input[right].compareTo(input[left]) < 0) {
            swapElements(input, right, left);
        }
        if (input[right].compareTo(input[center]) < 0) {
            swapElements(input, right, center);
        }

        swapElements(input, center, right - 1);
        return input[right - 1];
    }

    static <T extends Comparable<? super T>> void swapElements(T[] input, int left, int right) {
        T temp = input[left];
        input[left] = input[right];
        input[right] = temp;
    }
}
