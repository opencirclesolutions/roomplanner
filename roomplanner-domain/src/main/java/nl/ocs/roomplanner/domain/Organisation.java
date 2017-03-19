package nl.ocs.roomplanner.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.annotation.Attribute;
import com.ocs.dynamo.domain.model.annotation.Model;

@Model(displayProperty = "name")
@Entity
@Table(name = "organisations")
public class Organisation extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 6987287020622631660L;

	@Id
	@SequenceGenerator(name = "organisations_id_seq", sequenceName = "organisations_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organisations_id_seq")
	private Integer id;

	@NotNull
	@Size(max = 100)
	private String name;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
