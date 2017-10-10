/**
 * Created by psnje on 2017/3/19.
 */
public class MaxSubSum {
    /**
     * @param input
     * @return
     */
    public static int getMaxSubSum(int[] input) {

        return maxSumRec(input, 0, input.length - 1);
    }

    private static int maxSumRec(int[] input, int left, int right) {
        if (left == right) {
            if (input[left] > 0) {
                return input[left];
            } else {
                return 0;
            }
        }

        int center = (left + right) / 2;
        int maxLeftSum = maxSumRec(input, left, center);
        int maxRightSum = maxSumRec(input, center + 1, right);

        int maxLeftBorderSum = 0, leftBorderSum = 0;
        //for(int i=center)


        return 0;
    }
}
