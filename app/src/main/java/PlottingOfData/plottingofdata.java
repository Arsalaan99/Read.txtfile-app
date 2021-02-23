package PlottingOfData;



import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

 public class plottingofdata {
    LineGraphSeries<DataPoint> series;
    PointsGraphSeries<DataPoint> series2;


    public void plotECG(LineGraphSeries<DataPoint> series, GraphView graph){
        graph.addSeries(series);

    }
     public void plotPeak(PointsGraphSeries<DataPoint> series2, GraphView graph){
         series2.setSize(10);
         series2.setColor(Color.RED);
         graph.addSeries(series2);

     }
     public void SetXYaxis(GraphView graph){
         graph.getViewport().setXAxisBoundsManual(true);
         graph.getViewport().setMinX(0);
         graph.getViewport().setMaxX(10);

         graph.getViewport().setYAxisBoundsManual(true);
         graph.getViewport().setMinY(-1);
         graph.getViewport().setMaxY(2);

         graph.getViewport().setScrollable(true); // enables horizontal scrolling
         graph.getViewport().setScrollable(true); // enables vertical scrolling
     }
}
