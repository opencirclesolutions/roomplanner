package nl.ocs.roomplanner.ui.meeting;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.SimpleSearchLayout;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.ui.Views;

@SpringView(name = Views.SEARCH_MEETINGS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class SearchMeetingView extends LazyBaseView {

    @Autowired
    private MeetingService meetingService;

    private SimpleSearchLayout<Integer, Meeting> searchLayout;

    @Override
    public Component build() {

        FormOptions fo = new FormOptions().setEditAllowed(true).setShowRemoveButton(true);
        searchLayout = new SimpleSearchLayout<Integer, Meeting>(meetingService,
                getModelFactory().getModel(Meeting.class), QueryType.ID_BASED, fo,
                new SortOrder("description", SortDirection.ASCENDING)) {

        };
        searchLayout.setDividerProperty("meetingDate");

        return searchLayout;
    }

    @Override
    protected void refresh() {
        searchLayout.search();
    }
}
