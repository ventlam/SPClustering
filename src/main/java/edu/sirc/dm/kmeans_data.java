package edu.sirc.dm;

/**
 *
 * @param <b>data</b> <i>in double[length][dim]</i><br/>length个instance的坐标，第i(0~length-1)个instance为data[i]
 * @param <b>length</b> <i>in</i> instance个数
 * @param <b>dim</b> <i>in</i> instance维数
 * @param <b>label</b> <i>out int[length]</i><br/>聚类后，instance所属的聚类标号(0~k-1)
 * @param <b>centers</b> <i>in out double[k][dim]</i><br/>k个聚类中心点的坐标，第i(0~k-1)个中心点为centers[i]
 * @author Yuanbo She
 *
 */
public class kmeans_data {
	public double[][] data;
	public int length;
	public int dim;
	public int[] labels;
	public double[][] centers;
	public int[] centerCounts;
	
	public kmeans_data(double[][] data, int length, int dim) {
		this.data = data;
		this.length = length;
		this.dim = dim;
	}
}
