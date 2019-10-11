package de.ollie.archimedes.alexandrian.service.so;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for column service objects.
 *
 * @author ollie
 *
 */
@Generated
@Data
@Accessors(chain = true)
public class TypeSO {

	private int sqlType;
	private Integer length;
	private Integer precision;

}