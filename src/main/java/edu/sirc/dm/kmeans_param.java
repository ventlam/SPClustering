package edu.sirc.dm;

/**
 * 
 * @author Yuanbo She
 *
 */
public class kmeans_param {
	public static final int CENTER_ORDER = 0;
	public static final int CENTER_RANDOM = 1;
	public static final int MAX_ATTEMPTS = 4000;
	public static final double MIN_CRITERIA = 1.0;
	
	public double criteria = MIN_CRITERIA; //��ֵ
	public int attempts = MAX_ATTEMPTS; //���Դ���
	public int initCenterMehtod = CENTER_ORDER; //��ʼ���������ĵ㷽ʽ
	public boolean isDisplay = true; //�Ƿ�ֱ����ʾ���
}