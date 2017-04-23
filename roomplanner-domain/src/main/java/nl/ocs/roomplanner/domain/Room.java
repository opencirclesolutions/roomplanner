package nl.ocs.roomplanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.NumberSelectMode;
import com.ocs.dynamo.domain.model.annotation.Attribute;
import com.ocs.dynamo.domain.model.annotation.AttributeOrder;
import com.ocs.dynamo.domain.model.annotation.Model;

@Model(displayProperty = "name")
@Entity
@Table(name = "rooms")
@AttributeOrder(attributeNames = { "code", "name", "location", "comfortLevel", "capacity", "costPerHour",
        "phoneConferencing", "videoConferencing", "whiteboard" })
public class Room extends AbstractEntity<Integer> {

    private static final long serialVersionUID = -6342590031746525068L;

    @Id
    @SequenceGenerator(name = "rooms_id_seq", sequenceName = "rooms_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rooms_id_seq")
    private Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 20)
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Attribute(numberSelectMode = NumberSelectMode.SLIDER)
    @Min(0)
    @Max(100)
    private Integer capacity;

    @NotNull
    @Attribute(displayName = "Cost")
    @Column(name = "cost_per_hour")
    private Integer costPerHour;

    private Boolean whiteboard = Boolean.FALSE;

    @Column(name = "video_conferencing")
    private Boolean videoConferencing = Boolean.FALSE;

    @Column(name = "phone_conferencing")
    private Boolean phoneConferencing = Boolean.FALSE;

    @Min(0)
    @Max(10)
    @NotNull
    @Column(name = "confort_level")
    private Integer comfortLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @Attribute(complexEditable = true)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(Integer costPerHour) {
        this.costPerHour = costPerHour;
    }

    public Boolean getWhiteboard() {
        return whiteboard;
    }

    public void setWhiteboard(Boolean whiteboard) {
        this.whiteboard = whiteboard;
    }

    public Boolean getVideoConferencing() {
        return videoConferencing;
    }

    public void setVideoConferencing(Boolean videoConferencing) {
        this.videoConferencing = videoConferencing;
    }

    public Boolean getPhoneConferencing() {
        return phoneConferencing;
    }

    public void setPhoneConferencing(Boolean phoneConferencing) {
        this.phoneConferencing = phoneConferencing;
    }

    public Integer getComfortLevel() {
        return comfortLevel;
    }

    public void setComfortLevel(Integer comfortLevel) {
        this.comfortLevel = comfortLevel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

}
