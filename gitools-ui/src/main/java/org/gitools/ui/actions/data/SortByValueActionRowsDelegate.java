package org.gitools.ui.actions.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import org.gitools.aggregation.IAggregator;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.sort.SortCriteria;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;

public class SortByValueActionRowsDelegate {

	private static final long serialVersionUID = -1582437709508438222L;
	private IMatrix contents;
	private List<SortCriteria> criteriaList;

	public SortByValueActionRowsDelegate(IMatrixView matrixView, List<SortCriteria> criteriaList, boolean allRows) {
		
		this.contents = matrixView.getContents();
		this.criteriaList = criteriaList;
		
		//cols
		int colCount;
		int[] columns = null;
		if(matrixView.getSelectedColumns().length == 0) {
			colCount = matrixView.getVisibleColumns().length;
			columns = new int[colCount];
			for (int c = 0; c < colCount; c++)
				columns[c] = c;
		} else
			columns = matrixView.getSelectedColumns();

		//rows
		Integer[] rows = null;
		final int rowCount;
		if(allRows) {
			rowCount = contents.getRowCount();
			rows = new Integer[rowCount];
			for (int j = 0; j < rowCount; j++)
				rows[j] = j;
		}
		else {
			rowCount = matrixView.getRowCount();
			rows = new Integer[rowCount];
			int[] visibleRows = matrixView.getVisibleRows();
			for (int j = 0; j < rowCount; j++)
				rows[j] = visibleRows[j];
		}

		Integer[] sortedRowIndices = sortRows(columns, rows);
		int[] visibleSortedRows = new int[rowCount];
		for (int k = 0; k < rowCount; k++)
			visibleSortedRows[k] = sortedRowIndices[k];
		matrixView.setVisibleRows(visibleSortedRows);
	}
		

	private Integer[] sortRows(final int[] selection,
								final Integer[] indices) {
		
		final List<IAggregator> aggregators = new ArrayList<IAggregator>();
		final List<Integer> properties = new ArrayList<Integer>();
		final List<Integer> directions = new ArrayList<Integer>();
		
		final int criterias = criteriaList.size();
		
		for (int i = 0; i < criteriaList.size(); i++) {
			SortCriteria sortCriteria = criteriaList.get(i);
			
			properties.add(sortCriteria.getPropertyIndex());
			directions.add(sortCriteria.getDirection().getFactor());
			aggregators.add(sortCriteria.getAggregator());
		}
		
		final int N = selection.length;
		DoubleFactory1D df = DoubleFactory1D.dense;
		final DoubleMatrix1D row1 = df.make(N);
		final DoubleMatrix1D row2 = df.make(N);
		
		Arrays.sort(indices, new Comparator<Integer>() {		
			@Override
			public int compare(Integer idx1, Integer idx2) {
				
				int level = 0;
				double aggr1 = 0, aggr2 = 0;

				
				while (aggr1 == aggr2 && level < criterias) {
						
					for (int i = 0; i < N; i++) {
						int col = selection[i];
						
						Object value1 = contents.getCellValue(idx1, col, properties.get(level));
						double v1 = MatrixUtils.doubleValue(value1);
	
						if (!Double.isNaN(v1)) {
							row1.set(i, v1);
						}
						
						Object value2 = contents.getCellValue(idx2, col, properties.get(level));
						double v2 = MatrixUtils.doubleValue(value2);
	
						if (!Double.isNaN(v2)) {
							row2.set(i, v2);
						}
					}
					aggr1 = aggregators.get(level).aggregate(row1);
					aggr2 = aggregators.get(level).aggregate(row2);
					level++;
				}

				int res = (int) Math.signum(aggr1 - aggr2);
				level--;
				return res*directions.get(level);
			}
		});
		return indices;
	}
}
