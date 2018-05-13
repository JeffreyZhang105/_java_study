package psn.jeffreyzhang.jaza.algorithms.sorting;

public class InsertSort {

	public static <T extends Comparable<? super T>> void insertionSort(T[] input) {
		for (int i = 1; i < input.length; i++) {
			T currentItem = input[i];

			int j = i - 1;
			while (j >= 0 && input[j].compareTo(currentItem) > 0) {
				input[j + 1] = input[j];
			}
			input[j] = currentItem;
		}
	}
}
