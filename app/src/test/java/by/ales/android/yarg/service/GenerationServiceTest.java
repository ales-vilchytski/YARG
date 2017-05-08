package by.ales.android.yarg.service;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Ales on 06.04.2017.
 */

public class GenerationServiceTest {

    private GenerationService service = new GenerationService();

    @Test
    public void testRandomNumbersAdjustment() {
        double[][] data = new double[][]{
                new double[] { 0.0, 0, 10, /*->*/ 0 },
                new double[] { 0.2, 0, 10, /*->*/ 2 },
                new double[] { 0.4, 0, 10, /*->*/ 4 },
                new double[] { 0.5, 0, 10, /*->*/ 5 },
                new double[] { 0.99, 0, 10, /*->*/ 9.9 },
                new double[] { 0.0, 10, 20, /*->*/ 10 },
                new double[] { 0.2, 10, 20, /*->*/ 12 },
                new double[] { 0.4, 10, 20, /*->*/ 14 },
                new double[] { 0.5, 10, 20, /*->*/ 15 },
                new double[] { 0.99, 10, 20, /*->*/ 19.9 }
        };

        for (double[] vals: data) {
            double actual = service.adjustNumbber(vals[0], vals[1], vals[2]);
            double expected = vals[3];
            Assert.assertEquals(expected, actual);
        }

    }

}
