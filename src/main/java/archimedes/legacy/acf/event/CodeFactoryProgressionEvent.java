package archimedes.legacy.acf.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A container for code factory progression events.
 *
 * @author ollie (12.01.2020)
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class CodeFactoryProgressionEvent {

	private String factoryName;
	private String processName;
	private String message;
	private Integer currentProcess;
	private Integer currentStep;
	private Integer maximumProcessCount;
	private Integer maximumStepCount;

}