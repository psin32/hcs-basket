package co.uk.app.commerce.kafka.config;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.uk.app.commerce.catalog.bean.KafkaResponse;
import co.uk.app.commerce.catalog.repository.CategoryRepository;

public class KafkaReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private CategoryRepository categoryRepository;

	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}

	@KafkaListener(topics = "${kafka.topic.category}", containerFactory = "kafkaListenerContainerFactory")
	public void categoryReceive(ConsumerRecord<?, ?> record) {
		LOGGER.info("received payload='{}'", record.value());

//		JsonNode data;
//		try {
//			objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//			data = objectMapper.readTree(record.value().toString().getBytes());
//		} catch (Exception e) {
//			throw new SerializationException(e);
//		}
//
//		try {
//			KafkaResponse kafkaResponse = objectMapper.treeToValue(data, KafkaResponse.class);
//			categoryRepository.save(kafkaResponse.getAfter());
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		latch.countDown();
	}
}
