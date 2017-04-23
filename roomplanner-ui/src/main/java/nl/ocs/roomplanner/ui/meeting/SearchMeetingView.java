package nl.ocs.roomplanner.ui.meeting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.filter.FlexibleFilterDefinition;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.FlexibleSearchLayout;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.ui.Views;

@SpringView(name = Views.SEARCH_MEETINGS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class SearchMeetingView extends LazyBaseView {

    @Autowired
    private MeetingService meetingService;

    private FlexibleSearchLayout<Integer, Meeting> searchLayout;

    @Override
    public Component build() {

        EntityModel<Meeting> model = getModelFactory().getModel(Meeting.class);

        FormOptions fo = new FormOptions().setEditAllowed(true).setShowRemoveButton(true).setShowIterationButtons(true)
                .setSearchImmediately(true);
        searchLayout = new FlexibleSearchLayout<Integer, Meeting>(meetingService, model, QueryType.PAGING, fo,
                new SortOrder("description", SortDirection.ASCENDING)) {

//            @Override
//            protected void postProcessLayout(Layout main) {
//                FlexibleFilterDefinition fd = new FlexibleFilterDefinition();
//                fd.setAttributeModel(model.getAttributeModel("meetingDate"));
//                fd.setFlexibleFilterType(FlexibleFilterType.BETWEEN);
//                fd.setValue(LocalDate.now());
//                fd.setValueTo(LocalDate.now().plusDays(7));
//
//                FlexibleFilterDefinition fd2 = new FlexibleFilterDefinition();
//                fd2.setAttributeModel(model.getAttributeModel("startTime"));
//                fd2.setFlexibleFilterType(FlexibleFilterType.BETWEEN);
//                fd2.setValue(LocalTime.now());
//
//                FlexibleFilterDefinition fd3 = new FlexibleFilterDefinition();
//                fd3.setAttributeModel(model.getAttributeModel("startDateTime"));
//                fd3.setFlexibleFilterType(FlexibleFilterType.BETWEEN);
//                fd3.setValue(LocalDateTime.now());
//
//                getSearchForm().restoreFilterDefinitions(Lists.newArrayList(fd, fd2, fd3));
//            }

            @Override
            protected void postProcessButtonBar(Layout buttonBar) {
                Button button = new Button("Extract");
                button.addClickListener(event -> {
                    List<FlexibleFilterDefinition> def = getSearchForm().extractFilterDefinitions();
                    def.stream().forEach(x -> System.out.println(x));
                });
                buttonBar.addComponent(button);
            }
        };
        searchLayout.setDividerProperty("meetingDate");
        searchLayout.setMaxResults(10);

        return searchLayout;
    }

    @Override
    protected void refresh() {
        searchLayout.search();
    }
}
