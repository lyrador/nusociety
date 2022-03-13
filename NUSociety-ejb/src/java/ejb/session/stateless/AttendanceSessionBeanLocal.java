/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Attendance;
import java.util.List;
import javax.ejb.Local;
import util.exception.AttendanceNotFoundException;

/**
 *
 * @author 65912
 */
@Local
public interface AttendanceSessionBeanLocal {

    public List<Attendance> retrieveAllAttendances();

    public Attendance retrieveAttendanceByAttendanceId(Long attendanceId) throws AttendanceNotFoundException;

    public Long createNewAttendance(Attendance newAttendance);

    public void deleteAttendance(Long attendanceId) throws AttendanceNotFoundException;

    public void updateAttendance(Attendance tempAttendance) throws AttendanceNotFoundException;

}
