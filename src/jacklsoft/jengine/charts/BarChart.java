package jacklsoft.jengine.charts;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.ui.TextAnchor;

public class BarChart implements JRChartCustomizer{
    public void customize(JFreeChart jFreeChart, JRChart jRChart) {
        ItemLabelPosition ilp = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT, TextAnchor.CENTER_LEFT, -Math.PI / 2);
        jFreeChart.getCategoryPlot().getRenderer().setBasePositiveItemLabelPosition(ilp);
    }
}
