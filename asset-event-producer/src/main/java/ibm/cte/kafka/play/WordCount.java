/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ibm.cte.kafka.play;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * Count word occurrence in a text sent to a topic.
 * It is using the high-level Streams DSL which reads from a source topic "test-topic", where the values of messages represent lines of text,
 * split each text line into words and then computes the word occurrence histogram, write the continuous updated histogram
 * into a topic "streams-wordcount-output" where each record is an updated count of a single word.
 */
public class WordCount {

    public static void main(String[] args) throws Exception {
    	System.out.println("################# Word Count ########### ");
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        // props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092,broker2:9092,broker3:9092");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.89:30092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();
        final Pattern pattern = Pattern.compile("\\W+");
        builder.<String, String>stream("test-topic")
        .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
        .map((key, value) -> new KeyValue<Object, Object>(value, value))
        .filter((key, value) -> (!value.equals("the")))
        .groupByKey()
        .count("CountStore").mapValues(value->Long.toString(value)).toStream();
        
        builder.<String, String>stream("test-topic")
              
               .groupBy(new KeyValueMapper<String, String, String>() {
                   @Override
                   public String apply(String key, String value) {
                       return value;
                   }
                })
               .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
               .toStream()
               .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));


        /* ------- use the code below for Java 8 and comment the above ----

        builder.<String, String>stream("streams-plaintext-input")
               .flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
               .groupBy((key, value) -> value)
               .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
               .toStream()
               .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

           ----------------------------------------------------------------- */

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
