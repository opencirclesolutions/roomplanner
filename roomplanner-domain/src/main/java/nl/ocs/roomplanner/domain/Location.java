package nl.ocs.roomplanner.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.VisibilityType;
import com.ocs.dynamo.domain.model.annotation.Attribute;
import com.ocs.dynamo.domain.model.annotation.AttributeOrder;
import com.ocs.dynamo.domain.model.annotation.Model;

@Model(displayProperty = "codeAndName", sortOrder = "code ASC")
@Entity
@Table(name = "locations")
@AttributeOrder(attributeNames = { "code", "name", "street", "houseNumber", "city" })
public class Location extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "locations_id_seq", sequenceName = "locations_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locations_id_seq")
	private Integer id;

	@NotNull
	@Size(max = 20)
	@Attribute(searchable = true)
	private String code;

	@NotNull
	@Size(max = 100)
	@Attribute(searchable = true)
	private String name;

	@NotNull
	@Size(max = 100)
	private String street;

	@NotNull
	@Size(max = 20)
	@Column(name = "house_number")
	private String houseNumber;

	@NotNull
	@Size(max = 50)
	private String city;

	@OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
	private Set<Room> rooms = new HashSet<>();

	@JoinColumn(name = "organisation_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Organisation organisation;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Room addRoom(Room room) {
		this.rooms.add(room);
		room.setLocation(this);
		return room;
	}

	public Room removeRoom(Room room) {
		this.rooms.remove(room);
		room.setLocation(null);
		return room;
	}

	@Attribute(readOnly = true, visible = VisibilityType.HIDE, main = true)
	public String getCodeAndName() {
		return this.getCode() + " " + this.getName();
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

}
