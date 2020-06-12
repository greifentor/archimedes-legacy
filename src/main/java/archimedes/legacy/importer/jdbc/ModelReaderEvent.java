package archimedes.legacy.importer.jdbc;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.ToString;

/**
 * A container for model reader events.
 * 
 * @author ollie (12.06.2020)
 *
 */
@Data
@EqualsAndHashCode
@Getter
@Generated
@ToString
public class ModelReaderEvent {

	private LocalDateTime timestamp;
	private Integer currentProgress;
	private Integer maximumProgress;
	private Integer thread;
	private ModelReaderEventType type;
	private String objectName;

	public ModelReaderEvent(Integer currentProgress, Integer maximumProgress, Integer thread, ModelReaderEventType type,
			String objectName) {
		super();
		this.currentProgress = currentProgress;
		this.maximumProgress = maximumProgress;
		this.objectName = objectName;
		this.thread = thread;
		this.timestamp = LocalDateTime.now();
		this.type = type;
	}

}