## Storage 
 - for fraud detector data input
 - for fraud detector data output

### Create 3 buckets

**Docs**
- gsutils mb: https://cloud.google.com/storage/docs/gsutil/commands/mb 
- bucket locations: https://cloud.google.com/storage/docs/bucket-locations

<details><summary><b>Answer</b></summary>
<pre><code>gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-input/
gsutil mb -c regional -l europe-west3 gs://gft-academy-fraud-detector-output/
</code></pre>
</details>


### Upload sample data

**Docs**
- gsutils cp: https://cloud.google.com/storage/docs/gsutil/commands/cp

<details><summary><b>Answer</b></summary>
<pre><code>gsutil cp gs://gft-academy-fraud-detector-public-data/trades-small.csv gs://gft-academy-fraud-detector-input/
</code></pre>
</details>

## Antifraud ETL

### Create DataFlow bootstrap project

**Docs**
- https://cloud.google.com/dataflow/docs
- https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven

If you want to execute pipeline on the DataFlow you need to:
 1. install gsutils locally
 2. authenticate with command: ```gcloud auth login```

<details><summary><b>Answer</b></summary>
<pre><code>mvn archetype:generate \
      -DarchetypeArtifactId=google-cloud-dataflow-java-archetypes-examples \
      -DarchetypeGroupId=com.google.cloud.dataflow \
      -DarchetypeVersion=2.4.0 \
      -DgroupId=com.gft.academy \
      -DartifactId=gcp-anti-fraud-detector \
      -Dversion="0.1" \
      -DinteractiveMode=false \
      -Dpackage=com.gft.academy</code></pre>

<h4>Run locally</h4>
<pre><code>mvn compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--output=./output/"
</code></pre>

<h4>Run on the DataFlow</h4>
<pre><code>mvn compile exec:java \
      -Dexec.mainClass=com.gft.academy.WordCount \
      -Dexec.args="--project=&lt;my-cloud-project&gt; \
      --stagingLocation=gs://gft-academy-fraud-detector-output/staging/ \
      --inputFile=gs://gft-academy-fraud-detector-input/trades-small.csv \
      --output=gs://gft-academy-fraud-detector-output/output \
      --runner=DataflowRunner"
</code></pre>
</details>

### Create own Pipeline to find the frauds ###

<details><summary><b>Answer</b></summary>
 <h4>Implementation</h4>
 
<pre><code>package com.gft.academy;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.\*;
import org.apache.beam.sdk.transforms.\*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.Objects;

public class FraudDetector {

    public static void main(String[] args) {
        FraudDetectorOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(FraudDetectorOptions.class);
        Pipeline p = Pipeline.create(options);

        p.apply("ReadLines", TextIO.read().from(options.getInputFile()))
                .apply("Main fraud detecting pipeline",new FraudDetectorPipeline())
                .apply("Write Frauds to output", TextIO.write().to(options.getOutput()));

        p.run().waitUntilFinish();
    }

    public static class FraudDetectorPipeline extends PTransform<PCollection<String>, PCollection<String>> {

        @Override
        public PCollection<String> expand(PCollection<String> input) {
            return input.apply("Parse rows to Domain object", ParDo.of(new ParseTradeFn()))
                    .apply("Filter amounts less than 10", Filter.by(trade -> trade.amount.compareTo(10) < 0))
                    .apply("Prepare map of the clients with amounts", MapElements.via(new SimpleFunction<Trade, KV<String, Long>>() {
                        @Override
                        public KV<String, Long> apply(Trade input) {
                            return KV.of(input.client, input.amount.longValue());
                        }
                    }))
                    .apply("Sum total amount per client", Sum.longsPerKey())
                    .apply("Filter clients where the sum of amount is more than 100", Filter.by(counts -> counts.getValue().compareTo(100L) > 0))
                    .apply(MapElements.into(TypeDescriptors.strings()).via(entry -> entry.getKey() + "," + entry.getValue()));
        }
    }


    public interface FraudDetectorOptions extends PipelineOptions {

        @Description("Path of the file to read from")
        @Default.String("gs://gft-academy-fraud-detector-public-data/trades-small.csv")
        String getInputFile();

        void setInputFile(String value);

        @Description("Path of the file to write to")
        @Validation.Required
        String getOutput();

        void setOutput(String value);
    }

    public static class ParseTradeFn extends DoFn<String, Trade> {

        @ProcessElement
        public void processElement(ProcessContext c) {
            String[] cols = c.element().split(",");

            Trade model = new Trade();
            model.client = cols[1];
            model.amount = Integer.parseInt(cols[5]);
            model.createdOn = LocalDateTime.parse(cols[20]);

            Instant timestamp = new Instant(model.createdOn.toDate().getTime());

            c.outputWithTimestamp(model, timestamp);
        }

    }

    public static class Trade implements Serializable {
        String client;
        Integer amount;
        LocalDateTime createdOn;

        @Override
        public String toString() {
            return "Client: " + client + ", amount: " + amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Trade trade = (Trade) o;
            return Objects.equals(createdOn, trade.createdOn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(createdOn);
        }
    }

}
</code></pre>

<h4>Test</h4>

<pre><code>package com.gft.academy;

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
</code></pre>

<h4>Run locally</h4>
<pre><code>mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.FraudDetector \
      -Dexec.args="--output=./target/frauds/"
</code></pre>

<h4>Run on the DataFlow</h4>
<pre><code>mvn clean compile exec:java \
      -Dexec.mainClass=com.gft.academy.FraudDetector \
      -Dexec.args="--output=./target/frauds/ --runner=DataflowRunner --project=&lt;my-cloud-project&gt;"
</code></pre>
</details>
