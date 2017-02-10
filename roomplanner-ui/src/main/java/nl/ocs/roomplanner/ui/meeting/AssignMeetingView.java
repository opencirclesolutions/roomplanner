package nl.ocs.roomplanner.ui.meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.domain.Location;
import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.domain.Room;
import nl.ocs.roomplanner.service.LocationService;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.service.RoomService;
import nl.ocs.roomplanner.solver.RoomSolver;
import nl.ocs.roomplanner.solver.RoomplanningSolution;
import nl.ocs.roomplanner.solver.SolverSettings;
import nl.ocs.roomplanner.ui.Views;

import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.filter.Compare;
import com.ocs.dynamo.service.MessageService;
import com.ocs.dynamo.ui.ServiceLocator;
import com.ocs.dynamo.ui.component.DefaultHorizontalLayout;
import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.composite.dialog.SimpleModalDialog;
import com.ocs.dynamo.ui.composite.table.InMemoryTreeTable;
import com.ocs.dynamo.ui.view.BaseView;
import com.ocs.dynamo.utils.DateUtils;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = Views.ASSIGN_MEETINGS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class AssignMeetingView extends BaseView {

	private Layout mainLayout;

	@Autowired
	private MessageService messageService;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private LocationService locationService;

	private Map<Location, List<Room>> roomMap = new HashMap<>();

	@Autowired
	private OrganisationService organisationService;

	@Autowired
	private RoomService roomService;

	private List<Location> locations;

	private CheckBox checkEmployee;

	private CheckBox optimizeCost;

	private CheckBox optimizeCapacity;

	private InMemoryTreeTable<Integer, Room, Integer, Location> table;

	private Label notAssignedLabel;

	@Override
	public void enter(ViewChangeEvent event) {
		VerticalLayout main = new DefaultVerticalLayout(true, true);

		mainLayout = new VerticalLayout();
		main.addComponent(mainLayout);

		setCompositionRoot(main);

		final Organisation organisation = (Organisation) VaadinSession.getCurrent().getAttribute("organisation");

		locations = locationService.find(new Compare.Equal("organisation", organisation));
		for (Location lo : locations) {
			List<Room> rooms = roomService.find(new Compare.Equal("location", lo));
			roomMap.put(lo, rooms);
		}

		FormLayout optionFormLayout = new FormLayout();
		optionFormLayout.setMargin(true);
		optionFormLayout.setSpacing(true);

		checkEmployee = new CheckBox("Check employee double booking");
		optionFormLayout.addComponent(checkEmployee);

		optimizeCost = new CheckBox("Optimize Room cost");
		optionFormLayout.addComponent(optimizeCost);

		optimizeCapacity = new CheckBox("Optimize Room capacity");
		optionFormLayout.addComponent(optimizeCapacity);

		HorizontalLayout buttonBar = new DefaultHorizontalLayout(true, true, true);
		optionFormLayout.addComponent(buttonBar);

		buttonBar.addComponent(createSolveButton(main, organisation));
		buttonBar.addComponent(createConfirmButton(main, organisation));

		main.addComponent(optionFormLayout);

		initTable(main, meetingService.fetchByOrganisation(organisation));
	}

	private InMemoryTreeTable<Integer, Room, Integer, Location> createAssignTree(final List<Meeting> meetings,
	        final boolean checkEmployeeDoubleBooking) {

		final List<Date> dates = new ArrayList<>();
		for (Meeting m : meetings) {
			if (!dates.contains(m.getMeetingDate())) {
				dates.add(m.getMeetingDate());
			}
		}
		Collections.sort(dates);

		InMemoryTreeTable<Integer, Room, Integer, Location> table = new InMemoryTreeTable<Integer, Room, Integer, Location>() {

			@Override
			protected void addContainerProperties() {
				addContainerProperty("code", String.class, null);
				addContainerProperty("name", String.class, null);

				for (int i = 0; i < dates.size(); i++) {
					addContainerProperty(DateUtils.formatDate(dates.get(i), "dd-MM-yyyy"), String.class, null);
				}

				List<String> columnHeaders = new ArrayList<>();
				columnHeaders.add("Location/Room Code");
				columnHeaders.add("Location/Room Name");

				for (int i = 0; i < dates.size(); i++) {
					columnHeaders.add(DateUtils.formatDate(dates.get(i), "dd-MM-yyyy"));
				}
				setColumnHeaders(columnHeaders.toArray(new String[0]));
			}

			@Override
			protected String getPreviousColumnId(String columnId) {
				return null;
			}

			@Override
			protected String getCustomStyle(Object itemId, Object propertyId) {
				String error = getMeetingErrorMessage(itemId, propertyId);
				return StringUtils.isEmpty(error) ? null : "warning";
			}

			@Override
			protected void fillChildRow(Object[] row, Room entity, Location parentEntity) {
				row[0] = entity.getCode();
				row[1] = entity.getName() + " (" + entity.getCostPerHour() + " )";

				for (int i = 0; i < dates.size(); i++) {
					Date d = dates.get(i);
					for (Meeting m : meetings) {
						StringBuilder builder = new StringBuilder();
						if (m.getMeetingDate().equals(d) && entity.equals(m.getRoom())) {

							if (builder.length() > 0) {
								builder.append("<br/>");
							}
							builder.append(m.getDescription());
						}

						if (builder.length() > 0) {
							row[2 + i] = builder.toString();
						}
					}
				}

			}

			@Override
			protected void fillParentRow(Object[] row, Location entity) {
				row[0] = entity.getCode();
				row[1] = entity.getName();
			}

			@Override
			protected String[] getColumnstoUpdate(String propertyId) {
				return null;
			}

			@Override
			protected String getKeyPropertyId() {
				return "code";
			}

			@Override
			protected String getReportTitle() {
				return null;
			}

			@Override
			protected boolean isRightAligned(String propertyId) {
				return false;
			}

			@Override
			protected List<Location> getParentCollection() {
				return locations;
			}

			@Override
			protected List<Room> getRowCollection(Location parent) {
				return roomMap.get(parent);
			}

			@Override
			protected Class<?> getEditablePropertyClass(String propertyId) {
				return null;
			}

			@Override
			protected String[] getSumColumns() {
				return new String[0];
			}

			@Override
			protected Number handleChange(String propertyId, String rowId, String parentRowId, String childKey,
			        String parentKey, Object newValue) {
				return 0;
			}

			@Override
			protected boolean isEditable(String propertyId) {
				return false;
			}

			public String getMeetingErrorMessage(Object itemId, Object propertyId) {
				if (propertyId != null && propertyId.toString().matches("\\d+.*")) {
					// date property
					SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
					Date d;
					try {
						d = sf.parse(propertyId.toString());
					} catch (ParseException e) {
						return null;
					}

					String roomCode = (String) getObjectKey(itemId.toString());

					Object parentId = getParent(itemId);
					String parentKey = getObjectKey(parentId.toString());

					for (Location loc : roomMap.keySet()) {
						if (loc.getCode().equals(parentKey)) {
							// location found
							for (Room room : roomMap.get(loc)) {
								if (room.getCode().equals(roomCode)) {
									for (Meeting m : meetings) {
										if (room.equals(m.getRoom()) && m.getMeetingDate().equals(d)) {
											String error = getMeetingErrorString(m, checkEmployeeDoubleBooking,
											        meetings);
											return error == null ? null : error.replaceAll("\n", "<br/>");
										}
									}
								}
							}
						}
					}
				}
				return null;
			}

		};
		table.build();
		table.setItemDescriptionGenerator(new ItemDescriptionGenerator() {

			@Override
			public String generateDescription(Component source, Object itemId, Object propertyId) {
				InMemoryTreeTable<?, ?, ?, ?> table = (InMemoryTreeTable<?, ?, ?, ?>) source;

				if (propertyId != null && propertyId.toString().matches("\\d+.*")) {
					// date property
					SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
					Date d;
					try {
						d = sf.parse(propertyId.toString());
					} catch (ParseException e) {
						return null;
					}

					String roomCode = (String) table.getObjectKey(itemId.toString());

					Object parentId = table.getParent(itemId);
					if (parentId != null) {
						String parentKey = table.getObjectKey(parentId.toString());

						for (Location loc : roomMap.keySet()) {
							if (loc.getCode().equals(parentKey)) {
								// location found
								for (Room room : roomMap.get(loc)) {
									if (room.getCode().equals(roomCode)) {
										for (Meeting m : meetings) {
											if (room.equals(m.getRoom()) && m.getMeetingDate().equals(d)) {
												String error = getMeetingErrorString(m, checkEmployeeDoubleBooking,
												        meetings);
												return error;
											}
										}
									}
								}
							}
						}
					}
				}
				return null;
			}
		});

		return table;
	}

	private Button createConfirmButton(final Layout main, final Organisation organisation) {
		Button confirmButton = new Button("Confirm assignments");
		confirmButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				meetingService.confirmMeetings(organisation);
			}

		});
		return confirmButton;
	}

	private Button createSolveButton(final Layout main, final Organisation organisation) {
		Button solveButton = new Button("Assign meetings");
		solveButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -8368258847189827995L;

			@Override
			public void buttonClick(ClickEvent event) {
				RoomSolver solver = ServiceLocator.getService(RoomSolver.class);

				final SolverSettings settings = new SolverSettings();
				settings.setCheckEmployeeDoubleBooking(checkEmployee.getValue());
				settings.setOptimizeCosts(optimizeCost.getValue());
				settings.setOptimizeCapacity(optimizeCapacity.getValue());

				final RoomplanningSolution solution = solver.solve(organisation, settings);
				final List<Meeting> meetings = meetingService.fetchByOrganisation(organisation);
				initTable(main, meetings);

				SimpleModalDialog dialog = new SimpleModalDialog(false) {

					private static final long serialVersionUID = -8585232168453489812L;

					@Override
					protected String getTitle() {
						return "Scheduling results";
					}

					@Override
					protected void doBuild(Layout parent) {
						StringBuilder builder = new StringBuilder();
						int cost = 0;
						int wasted = 0;

						for (Meeting m : meetings) {
							String temp = getMeetingErrorString(m, settings.isCheckEmployeeDoubleBooking(),
							        solution.getMeetings());
							if (!StringUtils.isEmpty(temp)) {
								builder.append(temp);
							}
							if (m.getRoom() != null) {
								cost += m.getRoom().getCostPerHour();
								wasted += (m.getRoom().getCapacity() - m.getAttendees().size());
							}
						}

						if (solution.getScore().getHardScore() < 0) {
							builder.append("ERROR: NOT ALL MEETINGS COULD BE ASSIGNED!\n");
						} else {
							builder.append("ALL MEETINGS ASSIGNED\n");
						}

						builder.append(String.format("Best score was: %d HARD, %d MEDIUM, %d SOFT", solution.getScore()
						        .getHardScore(), solution.getScore().getMediumScore(), solution.getScore()
						        .getSoftScore()));

						builder.append("\nTotal cost: " + cost);
						builder.append("\nTotal wasted capacity: " + wasted);

						TextArea area = new TextArea();
						area.setValue(builder.toString());
						area.setSizeFull();
						area.setRows(20);

						parent.addComponent(area);

					}
				};
				dialog.build();
				UI.getCurrent().addWindow(dialog);
			}
		});
		return solveButton;
	}

	private String getMeetingErrorString(Meeting m, boolean checkEmployeeDoubleBooking, List<Meeting> allMeetings) {
		StringBuilder builder = new StringBuilder();
		if (m.getRoom() == null) {
			builder.append(String.format("Meeting %s (%d) has not been assigned \n ", m.getDescription(),
			        m.getNumberOfAttendees(), m));
		}

		// location does not match
		if (!m.getRoom().getLocation().equals(m.getDesiredLocation())) {
			builder.append(String.format("Location for meeting %s not correct\n", m.getDescription()));
		}

		// room not luxurious enough
		if (m.getPriorityAsInteger() > m.getRoom().getComfortLevel().intValue()) {
			builder.append(String.format("No suitably comfortable room for meeting %s\n", m.getDescription()));
		}

		if (m.getWhiteboard() && !m.getRoom().getWhiteboard()) {
			builder.append(String.format("No room with a whiteboard available for %s\n", m.getDescription()));
		}

		if (m.getVideoConferencing() && !m.getRoom().getVideoConferencing()) {
			builder.append(String.format("No room with video conferencing equipment available for %s\n",
			        m.getDescription()));
		}

		if (m.getPhoneConferencing() && !m.getRoom().getPhoneConferencing()) {
			builder.append(String.format("No room with audio conferencing equipment available for %s\n",
			        m.getDescription()));
		}

		if (m.getAttendees().size() > m.getRoom().getCapacity()) {
			builder.append(String.format("Not enough capacity (%d needed, %d available) for meeting %s", m
			        .getAttendees().size(), m.getRoom().getCapacity(), m.getDescription()));
		}

		if (checkEmployeeDoubleBooking) {
			for (Employee e : m.getAttendees()) {
				for (Meeting n : allMeetings) {
					if (!n.equals(m) && n.getMeetingDate().equals(m.getMeetingDate())) {
						if (n.getAttendees().contains(e)) {
							builder.append(String.format("%s has conflicting meeting (%s)\n", e.getFullName(),
							        n.getDescription()));
						}
					}
				}
			}

		}
		return builder.toString();
	}

	private void initTable(Layout main, List<Meeting> meetings) {
		if (isMeetingAssigned(meetings)) {

			InMemoryTreeTable<Integer, Room, Integer, Location> newTable = createAssignTree(meetings,
			        checkEmployee.getValue());
			if (notAssignedLabel != null) {
				main.removeComponent(notAssignedLabel);
			}
			main.replaceComponent(table, newTable);
			table = newTable;
		} else {
			Label notAssigned = new Label("No meetings have been assigned yet");
			if (table != null) {
				main.removeComponent(table);
			}
			main.replaceComponent(notAssignedLabel, notAssigned);
			notAssignedLabel = notAssigned;
		}
	}

	private boolean isMeetingAssigned(List<Meeting> meetings) {
		for (Meeting m : meetings) {
			if (m.getDesiredLocation() != null) {
				return true;
			}
		}
		return false;
	}
}
