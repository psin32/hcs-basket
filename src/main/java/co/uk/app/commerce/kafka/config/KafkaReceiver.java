package co.uk.app.commerce.kafka.config;

import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}

	@KafkaListener(topics = "${kafka.topic.category}", containerFactory = "kafkaListenerContainerFactory")
	public void categoryReceive(ConsumerRecord<?, ?> record) {
		LOGGER.info("received payload='{}'", record.value());
		latch.countDown();
	}
}
