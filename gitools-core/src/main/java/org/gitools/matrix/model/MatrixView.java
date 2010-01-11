package org.gitools.matrix.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementProperty;
import org.gitools.model.xml.adapter.IndexArrayXmlAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( 
		propOrder={
		"contents",
		"visibleRows", "visibleColumns",
		"selectedRows", "selectedColumns", 
		"selectionLeadRow", "selectionLeadColumn",
		"selectedPropertyIndex"} )

public class MatrixView
		extends AbstractModel
		implements Serializable, IMatrixView {

	private static final long serialVersionUID = -8602409555044803568L;

	private static final int INT_BIT_SIZE = 32;

	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected IMatrix contents;
	
	@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	protected int[] visibleRows;
	
	@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	protected int[] visibleColumns;
	
	@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	protected int[] selectedRows;
	
	@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	protected int[] selectedColumns;
	
	protected int selectionLeadRow;
	protected int selectionLeadColumn;
	
	protected int selectedPropertyIndex;

	@XmlTransient
	private int[] selectedColumnsBitmap;
	@XmlTransient
	private int[] selectedRowsBitmap;
	
	public MatrixView() {
		visibleRows = new int[0];
		visibleColumns = new int[0];
		selectedRows = new int[0];
		selectedColumns = new int[0];
		selectedColumnsBitmap = new int[0];
		selectedRowsBitmap = new int[0];
		selectionLeadRow = selectionLeadColumn = -1;
	}
	
	public MatrixView(IMatrix contents) {
		this.contents = contents;
		
		// initialize visible rows and columns
		
		visibleRows = new int[contents.getRowCount()];
		for (int i = 0; i < contents.getRowCount(); i++)
			visibleRows[i] = i;
		
		visibleColumns = new int[contents.getColumnCount()];
		for (int i = 0; i < contents.getColumnCount(); i++)
			visibleColumns[i] = i;
		
		// initialize selection
		
		selectedRows = new int[0];
		selectedColumns = new int[0];

		selectedColumnsBitmap = newSelectionBitmap(contents.getColumnCount());
		selectedRowsBitmap = newSelectionBitmap(contents.getRowCount());

		selectionLeadRow = selectionLeadColumn = -1;
		
		// selected property
		
		int i = 0;
		for (IElementProperty attr : contents.getCellAttributes()) {
			if ("right-p-value".equals(attr.getId())
					|| "p-value".equals(attr.getId())) {
					selectedPropertyIndex = i;
					break;
			}
			i++;
		}
	}
	
	/* visibility */
	
	@Override
	public IMatrix getContents() {
		return contents;
	}

	@Override
	public int[] getVisibleRows() {
		return visibleRows;
	}

	@Override
	public void setVisibleRows(int[] indices) {
		this.visibleRows = indices;
		firePropertyChange(VISIBLE_ROWS_CHANGED);
	}

	@Override
	public int[] getVisibleColumns() {
		return visibleColumns;
	}

	@Override
	public void setVisibleColumns(int[] indices) {
		this.visibleColumns = indices;
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);
	}
	
	@Override
	public void moveRowsUp(int[] indices) {
		arrayMoveLeft(visibleRows, indices, selectedRows);
		firePropertyChange(VISIBLE_ROWS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void moveRowsDown(int[] indices) {
		arrayMoveRight(visibleRows, indices, selectedRows);
		firePropertyChange(VISIBLE_ROWS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void moveColumnsLeft(int[] indices) {
		arrayMoveLeft(visibleColumns, indices, selectedColumns);
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}

	@Override
	public void moveColumnsRight(int[] indices) {
		arrayMoveRight(visibleColumns, indices, selectedColumns);
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	/* selection */
	
	@Override
	public int[] getSelectedRows() {
		return selectedRows;
	}
	
	@Override
	public void setSelectedRows(int[] indices) {
		this.selectedRows = indices;
		updateSelectionBitmap(selectedRowsBitmap, indices);
		firePropertyChange(SELECTION_CHANGED);
	}

	@Override
	public boolean isRowSelected(int index) {
		int bindex = index / INT_BIT_SIZE;
		int bit = 1 << (index % INT_BIT_SIZE);
		return (selectedRowsBitmap[bindex] & bit) != 0;
	}

	@Override
	public int[] getSelectedColumns() {
		return selectedColumns;
	}
	
	@Override
	public void setSelectedColumns(int[] indices) {
		this.selectedColumns = indices;
		updateSelectionBitmap(selectedColumnsBitmap, indices);
		firePropertyChange(SELECTION_CHANGED);
	}

	@Override
	public boolean isColumnSelected(int index) {
		int bindex = index / INT_BIT_SIZE;
		int bit = 1 << (index % INT_BIT_SIZE);
		return (selectedColumnsBitmap[bindex] & bit) != 0;
	}

	@Override
	public void selectAll() {
		selectedRows = new int[getRowCount()];
		for (int i = 0; i < getRowCount(); i++)
			selectedRows[i] = i;
		selectedColumns = new int[0];

		Arrays.fill(selectedRowsBitmap, -1);
		Arrays.fill(selectedColumnsBitmap, 0);

		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void invertSelection() {
		if (selectedRows.length == 0 && selectedColumns.length == 0)
			selectAll();
		else if (selectedRows.length == 0)
			setSelectedColumns(invertSelectionArray(
					selectedColumns, getColumnCount()));
		else if (selectedColumns.length == 0)
			setSelectedRows(invertSelectionArray(
					selectedRows, getRowCount()));
	}
	
	@Override
	public void clearSelection() {
		selectedColumns = new int[0];
		selectedRows = new int[0];

		Arrays.fill(selectedRowsBitmap, 0);
		Arrays.fill(selectedColumnsBitmap, 0);

		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public int getSelectionLeadRow() {
		return selectionLeadRow;
	}
	
	@Override
	public int getSelectionLeadColumn() {
		return selectionLeadColumn;
	}
	
	@Override
	public void setLeadSelection(int row, int column) {
		this.selectionLeadRow = row;
		this.selectionLeadColumn = column;
		firePropertyChange(SELECTED_LEAD_CHANGED);
	}

	@Override
	public int getSelectedPropertyIndex() {
		return selectedPropertyIndex;
	}

	@Override
	public void setSelectedPropertyIndex(int index) {
		this.selectedPropertyIndex = index;		
	}

	/* IMatrix */
	
	@Override
	public int getRowCount() {
		return visibleRows.length;
	}
	
	@Override
	public String getRowLabel(int index) {
		return contents.getRowLabel(visibleRows[index]);
	}
	
	@Override
	public int getColumnCount() {
		return visibleColumns.length;
	}
	
	@Override
	public String getColumnLabel(int index) {
		return contents.getColumnLabel(visibleColumns[index]);
	}
	
	@Override
	public Object getCell(int row, int column) {
		return contents.getCell(visibleRows[row], visibleColumns[column]);
	}
	
	@Override
	public Object getCellValue(int row, int column, int index) {
		return contents.getCellValue(
				visibleRows[row], 
				visibleColumns[column],
				index);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		return contents.getCellValue(
				visibleRows[row], 
				visibleColumns[column],
				id);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		contents.setCellValue(
				visibleRows[row], 
				visibleColumns[column], 
				index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		contents.setCellValue(
				visibleRows[row], 
				visibleColumns[column], 
				id, value);
	}
	
	@Override
	public IElementAdapter getCellAdapter() {
		return contents.getCellAdapter();
	}
	
	@Override
	public List<IElementProperty> getCellAttributes() {
		return contents.getCellAttributes();
	}
	
	// internal operations
	
	private void arrayMoveLeft(
			int[] array, int[] indices, int[] selection) {
		
		if (indices.length == 0 || indices[0] == 0)
			return;
		
		Arrays.sort(indices);
		
		for (int idx : indices) {
			int tmp = array[idx - 1];
			array[idx - 1] = array[idx];
			array[idx] = tmp;
		}
		
		for (int i = 0; i < selection.length; i++)
			selection[i]--;
	}
	
	private void arrayMoveRight(
			int[] array, int[] indices, int[] selection) {
		
		if (indices.length == 0 
				|| indices[indices.length - 1] == array.length - 1)
				return;
			
		Arrays.sort(indices);
		
		for (int i = indices.length - 1; i >= 0; i--) {
			int idx = indices[i];
			int tmp = array[idx + 1];
			array[idx + 1] = array[idx];
			array[idx] = tmp;
		}
		
		for (int i = 0; i < selection.length; i++)
			selection[i]++;
	}

	private int[] invertSelectionArray(int[] array, int count) {
		int size = count - array.length;
		int[] invArray = new int[size];

		int j = 0;
		int lastIndex = 0;
		for (int i = 0; i < array.length; i++) {
			while (lastIndex < array[i])
				invArray[j++] = lastIndex++;
			lastIndex = array[i] + 1;
		}
		while (lastIndex < count)
			invArray[j++] = lastIndex++;

		return invArray;
	}

	private int[] newSelectionBitmap(int size) {
		int[] a = new int[(size + INT_BIT_SIZE - 1) / INT_BIT_SIZE];
		Arrays.fill(a, 0);
		return a;
	}

	private void updateSelectionBitmap(int[] bitmap, int[] indices) {
		Arrays.fill(bitmap, 0);
		for (int index : indices) {
			int bindex = index / INT_BIT_SIZE;
			int bit = 1 << (index % INT_BIT_SIZE);
			bitmap[bindex] |= bit;
		}
	}

	// FIXME:
	// 	this must be common to MatrixView and TableView

	// Marshal and unMarshall methods
	// Marshalling
	public void beforeMarshal(Marshaller u) {
		boolean naturalOrder = true;
		int rows = visibleRows.length;
		int columns = visibleColumns.length;
		int maxSize = rows > columns ? rows : columns;

		int i = 0;
		while (i < maxSize && naturalOrder) {
			if (i < columns)
				naturalOrder = i == visibleColumns[i];
			if (i < rows)
				naturalOrder = (i == visibleRows[i] && naturalOrder);
			i++;
		}

		if (naturalOrder) {
			visibleColumns = null;
			visibleRows = null;
		}
	}

	public void afterMarshal(Marshaller u) {
		if (visibleColumns == null && visibleRows == null) {
			
			int count = contents.getRowCount();
			int[] rows = new int[count];

			for (int i = 0; i < count; i++)
				rows[i] = i;
			setVisibleRows(rows);

			count = contents.getColumnCount();
			int[] columns = new int[count];

			for (int i = 0; i < count; i++)
				columns[i] = i;
			setVisibleColumns(columns);
		}
	}

	// UnMarshalling
	void afterUnmarshal(Unmarshaller u, Object parent) {
		int count = 0;
		int[] rows;
		int[] columns;

		if (visibleRows.length == 0) {
			count = contents.getRowCount();
			rows = new int[count];

			for (int i = 0; i < count; i++)
				rows[i] = i;
			setVisibleRows(rows);
		}
		if (visibleColumns.length == 0) {
			count = contents.getColumnCount();
			columns = new int[count];

			for (int i = 0; i < count; i++)
				columns[i] = i;
			setVisibleColumns(columns);
		}
	}

}