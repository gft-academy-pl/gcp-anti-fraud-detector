package com.gft.academy;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.Sum;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.Objects;

public class FraudDetector {

    public static void main(String[] args) {
        FraudDetectorOptions options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(FraudDetectorOptions.class);
        Pipeline p = Pipeline.create(options);

        p.apply("ReadLines", TextIO.read().from(options.getInputFile()))
                .apply("Main fraud detecting pipeline", new FraudDetectorPipeline())
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
                    .apply(Sum.longsPerKey())
                    .apply("Filter clients where the sum of amount is more than 100", Filter.by(counts -> counts.getValue().compareTo(100L) > 0))
                    .apply(MapElements.into(TypeDescriptors.strings()).via(entry -> entry.getKey() + "," + entry.getValue()));
        }

    }

    public interface FraudDetectorOptions extends PipelineOptions {


        @Description("Path of the file to read from")
        @Default.String("gs://gft-academy-fraud-detector-public-data/trades-small.csv")
        ValueProvider<String> getInputFile();

        void setInputFile(ValueProvider<String> value);

        @Description("Path of the file to write to")
        @Validation.Required
        ValueProvider<String> getOutput();

        void setOutput(ValueProvider<String> value);

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
