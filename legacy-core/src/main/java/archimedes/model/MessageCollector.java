package archimedes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;

public class MessageCollector {

	public enum Priority {
		INFO,
		WARN,
		ERROR,
		FATAL;
	}

	@Getter
	public static class Message {

		public Message(String codeGeneratorName, Priority priority, String message) {
			this.codeGeneratorName = codeGeneratorName;
			this.message = message;
			this.priority = priority;
			this.timestamp = LocalDateTime.now();
		}

		private String codeGeneratorName;
		private String message;
		private Priority priority;
		private LocalDateTime timestamp;
	}

	private List<Message> messages = new ArrayList<>();

	public void add(Message message) {
		if (message != null) {
			messages.add(message);
		}
	}

	public Stream<Message> getMessages() {
		return messages.stream();
	}

}
