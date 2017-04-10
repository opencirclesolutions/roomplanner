package nl.ocs.roomplanner.solver;

import java.util.List;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.domain.Room;
import nl.ocs.roomplanner.service.EmployeeService;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.service.RoomService;

import org.apache.log4j.Logger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocs.dynamo.dao.SortOrder;
import com.ocs.dynamo.dao.query.FetchJoinInformation;
import com.ocs.dynamo.filter.Compare;

/**
 * 
 * @author bas.rutten
 *
 */
@Service("roomSolver")
public class RoomSolver {

    private static final Logger LOGGER = Logger.getLogger(RoomSolver.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private EmployeeService employeeService;

    public RoomplanningSolution solve(Organisation organisation, SolverSettings settings) {
        List<Integer> roomIds = roomService.findIds(new Compare.Equal("organisation", organisation),
                (SortOrder[]) null);
        List<Room> rooms = roomService.fetchByIds(roomIds,
                new FetchJoinInformation[] { new FetchJoinInformation("location") });

        // find all meetings
        List<Integer> ids = meetingService.findIds(new Compare.Equal("organisation", organisation), (SortOrder[]) null);
        List<Meeting> meetings = meetingService.fetchByIds(ids, new FetchJoinInformation[] {
                new FetchJoinInformation("attendees"), new FetchJoinInformation("desiredLocation") });

        RoomplanningSolution solution = new RoomplanningSolution(rooms, meetings, employeeService.findAll());
        solution.setSolverSettings(settings);
        SolverFactory<RoomplanningSolution> factory = SolverFactory
                .createFromXmlResource("optaplanner/solverConfig.xml");

        Solver<RoomplanningSolution> solver = factory.buildSolver();
        solver.solve(solution);

        RoomplanningSolution best = solver.getBestSolution();

        int cost = best.getMeetings().stream().filter(m -> m.getRoom() != null).mapToInt(m -> m.getRoom().getCostPerHour()).sum();
        LOGGER.info("Total cost: " + cost);

        int wasted = best.getMeetings().stream().filter(m -> m.getRoom() != null)
                .mapToInt(m -> m.getRoom().getCapacity() - m.getAttendees().size()).sum();
        LOGGER.info("Total wasted capacity: " + wasted);

        List<Meeting> savedMeetings = meetingService.save(best.getMeetings());
        best.setMeetings(savedMeetings);

        return best;
    }
}
