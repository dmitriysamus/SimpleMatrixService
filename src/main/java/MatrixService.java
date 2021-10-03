import java.util.ArrayList;
import java.util.List;
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
            this.fromColumn = fromColumn;
            this.toColumn = toColumn;
            this.matrix = matrix;
            this.resultId = resultId;
            this.result = result;
            this.syncObject = syncObject;
        }

        @Override
        public void run() {
            while (fromColumn <= toColumn) {
                for(int i = 0; i < matrix.length; i++) {
                    result[resultId] += matrix[i][fromColumn];
                }
                ++fromColumn;
            }
            synchronized (syncObject) {
                if (syncObject.decrementAndGet() == 0) {
                    syncObject.notify();
                }
            }
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

        if (matrix.length == 0) {
            return 0;
        }

        int[] result = new int[nthreads];
        AtomicInteger syncObject = new AtomicInteger(nthreads + 1);
        List<Thread> threadList = new ArrayList<>();
        int fromColumn = 0;
        int resultId = 0;

        int partsCount;
        if (nthreads > matrix[0].length) {
            partsCount = matrix[0].length;
        } else {
            partsCount = matrix[0].length / nthreads;
        }

        int toColumn = fromColumn + partsCount - 1;

        for (int i = 0; i < nthreads; i++) {
            ColumnSummator columnSummator = new ColumnSummator(fromColumn, toColumn, matrix, resultId, result, syncObject);
            threadList.add(new Thread(columnSummator));

            fromColumn = toColumn + 1;
            ++resultId;

            if (matrix[0].length <= toColumn + matrix.length / partsCount) {
                toColumn = matrix[0].length - 1;
            } else {
                toColumn = toColumn + matrix.length / partsCount;
            };
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        synchronized (syncObject) {
            if (syncObject.decrementAndGet() != 0) {
                syncObject.wait();
            }
        }

        int sum = 0;
        for (int threadResult : result) {
            sum += threadResult;
        }

        return sum;
    }
}