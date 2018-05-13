package psn.jeffreyzhang.jaza.algorithms.sorting;

public class MergeSort {

	public static <T extends Comparable<? super T>> void mergeSort(T[] input) {
		T[] temp = (T[]) new Comparable[input.length];
		mergeSort(input, temp, 0, input.length);
	}

	static <T extends Comparable<? super T>> void mergeSort(T[] input, T[] temp, int left, int right) {
		if (left < right) {
			int center = (left + right) / 2;
			mergeSort(input, temp, left, center);
			mergeSort(input, temp, center + 1, right);
			merge(input, temp, left, center + 1, right);
		}
	}

	static <T extends Comparable<? super T>> void merge(T[] input, T[] temp, int left, int right, int rightEnd) {
		int leftEnd = right - 1;
		int tempPos = left;
		int numElements = right - leftEnd + 1;

		while (left <= leftEnd && right <= rightEnd) {
			if (input[left].compareTo(input[right]) < 0) {
				temp[tempPos++] = input[left++];
			} else {
				temp[tempPos++] = input[right++];
			}
		}

		while (left < leftEnd) {
			temp[tempPos++] = input[left++];
		}

		while (right < rightEnd) {
			temp[tempPos++] = input[right++];
		}

		for (int idx = 0; idx < numElements; idx++, rightEnd--) {
			input[rightEnd] = temp[rightEnd];
		}
	}
}
