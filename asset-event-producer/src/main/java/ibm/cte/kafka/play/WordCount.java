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
import java.util.regex.Pattern;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * Count word occurrence in a text sent to a topic.
 * It is using the high-level Streams DSL which reads from a source topic "test-topic", where the values of messages represent lines of text,
 * split each text line into words and then computes the word occurrence histogram, write the continuous updated histogram
 * into a topic "streams-wordcount-output" where each record is an updated count of a single word.
 * It is operating on an infinite, unbounded stream of data.
 */
public class WordCount {
	private static String SOURCETOPICNAME = "test-topic";
	private static String SINKTOPICNAME = "streams-wordcount-output";
	private static String BOOTSTRAP_SERVERS = "docker.for.mac.host.internal:30092";
	
    public static void main(String[] args) throws Exception {
    	String brokers = BOOTSTRAP_SERVERS;
		String source = SOURCETOPICNAME;
		String sink = SINKTOPICNAME;
		
		if(args.length == 3) {
			source = args[0].toString();
			sink =  args[1].toString();
			brokers = args[2].toString();
		}
		
    	System.out.println("################# Word Count Processor from: " + source + " to " + sink + " ###########");
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();
        final Pattern pattern = Pattern.compile("\\W+");
        
        KStream<String, String> textLines = builder.stream(source);

        KTable<String, Long> wordCounts = textLines
            .flatMapValues(textLine -> Arrays.asList(pattern.split(textLine.toLowerCase())))
            .groupBy((key, word) -> word)
            .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
        wordCounts.toStream().to(sink, Produced.with(Serdes.String(), Serdes.Long()));
 
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
               
    }
}
