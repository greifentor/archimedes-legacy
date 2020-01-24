package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for table meta information like stereo types and options.
 *
 * @author ollie
 *
 */
@Generated
@Data
@Accessors(chain = true)
public class TableMetaInfo {

	private List<OptionSO> options = new ArrayList<>();
	private List<StereotypeSO> stereotypes = new ArrayList<>();

}