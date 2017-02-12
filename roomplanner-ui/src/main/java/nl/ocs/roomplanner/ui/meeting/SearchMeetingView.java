package nl.ocs.roomplanner.ui.meeting;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.ui.Views;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.exception.OCSValidationException;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.SimpleSearchLayout;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;

@SpringView(name = Views.SEARCH_MEETINGS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class SearchMeetingView extends LazyBaseView {

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private OrganisationService organisationService;

	private SimpleSearchLayout<Integer, Meeting> searchLayout;

	@Override
	public Component build() {

		FormOptions fo = new FormOptions().setShowEditButton(true).setShowRemoveButton(true);
		searchLayout = new SimpleSearchLayout<Integer, Meeting>(meetingService, getModelFactory().getModel(
		        Meeting.class), QueryType.ID_BASED, fo, new SortOrder("description", SortDirection.ASCENDING)) {

			@Override
			public void validateBeforeSearch() {
				if (!isFilterSet("desiredLocation")) {
					throw new OCSValidationException("Location is required");
				}
			}

		};
		searchLayout.setDividerProperty("meetingDate");

		return searchLayout;
	}

	@Override
	protected void refresh() {
		searchLayout.search();
	}
}
