package psn.jeffreyzhang.jaza.algorithms.sorting;

public class QuickSort {

    public static <T extends Comparable<? super T>> void quickSort(T[] input) {
        quickSort(input, 0, input.length - 1);
    }

    static <T extends Comparable<? super T>> void quickSort(T[] arr, int head, int tail) {
        if (head >= tail || arr == null || arr.length <= 1) {
            return;
        }
        int i = head, j = tail;
        T pivot = arr[(head + tail) / 2];
        while (i <= j) {
            while (arr[i].compareTo(pivot) <= 0) {
                ++i;
            }
            while (arr[i].compareTo(pivot) > 0) {
                --j;
            }
            if (i < j) {
                T t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
                ++i;
                --j;
            } else if (i == j) {
                ++i;
            }
        }
        quickSort(arr, head, j);
        quickSort(arr, i, tail);
    }
}
