package ibm.cte.kafka.play;

public abstract class SimpleKafkaBase {
	// public static String BOOTSTRAP_SERVERS = "172.16.40.133:32224,172.16.40.137:32224,172.16.40.135:32224";
	public static String BOOTSTRAP_SERVERS = "gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:3224";
	// private static String TOPICNAME = "streams-wordcount-output";
	public static String TOPICNAME = "test-topic";
}
