package nl.ocs.roomplanner.ui.meeting;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.ui.Views;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.SimpleSearchLayout;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.BaseView;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = Views.SEARCH_MEETINGS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class SearchMeetingView extends BaseView {

	private Layout mainLayout;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private OrganisationService organisationService;

	@Override
	public void enter(ViewChangeEvent event) {
		VerticalLayout main = new DefaultVerticalLayout(true, true);

		mainLayout = new VerticalLayout();
		main.addComponent(mainLayout);

		setCompositionRoot(main);

		FormOptions fo = new FormOptions();
		fo.setShowEditButton(true);
		fo.setShowRemoveButton(true);

		SimpleSearchLayout<Integer, Meeting> searchLayout = new SimpleSearchLayout<>(
				meetingService, getModelFactory().getModel(Meeting.class), QueryType.ID_BASED, fo,
				new SortOrder("description", SortDirection.ASCENDING));
		searchLayout.setDividerProperty("meetingDate");
		searchLayout.addFieldEntityModel("desiredLocation", "SearchMeetingLocation");
		
		mainLayout.addComponent(searchLayout);
	}
}
