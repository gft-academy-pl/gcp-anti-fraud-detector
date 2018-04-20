package com.gft.academy;

import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static java.util.Arrays.asList;

@RunWith(JUnit4.class)
public class FraudDetectorTest {

    @Rule
    public TestPipeline p = TestPipeline.create();

    @Test
    public void shouldFindFraud() {
        List<String> INPUT_DATA = asList(
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:01:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:02:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:03:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:04:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:05:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:06:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:07:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:08:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:09:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:10:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:11:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T08:12:00"
        );

        PCollection<String> output = p
                .apply(Create.of(INPUT_DATA)).setCoder(StringUtf8Coder.of())
                .apply(new FraudDetector.FraudDetectorPipeline());

        PAssert.that(output).containsInAnyOrder(asList(
                "Bluezoom,108"
        ));

        p.run().waitUntilFinish();

    }

    @Test
    public void shouldNotFindFraudWithTradesSumAmountLessThanTreshold100() {
        List<String> INPUT_DATA = asList(
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:01:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:02:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:03:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:04:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:05:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:06:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:07:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:08:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:09:00",
                ",Bluezoom,,,,9,,,,,,,,,,,,,,,2018-04-10T07:10:00"
        );

        PCollection<String> output = p
                .apply(Create.of(INPUT_DATA)).setCoder(StringUtf8Coder.of())
                .apply(new FraudDetector.FraudDetectorPipeline());

        PAssert.that(output).empty();

        p.run().waitUntilFinish();

    }

    @Test
    public void shouldNotFindFraudWithTradesAboveSingleAmountMoreThanTreshold10() {
        List<String> INPUT_DATA = asList(
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:01:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:02:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:03:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:04:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:05:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:06:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:07:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:08:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:08:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:08:00",
                ",Bluezoom,,,,11,,,,,,,,,,,,,,,2018-04-10T07:08:00"
        );

        PCollection<String> output = p
                .apply(Create.of(INPUT_DATA)).setCoder(StringUtf8Coder.of())
                .apply(new FraudDetector.FraudDetectorPipeline());

        PAssert.that(output).empty();

        p.run().waitUntilFinish();

    }
}
