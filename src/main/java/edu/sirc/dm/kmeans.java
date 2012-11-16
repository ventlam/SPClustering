package edu.sirc.dm;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Yuanbo She
 * 
 */
public class kmeans {

    /**
     * double[][] Ԫ��ȫ��0
     * 
     * @param matrix
     *            double[][]
     * @param highDim
     *            int
     * @param lowDim
     *            int <br/>
     *            double[highDim][lowDim]
     */
    private static void setDouble2Zero(double[][] matrix, int highDim, int lowDim) {
        for (int i = 0; i < highDim; i++) {
            for (int j = 0; j < lowDim; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    /**
     * ����Դ��ά����Ԫ�ص�Ŀ���ά���� foreach (dests[highDim][lowDim] = sources[highDim][lowDim]);
     * 
     * @param dests
     *            double[][]
     * @param sources
     *            double[][]
     * @param highDim
     *            int
     * @param lowDim
     *            int
     */
    private static void copyCenters(double[][] dests, double[][] sources, int highDim, int lowDim) {
        for (int i = 0; i < highDim; i++) {
            for (int j = 0; j < lowDim; j++) {
                dests[i][j] = sources[i][j];
            }
        }
    }

    /**
     * ���¾�����������
     * 
     * @param k
     *            int �������
     * @param data
     *            kmeans_data
     */
    private static void updateCenters(int k, kmeans_data data) {
        double[][] centers = data.centers;
        setDouble2Zero(centers, k, data.dim);
        int[] labels = data.labels;
        int[] centerCounts = data.centerCounts;
        for (int i = 0; i < data.dim; i++) {
            for (int j = 0; j < data.length; j++) {
                centers[labels[j]][i] += data.data[j][i];
            }
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < data.dim; j++) {
                centers[i][j] = centers[i][j] / centerCounts[i];
            }
        }
    }

    /**
     * ��������ŷ�Ͼ���
     * 
     * @param pa
     *            double[]
     * @param pb
     *            double[]
     * @param dim
     *            int ά��
     * @return double ����
     */
    public static double dist(double[] pa, double[] pb, int dim) {
        double rv = 0;
        for (int i = 0; i < dim; i++) {
            double temp = pa[i] - pb[i];
            temp = temp * temp;
            rv += temp;
        }
        return Math.sqrt(rv);
    }

    /**
     * ��Kmeans����
     * 
     * @param k
     *            int �������
     * @param data
     *            kmeans_data kmeans������
     * @param param
     *            kmeans_param kmeans������
     * @return kmeans_result kmeans������Ϣ��
     */
    public static kmeans_result doKmeans(int k, kmeans_data data, kmeans_param param) {
        // Ԥ����
        double[][] centers = new double[k][data.dim]; // �������ĵ㼯
        data.centers = centers;
        int[] centerCounts = new int[k]; // ������İ��������
        data.centerCounts = centerCounts;
        Arrays.fill(centerCounts, 0);
        int[] labels = new int[data.length]; // ����������������
        data.labels = labels;
        double[][] oldCenters = new double[k][data.dim]; // ��ʱ����ɵľ�����������

        // ��ʼ���������ģ������������ѡ��data�ڵ�k�����ظ��㣩
        if (param.initCenterMehtod == kmeans_param.CENTER_RANDOM) { // ���ѡȡk����ʼ��������
            Random rn = new Random();
            List<Integer> seeds = new LinkedList<Integer>();
            while (seeds.size() < k) {
                int randomInt = rn.nextInt(data.length);
                if (!seeds.contains(randomInt)) {
                    seeds.add(randomInt);
                }
            }
            Collections.sort(seeds);
            for (int i = 0; i < k; i++) {
                int m = seeds.remove(0);
                for (int j = 0; j < data.dim; j++) {
                    centers[i][j] = data.data[m][j];
                }
            }
        } else { // ѡȡǰk����λ��ʼ��������
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < data.dim; j++) {
                    centers[i][j] = data.data[i][j];
                }
            }
        }

        // ��һ�ֵ���
        for (int i = 0; i < data.length; i++) {
            double minDist = dist(data.data[i], centers[0], data.dim);
            int label = 0;
            for (int j = 1; j < k; j++) {
                double tempDist = dist(data.data[i], centers[j], data.dim);
                if (tempDist < minDist) {
                    minDist = tempDist;
                    label = j;
                }
            }
            labels[i] = label;
            centerCounts[label]++;
        }
        updateCenters(k, data);
        copyCenters(oldCenters, centers, k, data.dim);

        // ����Ԥ����
        int maxAttempts = param.attempts > 0 ? param.attempts : kmeans_param.MAX_ATTEMPTS;
        int attempts = 1;
        double criteria = param.criteria > 0 ? param.criteria : kmeans_param.MIN_CRITERIA;
        double criteriaBreakCondition = 0;
        boolean[] flags = new boolean[k]; // �����Щ���ı��޸Ĺ�

        // ����
        iterate: while (attempts < maxAttempts) { // �����������������ֵ��������ĸı�����������ֵ
            for (int i = 0; i < k; i++) { // ��ʼ�����ĵ㡰�Ƿ��޸Ĺ������
                flags[i] = false;
            }
            for (int i = 0; i < data.length; i++) { // ����data�����е�
                double minDist = dist(data.data[i], centers[0], data.dim);
                int label = 0;
                for (int j = 1; j < k; j++) {
                    double tempDist = dist(data.data[i], centers[j], data.dim);
                    if (tempDist < minDist) {
                        minDist = tempDist;
                        label = j;
                    }
                }
                if (label != labels[i]) { // �����ǰ�㱻���ൽ�µ������������
                    int oldLabel = labels[i];
                    labels[i] = label;
                    centerCounts[oldLabel]--;
                    centerCounts[label]++;
                    flags[oldLabel] = true;
                    flags[label] = true;
                }
            }
            updateCenters(k, data);
            attempts++;

            // ���㱻�޸Ĺ������ĵ�����޸����Ƿ񳬹���ֵ
            double maxDist = 0;
            for (int i = 0; i < k; i++) {
                if (flags[i]) {
                    double tempDist = dist(centers[i], oldCenters[i], data.dim);
                    if (maxDist < tempDist) {
                        maxDist = tempDist;
                    }
                    for (int j = 0; j < data.dim; j++) { // ����oldCenter
                        oldCenters[i][j] = centers[i][j];
                    }
                }
            }
            if (maxDist < criteria) {
                criteriaBreakCondition = maxDist;
                break iterate;
            }
        }

        // �����Ϣ
        kmeans_result rvInfo = new kmeans_result();
        rvInfo.attempts = attempts;
        rvInfo.criteriaBreakCondition = criteriaBreakCondition;
        if (param.isDisplay) {
            System.out.println("k=" + k);
            System.out.println("attempts=" + attempts);
            System.out.println("criteriaBreakCondition=" + criteriaBreakCondition);
            System.out.println("The number of each classes are: ");
            for (int i = 0; i < k; i++) {
                System.out.print(centerCounts[i] + " ");
            }
            System.out.print("\n\n");
        }
        return rvInfo;
    }
}
