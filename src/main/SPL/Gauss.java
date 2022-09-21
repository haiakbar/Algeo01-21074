package main.SPL;

import main.SPL.errors.InfinitySolutionException;
import main.SPL.errors.NoSolutionException;
import main.SPL.utils.Transformers;
import main.matrix.MatrixAugmented;
import main.matrix.errors.NotMatrixSquareException;

public class Gauss {
    public static double[] solve(MatrixAugmented matrix) throws NotMatrixSquareException, NoSolutionException, InfinitySolutionException {
        Integer[] removedIdx = Transformers.removeUnnecesaryVariable(matrix);
        operation(matrix);

        // selesaikan solusi SPL dari matriks eselon
        for (int i = matrix.getRowCount() - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                matrix.addRow(j, i, matrix.getOriginal().getMatrix()[j][i]);
            }
        }

        // buat array hasil
        return Transformers.fillResultWithZero(matrix.getAugmentation().transpose().getMatrix()[0], removedIdx);
    }

    /*
     * lakukan operasi gauss sehingga terbentuk matriks eselon
     */
    public static void operation(MatrixAugmented matrix) throws NotMatrixSquareException, NoSolutionException {
        double determinant = matrix.getOriginal().getDeterminant();

        if (determinant == 0) {
            throw new NoSolutionException("Tidak terdapat solusi SPL karena determinan nol");
        }

        // Sort matrix agar kolom ke-n pada baris ke-n merupakan nilai terbesar di antara kolom ke-n pada semua baris
        for (int i = 0; i < matrix.getOriginal().getColumnCount(); i++) {
            int maxRowIdx = matrix.getColMaxIndex(i, i, matrix.getRowCount() - 1);
            if (maxRowIdx != -1) {
                matrix.swapRow(i, maxRowIdx);
            }
        }

        // Untuk baris ke-n, lakukan operasi pada baris sehingga kolom ke-n pada baris lain bernilai nol
        for (int i = 0; i < matrix.getRowCount(); i++) {
            // Sebelum baris matriksnya dipasangkan, terlebih dahulu bagi barisnya sehingga kolom ke-n pada baris ke-n
            // bernilai 1
            double divider = matrix.getOriginal().getMatrix()[i][i];
            matrix.multiplyRow(i, 1d / divider);

            for (int j = i + 1; j < matrix.getRowCount(); j++) {
                double multiplier = matrix.getOriginal().getMatrix()[j][i];

                matrix.addRow(j, i, -1d * multiplier);
            }
        }
    }
}
