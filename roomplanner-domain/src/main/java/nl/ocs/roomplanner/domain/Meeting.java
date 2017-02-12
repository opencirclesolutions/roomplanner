package nl.ocs.roomplanner.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import nl.ocs.roomplanner.domain.comparator.MeetingSizeComparator;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.AttributeSelectMode;
import com.ocs.dynamo.domain.model.VisibilityType;
import com.ocs.dynamo.domain.model.annotation.Attribute;
import com.ocs.dynamo.domain.model.annotation.AttributeOrder;
import com.ocs.dynamo.domain.model.annotation.Model;
import com.ocs.dynamo.domain.validator.URL;

@PlanningEntity(difficultyComparatorClass = MeetingSizeComparator.class)
@Entity
@Table(name = "meetings")
@AttributeOrder(attributeNames = { "description", "desiredLocation", "meetingDate", "startTime", "endTime",
        "attendees", "whiteboard", "videoConferencing", "phoneConferencing", "priority" })
@Model(displayProperty = "description")
public class Meeting extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 8516781577227355815L;

	@Id
	@SequenceGenerator(name = "meetings_id_seq", sequenceName = "meetings_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meetings_id_seq")
	private Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 200)
	@Attribute(searchable = true)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "desired_location_id")
	@Attribute(complexEditable = true, searchable = true, selectMode = AttributeSelectMode.LOOKUP, displayName = "Location")
	private Location desiredLocation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_location_id")
	private Location assignedLocation;

	@Attribute(searchable = true, trueRepresentation = "You bet", falseRepresentation = "No way")
	private Boolean whiteboard = Boolean.FALSE;

	@Column(name = "video_conferencing")
	private Boolean videoConferencing = Boolean.FALSE;

	@Column(name = "phone_conferencing")
	private Boolean phoneConferencing = Boolean.FALSE;

	@Temporal(TemporalType.TIME)
	@Column(name = "start_time")
	private Date startTime;

	@Temporal(TemporalType.TIME)
	@Column(name = "end_time")
	private Date endTime;

	@NotNull
	@Column(name = "priority")
	private PriorityType priority;

	@Attribute(defaultValue = "01-01-2016")
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "meeting_date")
	private Date meetingDate;

	@Attribute(readOnly = true)
	private Boolean assigned = Boolean.FALSE;

	@Attribute(complexEditable = true, searchable = true)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "meetings_employees", joinColumns = @JoinColumn(name = "meeting_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
	private Set<Employee> attendees = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "room_id")
	private Room room;

	@Attribute(visible = VisibilityType.HIDE)
	@Column(name = "exchange_item_id")
	private String exchangeItemId;

	@Attribute(visible = VisibilityType.HIDE)
	@Column(name = "exchange_change_key")
	private String exchangeChangeKey;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisation_id")
	@Attribute(searchable = true, multipleSearch = true)
	private Organisation organisation;

	@Attribute(readOnly = true)
	private Boolean confirmed = Boolean.FALSE;

	@URL
	@Attribute(showInTable = VisibilityType.SHOW, url = true)
	@Size(max = 255)
	private String url;

	public Location getDesiredLocation() {
		return desiredLocation;
	}

	public void setDesiredLocation(Location desiredLocation) {
		this.desiredLocation = desiredLocation;
	}

	public Location getAssignedLocation() {
		return assignedLocation;
	}

	public void setAssignedLocation(Location assignedLocation) {
		this.assignedLocation = assignedLocation;
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

	public PriorityType getPriority() {
		return priority;
	}

	public void setPriority(PriorityType priority) {
		this.priority = priority;
	}

	public Boolean getAssigned() {
		return assigned;
	}

	public void setAssigned(Boolean assigned) {
		this.assigned = assigned;
	}

	public Employee addAttendee(Employee employee) {
		this.attendees.add(employee);
		return employee;
	}

	public Employee removeAttendee(Employee employee) {
		this.attendees.remove(employee);
		return employee;
	}

	public Set<Employee> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<Employee> attendees) {
		this.attendees = attendees;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Attribute(readOnly = true, sortable = false)
	public Integer getNumberOfAttendees() {
		return attendees == null ? 0 : attendees.size();
	}

	@PlanningVariable(valueRangeProviderRefs = { "roomRange" })
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public String getExchangeItemId() {
		return exchangeItemId;
	}

	public void setExchangeItemId(String exchangeItemId) {
		this.exchangeItemId = exchangeItemId;
	}

	/**
	 * The meeting ID as a string (for Optaplanner purposes)
	 * 
	 * @return
	 */
	@Attribute(visible = VisibilityType.HIDE)
	public String getMeetingId() {
		return Integer.toString(id);
	}

	@Attribute(visible = VisibilityType.HIDE)
	public int getPriorityAsInteger() {
		return priority.ordinal() + 1;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public String getExchangeChangeKey() {
		return exchangeChangeKey;
	}

	public void setExchangeChangeKey(String exchangeChangeKey) {
		this.exchangeChangeKey = exchangeChangeKey;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
