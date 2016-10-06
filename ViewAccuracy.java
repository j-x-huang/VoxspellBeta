package beta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ViewAccuracy extends AbstractTableModel{
	private ArrayList<Integer> attemptsList = new ArrayList<Integer>();
	private ArrayList<Integer> failsList = new ArrayList<Integer>();
	private ArrayList<Double> accuracyList = new ArrayList<Double>();
	

	private final String[] COLUMN_HEADERS = {"Level", "Accuracy", "Fails","Attempts" };
	private final Class<?> _colClasses[] = {String.class, String.class, Integer.class, Integer.class};


	public ViewAccuracy() {
		//This for loop reads all accuracy save files and adds the values to some lists
		for (int i = 1; i <= 11; i++) {
			try {
			FileReader fr = new FileReader(".accuracy_" + i);
			BufferedReader br = new BufferedReader(fr);
			String str;
			str = br.readLine();
			attemptsList.add(Integer.parseInt(str));
			str = br.readLine();
			failsList.add(Integer.parseInt(str));
			br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//This for loop calculates the overall accuracy (as a percentage) for all levels
		for (int j = 0; j < 11; j++) {
			double num = 0;
			if (attemptsList.get(j) != 0) {
				num = (double) failsList.get(j) / (double) attemptsList.get(j);
				num = 100 - (num * 100);
				
				num = (double) Math.round(num * 100) / 100;

			}

			accuracyList.add(num);
		}
		
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 11;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 4;

	}
	//Add values from the arraylists into the table
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		if (columnIndex == 0) {
			int tempRow = rowIndex + 1;
			return "Level " + tempRow;
		} else if (columnIndex == 1) {
			return accuracyList.get(rowIndex) + "%";
		} else if (columnIndex == 2) {
			return failsList.get(rowIndex);

		} else if (columnIndex == 3) {
			return attemptsList.get(rowIndex);
		}
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		return COLUMN_HEADERS[column];

	}
	@Override
	public Class<?> getColumnClass(int column) {
		return _colClasses[column];
	}
	
	
	
}
