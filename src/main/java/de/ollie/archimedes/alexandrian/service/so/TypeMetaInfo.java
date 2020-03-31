package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for type meta information.
 * 
 * @author Oliver.Lieshoff
 *
 */
@Accessors(chain = true)
@Data
@Generated
public class TypeMetaInfo {

	private List<OptionSO> options = new ArrayList<>();

}