package co.uk.app.commerce.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.processor.StateStoreSupplier;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.uk.app.commerce.catalog.bean.KafkaResponse;
import co.uk.app.commerce.catalog.document.Category;
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

	private ObjectMapper objectMapper = new ObjectMapper();

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
		// props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// String metadataMaxAgeMs =
		// System.getProperty(ConsumerConfig.METADATA_MAX_AGE_CONFIG);
		// if (metadataMaxAgeMs != null) {
		// try {
		// int value = Integer.parseInt(metadataMaxAgeMs);
		// props.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, value);
		// System.out.println(
		// "Set consumer configuration " +
		// ConsumerConfig.METADATA_MAX_AGE_CONFIG + " to " + value);
		// } catch (NumberFormatException ignored) {
		// }
		// }
		return new StreamsConfig(props);
	}

	@Bean
	public KTable<String, GenericRecord> ktable() throws Exception {
		final StreamsBuilder builder = new StreamsBuilder();
		StateStoreSupplier<StateStore> storeCategory = Stores.create(this.categoryStore).withKeys(Serdes.String())
				.withValues(Serdes.Integer()).persistent().build();
		KTable<String, GenericRecord> table = builder.table(categoryTopic, Materialized.as(storeCategory.name()));
		table.toStream().print(Printed.toSysOut());

		if (null == streams) {
			streams = new KafkaStreams(builder.build(), config());
		}
		streams.start();

		return table;
	}

//	@Bean
//	public KStream<String, GenericRecord> kstream() throws Exception {
//
//		StateStoreSupplier<StateStore> storeCategory = Stores.create(categoryStore).withKeys(Serdes.Integer())
//				.withValues(Serdes.Integer()).persistent().build();
//
//		final StreamsBuilder builder = new StreamsBuilder();
//		KStream<String, GenericRecord> ksin = builder.stream("category");
//
//		ksin.map(new KeyValueMapper<String, GenericRecord, KeyValue<? extends String, ? extends GenericRecord>>() {
//
//			@Override
//			public KeyValue<? extends String, ? extends GenericRecord> apply(String key, GenericRecord value) {
//				JsonNode data;
//				Category category = null;
//				try {
//					objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//					data = objectMapper.readTree(value.toString().getBytes());
//					KafkaResponse kafkaResponse = objectMapper.treeToValue(data, KafkaResponse.class);
//					if (null != kafkaResponse.getAfter()) {
//						category = kafkaResponse.getAfter();
//						System.out.println(category.getIdentifier());
//					}
//
//					if (null != kafkaResponse.getPatch()) {
//						category = kafkaResponse.getPatch();
//						System.out.println(category.getIdentifier());
//					}
//				} catch (Exception e) {
//					throw new SerializationException(e);
//				}
//
//				KeyValue<String, GenericRecord> kvin = new KeyValue<String, GenericRecord>(key, value);
//				return kvin;
//			}
//		}).to("catalog-category-table");
//
//		KTable<Integer, Integer> table = builder.table(categoryTopic, Materialized.as(storeCategory.name()));
//		table.toStream().print(Printed.toSysOut());
//
//		streams = new KafkaStreams(builder.build(), config());
//		streams.start();
//
//		return ksin;
//	}

	public ReadOnlyKeyValueStore<String, GenericRecord> readStore() throws InterruptedException {
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
