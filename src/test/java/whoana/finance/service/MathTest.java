package whoana.finance.service;

import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;

public class MathTest {


	public static void main(String[] args) {
		try {
			SumOfLogs sl = new SumOfLogs();
			System.out.println(sl.evaluate(new double[] {1,2,3,4,5,6,7,8,9,10}));

			GeometricMean gm = new GeometricMean();
			gm.setData(new double[] {10,-10});
			System.out.println(gm.evaluate());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
