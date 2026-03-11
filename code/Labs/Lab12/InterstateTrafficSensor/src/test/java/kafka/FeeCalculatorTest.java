package kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FeeCalculatorTest {

    private final FeeCalculator feeCalculator = new FeeCalculator();

    @Test
    void returnsNoFeeAtOrBelowLimit() {
        assertEquals(0, feeCalculator.calculateFee(72));
    }

    @Test
    void returnsTwentyFiveDollarsForSpeedsUpToSeventySeven() {
        assertEquals(25, feeCalculator.calculateFee(77));
    }

    @Test
    void returnsFortyFiveDollarsForSpeedsUpToEightyTwo() {
        assertEquals(45, feeCalculator.calculateFee(82));
    }

    @Test
    void returnsEightyDollarsForSpeedsUpToNinety() {
        assertEquals(80, feeCalculator.calculateFee(90));
    }

    @Test
    void returnsOneHundredTwentyFiveDollarsAboveNinety() {
        assertEquals(125, feeCalculator.calculateFee(91));
    }
}
