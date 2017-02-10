//package nl.ocs.roomplanner.ui.charts;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import nl.ocs.roomplanner.domain.Location;
//import nl.ocs.roomplanner.domain.Meeting;
//import nl.ocs.roomplanner.domain.MeetingDTO;
//import nl.ocs.roomplanner.domain.Organisation;
//import nl.ocs.roomplanner.service.MeetingService;
//import nl.ocs.roomplanner.ui.RoomplannerUI;
//import nl.ocs.roomplanner.ui.Views;
//import nl.ocs.ui.Observer;
//import nl.ocs.ui.component.DefaultHorizontalLayout;
//import nl.ocs.ui.component.DefaultVerticalLayout;
//import nl.ocs.ui.view.BaseView;
//import nl.ocs.utils.DateUtils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.model.ChartType;
//import com.vaadin.addon.charts.model.Configuration;
//import com.vaadin.addon.charts.model.DataSeries;
//import com.vaadin.addon.charts.model.DataSeriesItem;
//import com.vaadin.addon.charts.model.ListSeries;
//import com.vaadin.addon.charts.model.XAxis;
//import com.vaadin.addon.charts.model.YAxis;
//import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
//import com.vaadin.server.VaadinSession;
//import com.vaadin.spring.annotation.SpringView;
//import com.vaadin.spring.annotation.UIScope;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Layout;
//import com.vaadin.ui.UI;
//
//@SpringView(name = Views.CHARTS_VIEW)
//@UIScope
//@SuppressWarnings("serial")
//public class ChartsView extends BaseView implements Observer<MeetingDTO> {
//
//	@Autowired
//	private MeetingService meetingService;
//
//	private Layout main;
//
//	private Layout chartLayout;
//
//	@Override
//	public void enter(ViewChangeEvent event) {
//
//		main = new DefaultVerticalLayout(true, true);
//		setCompositionRoot(main);
//
//		chartLayout = constructLayout(new MeetingDTO());
//		main.addComponent(chartLayout);
//
//		((RoomplannerUI) UI.getCurrent()).register(this);
//	}
//
//	private Chart createPieChart(List<Meeting> meetings) {
//		Chart locationChart = new Chart(ChartType.PIE);
//		locationChart.setWidth("400px");
//		locationChart.setHeight("300px");
//
//		Configuration conf = locationChart.getConfiguration();
//		conf.setTitle("Meetings by Location");
//		conf.getLegend().setEnabled(true);
//
//		Map<Location, Integer> meetingsPerLocations = groupMeetingsByLocation(meetings);
//
//		// List<Number> meetingCount = new ArrayList<>();
//
//		DataSeries meetingSeries = new DataSeries("Nr of meetings");
//		for (Location lo : meetingsPerLocations.keySet()) {
//			meetingSeries.add(new DataSeriesItem(lo.getName(), meetingsPerLocations.get(lo)));
//		}
//
//		conf.addSeries(meetingSeries);
//
//		return locationChart;
//	}
//
//	private Layout constructLayout(MeetingDTO dto) {
//		HorizontalLayout layout = new DefaultHorizontalLayout();
//
//		final Organisation organisation = (Organisation) VaadinSession.getCurrent().getAttribute(
//				"organisation");
//		List<Meeting> meetings = filterMeetings(meetingService.fetchByOrganisation(organisation),
//				dto);
//		List<Date> dates = getUniqueMeetingDates(meetings);
//
//		Chart chart = new Chart(ChartType.BAR);
//		chart.setWidth("400px");
//		chart.setHeight("300px");
//
//		// Modify the default configuration a bit
//		Configuration conf = chart.getConfiguration();
//		conf.setTitle("Meetings by Date");
//		// conf.setSubTitle("The bigger they are the harder they pull");
//		conf.getLegend().setEnabled(true); // Disable legend
//		// The data
//
//		Map<Date, Integer> meetingsPerDate = groupMeetingsByDate(meetings);
//		Map<Date, Integer> employeesPerDate = groupAttendeesByDate(meetings);
//
//		List<Number> meetingCount = new ArrayList<>();
//		List<Number> employeeCount = new ArrayList<>();
//
//		for (Date d : dates) {
//			meetingCount.add(meetingsPerDate.get(d));
//			employeeCount.add(employeesPerDate.get(d));
//		}
//
//		ListSeries meetingSeries = new ListSeries("Nr of meetings");
//		meetingSeries.setData(meetingCount);
//		conf.addSeries(meetingSeries);
//
//		ListSeries employeeSeries = new ListSeries("Nr of employees");
//		employeeSeries.setData(employeeCount);
//		conf.addSeries(employeeSeries);
//
//		// Set the category labels on the axis correspondingly
//		XAxis xaxis = new XAxis();
//
//		List<String> dateStrings = new ArrayList<>();
//		for (Date date : dates) {
//			dateStrings.add(DateUtils.formatDate(date, "dd-MM-yyyy"));
//		}
//
//		xaxis.setCategories(dateStrings.toArray(new String[0]));
//		xaxis.setTitle("Meeting dates");
//		conf.addxAxis(xaxis);
//
//		// Set the Y axis title
//		YAxis yaxis = new YAxis();
//		yaxis.setTitle("Numbers");
//		yaxis.getLabels().setStep(2);
//		yaxis.setExtremes(0, 25);
//		conf.addyAxis(yaxis);
//
//		layout.addComponent(chart);
//
//		Chart locationChart = createPieChart(meetings);
//		layout.addComponent(locationChart);
//
//		Chart costChart = createCostChart(meetings);
//		layout.addComponent(costChart);
//		return layout;
//	}
//
//	private Chart createCostChart(List<Meeting> meetings) {
//		Chart locationChart = new Chart(ChartType.LINE);
//		locationChart.setWidth("400px");
//		locationChart.setHeight("400px");
//
//		Configuration conf = locationChart.getConfiguration();
//		conf.setTitle("Cost per day");
//		conf.getLegend().setEnabled(true);
//
//		Map<Date, Integer> meetingCostPerDate = groupMeetingCostByDay(meetings);
//		Map<Date, Integer> meetingsPerDate = groupMeetingsByDate(meetings);
//		List<Date> dates = getUniqueMeetingDates(meetings);
//
//		List<String> dateStrings = new ArrayList<>();
//		for (Date date : dates) {
//			dateStrings.add(DateUtils.formatDate(date, "dd-MM-yyyy"));
//		}
//
//		XAxis xaxis = new XAxis();
//		xaxis.setCategories(dateStrings.toArray(new String[0]));
//		xaxis.setTitle("Date");
//		conf.addxAxis(xaxis);
//
//		List<Number> cost = new ArrayList<>();
//		List<Number> avgCost = new ArrayList<>();
//
//		for (Date d : dates) {
//			cost.add(meetingCostPerDate.get(d));
//
//			Integer costPerDate = meetingCostPerDate.get(d);
//			Integer nrOfMeetings = meetingsPerDate.get(d);
//			double avg = (costPerDate * 1.) / (1. * nrOfMeetings);
//			avgCost.add((int) avg);
//		}
//
//		ListSeries costSeries = new ListSeries("Total cost");
//		costSeries.setData(cost);
//		conf.addSeries(costSeries);
//
//		ListSeries avgCostSeries = new ListSeries("Average cost");
//		avgCostSeries.setData(avgCost);
//		conf.addSeries(avgCostSeries);
//
//		YAxis yaxis = new YAxis();
//		yaxis.setTitle("Cost");
//		yaxis.setExtremes(0, 1250);
//		conf.addyAxis(yaxis);
//
//		return locationChart;
//	}
//
//	private List<Date> getUniqueMeetingDates(List<Meeting> meetings) {
//		List<Date> result = new ArrayList<>();
//
//		for (Meeting m : meetings) {
//			if (!result.contains(m.getMeetingDate())) {
//				result.add(m.getMeetingDate());
//			}
//		}
//		Collections.sort(result);
//
//		return result;
//	}
//
//	private Map<Date, Integer> groupMeetingsByDate(List<Meeting> meetings) {
//		Map<Date, Integer> result = new HashMap<>();
//
//		for (Meeting m : meetings) {
//			if (!result.containsKey(m.getMeetingDate())) {
//				result.put(m.getMeetingDate(), 0);
//			}
//
//			result.put(m.getMeetingDate(), result.get(m.getMeetingDate()) + 1);
//		}
//
//		return result;
//	}
//
//	private Map<Date, Integer> groupMeetingCostByDay(List<Meeting> meetings) {
//		Map<Date, Integer> result = new HashMap<>();
//
//		for (Meeting m : meetings) {
//			if (!result.containsKey(m.getMeetingDate())) {
//				result.put(m.getMeetingDate(), 0);
//			}
//
//			result.put(m.getMeetingDate(), result.get(m.getMeetingDate())
//					+ (m.getRoom().getCostPerHour() == null ? 0 : m.getRoom().getCostPerHour()));
//		}
//
//		return result;
//	}
//
//	private Map<Location, Integer> groupMeetingsByLocation(List<Meeting> meetings) {
//		Map<Location, Integer> result = new HashMap<>();
//
//		for (Meeting m : meetings) {
//			if (!result.containsKey(m.getDesiredLocation())) {
//				result.put(m.getDesiredLocation(), 0);
//			}
//
//			result.put(m.getDesiredLocation(), result.get(m.getDesiredLocation()) + 1);
//		}
//
//		return result;
//	}
//
//	private Map<Date, Integer> groupAttendeesByDate(List<Meeting> meetings) {
//		Map<Date, Integer> result = new HashMap<>();
//
//		for (Meeting m : meetings) {
//			if (!result.containsKey(m.getMeetingDate())) {
//				result.put(m.getMeetingDate(), 0);
//			}
//
//			result.put(m.getMeetingDate(),
//					result.get(m.getMeetingDate()) + m.getNumberOfAttendees());
//		}
//
//		return result;
//	}
//
//	@Override
//	public void notify(MeetingDTO entity) {
//		Layout newLayout = constructLayout(entity);
//		main.replaceComponent(chartLayout, newLayout);
//		chartLayout = newLayout;
//	}
//
//	private List<Meeting> filterMeetings(List<Meeting> input, MeetingDTO dto) {
//		List<Meeting> result = new ArrayList<>();
//
//		for (Meeting m : input) {
//			boolean from = dto.getFrom() == null
//					|| dto.getFrom().compareTo(m.getMeetingDate()) <= 0;
//			boolean to = dto.getTo() == null || dto.getTo().compareTo(m.getMeetingDate()) >= 0;
//			if (from && to) {
//				result.add(m);
//			}
//		}
//
//		return result;
//
//	}
//}
