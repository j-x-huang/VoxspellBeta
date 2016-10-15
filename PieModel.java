package beta;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieModel {
	String _level;
	private int _fails;
	private int _correct;
	public PieModel(int correct, int fails, String level) {
		_level = level;
		_fails = fails;
		_correct = correct;
	}
	
	protected JPanel getPieChart() {
		JFreeChart chart = PieChart(newPieDataset());
		return new ChartPanel(chart);
		
	}
	private PieDataset newPieDataset(){
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Correct", new Double(_correct));
		dataset.setValue("Incorrect", new Double(_fails));
		return dataset;
		
	}
	
	private JFreeChart PieChart(PieDataset dataset){
		//Creates the pie chart and sets its axis and colours
		JFreeChart pieChart = ChartFactory.createPieChart(
				"Accuracy for " + _level, 
				dataset,
				true,
				true,
				false);
		pieChart.getTitle().setFont(new Font("SansSerif", Font.ITALIC, 20));
		pieChart.getTitle().setPaint(new Color(51, 47, 47));
		pieChart.setBackgroundPaint(new Color(255,255,152));
		PiePlot plot = (PiePlot) pieChart.getPlot();
		plot.setBackgroundPaint(new Color(255,255,152));
		plot.setSectionPaint("Correct", new Color(83, 104, 120, 255));
		plot.setSectionPaint("Incorrect",new Color(255, 84, 83, 255));
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}: {1} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()
				));
		
		return pieChart;
	}
}
