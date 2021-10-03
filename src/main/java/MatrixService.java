import java.util.concurrent.atomic.AtomicInteger;

public class MatrixService {

    /**
     * Iterates over each column and calculates sum of elements
     */
    private static class ColumnSummator implements Runnable {

        private int fromColumn;
        private int toColumn;
        private int[][] matrix;
        private int resultId;
        private int[] result;
        private AtomicInteger syncObject;

        /**
         * Constructor
         *
         * @param fromColumn - column index start with
         * @param toColumn   - to column index. You should process columns strong before column with index toColumn
         * @param matrix     - matrix
         * @param resultId   - position of result in result array
         * @param result     - result array
         * @param syncObject - object for synchronization between threads
         */
        public ColumnSummator(int fromColumn, int toColumn, int[][] matrix, int resultId, int[] result, AtomicInteger syncObject) {
            // should be implemented
        }

        @Override
        public void run() {
            // should be implemented
        }
    }

    /**
     * Get sum of matrix elements. You should parallel work between several threads
     *
     * @param matrix   - matrix
     * @param nthreads - threads count. It is guarantee that number of matrix column is greater than nthreads.
     * @return sum of matrix elements
     */
    public int sum(int[][] matrix, int nthreads) throws InterruptedException {

        int[] result = new int[nthreads];
        AtomicInteger syncObject = new AtomicInteger(nthreads);

        // create threads and divide work between them
        // should be implemented

        int sum = 0;
        for (int threadResult : result) {
            sum += threadResult;
        }
        return sum;
    }
}
