package co.uk.app.commerce.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KafkaStreams.State;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamConfig {

	@Value("${kafka.config.bootstrap.servers}")
	private String bootstrapServers;

	@Value("${kafka.config.application_id}")
	private String applicationId;

	@Value("${kafka.config.schema.registry.url}")
	private String schemaRegistryURL;

	@Value("${kafka.config.state.store.directory}")
	private String stateStoreDir;

	@Value("${kafka.topic.category}")
	private String categoryTopic;

	@Value("${kafka.topic.catentry}")
	private String catentryTopic;

	@Value("${kafka.store.category}")
	private String categoryStore;

	private KafkaStreams streams = null;

	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public StreamsConfig config() {
		Serde<String> stringSerde = Serdes.String();

		Map<String, Object> props = new HashMap<>();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
		props.put(StreamsConfig.STATE_DIR_CONFIG, stateStoreDir);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, stringSerde.getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
		props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryURL);
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return new StreamsConfig(props);
	}

	@Bean
	public KTable<String, GenericRecord> ktable() throws Exception {
		final StreamsBuilder builder = new StreamsBuilder();
		KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(this.categoryStore);
		KTable<String, GenericRecord> table = builder.table(categoryTopic, Materialized.as(storeSupplier));
		table.toStream().print(Printed.toSysOut());

		if (null == streams) {
			streams = new KafkaStreams(builder.build(), config());
		}
		streams.start();

		return table;
	}

	public ReadOnlyKeyValueStore<String, GenericRecord> readStore() throws InterruptedException {
		final StreamsBuilder builder = new StreamsBuilder();
		if (null == streams) {
			streams = new KafkaStreams(builder.build(), config());
		}
		if (streams.state() != State.RUNNING) {
			streams.start();
		}
		waitUntilStoreIsQueryable(this.categoryStore, QueryableStoreTypes.<String, GenericRecord>keyValueStore(),
				streams);
		ReadOnlyKeyValueStore<String, GenericRecord> myStore = streams.store(this.categoryStore,
				QueryableStoreTypes.<String, GenericRecord>keyValueStore());
		return myStore;
	}

	public static <T> T waitUntilStoreIsQueryable(final String storeName,
			final QueryableStoreType<T> queryableStoreType, final KafkaStreams streams) throws InterruptedException {
		while (true) {
			try {
				return streams.store(storeName, queryableStoreType);
			} catch (InvalidStateStoreException ignored) {
				Thread.sleep(100);
			}
		}
	}

	@Bean
	public KStream<String, GenericRecord> kstreamCatentry() throws Exception {
		final StreamsBuilder builder = new StreamsBuilder();
		KStream<String, GenericRecord> ksin = builder.stream(catentryTopic);
		return ksin;
	}
}
