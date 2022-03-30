/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AnnouncementSessionBeanLocal;
import ejb.session.stateless.AttendanceSessionBeanLocal;
import ejb.session.stateless.SocietySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Attendance;
import entity.Society;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import util.enumeration.AccessRightEnum;
import util.exception.AttendanceNotFoundException;
import util.exception.SocietyNotFoundException;
import util.exception.StudentNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeeda
 */
@Named(value = "rosterManagementManagedBean")
@ViewScoped
public class RosterManagementManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    @EJB
    private SocietySessionBeanLocal societySessionBeanLocal;
    @EJB
    private AttendanceSessionBeanLocal attendanceSessionBeanLocal;

    private List<Student> students;
    private Student newStudent;
    private Student studentToUpdate;
    private Student studentToDelete;
    private Society currentSociety;
    private Attendance tempAttendance;
    private Attendance viewedAttendance;
    private HashMap<Long, Attendance> attendances;
    private String currentValue;
    private AccessRightEnum leaderAccessRightEnum;
    private Student studentToAdd;

    public RosterManagementManagedBean() {
        newStudent = new Student();
        studentToUpdate = new Student();
        studentToDelete = new Student();
        studentToAdd = new Student();
        leaderAccessRightEnum = AccessRightEnum.LEADER;
    }

    @PostConstruct
    public void postConstruct() {
        String societyId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("societyId");
        try {
            currentSociety = societySessionBeanLocal.retrieveSocietyById(Long.parseLong(societyId));
        } catch (SocietyNotFoundException ex) {
            ex.getMessage();
        }
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
    }

    public void createNewStudent(ActionEvent event) {
        Long newStudentId = studentSessionBeanLocal.createNewStudent(newStudent);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New student created: " + newStudentId, "New student created: " + newStudentId));
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
    }

    public void addStudent(ActionEvent event) throws StudentNotFoundException, SocietyNotFoundException {
        studentToAdd = studentSessionBeanLocal.retrieveStudentByEmail(txt1);
        if (!students.contains(studentToAdd)) {
            societySessionBeanLocal.addStudentToSociety(studentToAdd.getStudentId(), currentSociety.getSocietyId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New student added: " + studentToAdd.getStudentId(), "New student added: " + studentToAdd.getStudentId()));
            Attendance newAttendance = new Attendance(1, 1);
            newAttendance.setStudent(studentToAdd);
            attendanceSessionBeanLocal.createNewAttendance(newAttendance);
            this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
            attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while adding student, student is already in society!", null));
        }
    }

    public void updateStudent(ActionEvent event) throws StudentNotFoundException {
        studentSessionBeanLocal.updateStudent(studentToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student updated: " + studentToUpdate, "Student updated: " + studentToUpdate));
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
    }

    public void deleteStudent(ActionEvent event) {
        try {
            studentSessionBeanLocal.deleteStudent(studentToDelete.getStudentId());
            students.remove(studentToDelete);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student deleted successfully", null));
        } catch (StudentNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting student: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
    }

    public void removeStudent(ActionEvent event) {
        try {
            Student studentToRemove = (Student) event.getComponent().getAttributes().get("studentToRemove");
            societySessionBeanLocal.removeStudentFromSociety(currentSociety.getSocietyId(), studentToRemove.getStudentId());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Student removed successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
    }

    public void incrementEveryonesAttendedCountByOne(ActionEvent event) {
        try {
            for (Student student : students) {
                Attendance currentAttendance = attendanceSessionBeanLocal.retrieveAttendanceFromStudentIdAndSocietyId(student.getStudentId(), currentSociety.getSocietyId());
                currentAttendance.setAttendedCount(currentAttendance.getAttendedCount() + 1);
                attendanceSessionBeanLocal.updateAttendance(currentAttendance);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "All attended counts incremented successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
    }

    public void incrementEveryonesTotalCountByOne(ActionEvent event) {
        try {
            for (Student student : students) {
                Attendance currentAttendance = attendanceSessionBeanLocal.retrieveAttendanceFromStudentIdAndSocietyId(student.getStudentId(), currentSociety.getSocietyId());
                currentAttendance.setTotalCount(currentAttendance.getTotalCount() + 1);
                attendanceSessionBeanLocal.updateAttendance(currentAttendance);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "All total counts incremented successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
    }

    public void setAttendance(ActionEvent event) {
        try {
            Student tempStudentToUpdate = (Student) event.getComponent().getAttributes().get("studentToUpdate");
            System.out.println(tempStudentToUpdate.getEmail());
            Society currentSociety = societySessionBeanLocal.retrieveSocietyById(this.currentSociety.getSocietyId());
            tempAttendance = attendanceSessionBeanLocal.retrieveAttendanceFromStudentIdAndSocietyId(tempStudentToUpdate.getStudentId(), currentSociety.getSocietyId());
            DecimalFormat df = new DecimalFormat("#.##");
            currentValue = tempAttendance.getAttendedCount() + " / " + tempAttendance.getTotalCount() + " (" + df.format(tempAttendance.getAttendanceRate()) + "%)";
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
    }

    public void updateAttendance(ActionEvent event) throws AttendanceNotFoundException {
        attendanceSessionBeanLocal.updateAttendance(tempAttendance);
        this.students = studentSessionBeanLocal.retrieveAllStudentsFromSocietyId(currentSociety.getSocietyId());
        attendances = attendanceSessionBeanLocal.retrieveMapAttendancesFromStudentListAndSocietyId(students, currentSociety.getSocietyId());
        DecimalFormat df = new DecimalFormat("#.##");
        currentValue = tempAttendance.getAttendedCount() + " / " + tempAttendance.getTotalCount() + " (" + df.format(tempAttendance.getAttendanceRate()) + "%)";
        tempAttendance = new Attendance();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Attendance updated: " + tempAttendance, "Attendance updated: " + tempAttendance));
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Student getStudentToUpdate() {
        return studentToUpdate;
    }

    public void setStudentToUpdate(Student studentToUpdate) {
        this.studentToUpdate = studentToUpdate;
    }

    public Student getStudentToDelete() {
        return studentToDelete;
    }

    public void setStudentToDelete(Student studentToDelete) {
        this.studentToDelete = studentToDelete;
    }

    public Student getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }

    public Attendance getTempAttendance() {
        return tempAttendance;
    }

    public void setTempAttendance(Attendance tempAttendance) {
        this.tempAttendance = tempAttendance;
    }

    public HashMap<Long, Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(HashMap<Long, Attendance> attendances) {
        this.attendances = attendances;
    }

    public Attendance getViewedAttendance() {
        return viewedAttendance;
    }

    public void setViewedAttendance(Attendance viewedAttendance) {
        this.viewedAttendance = viewedAttendance;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public AccessRightEnum getLeaderAccessRightEnum() {
        return leaderAccessRightEnum;
    }

    private String txt1;
    private String txt2;
    private String txt3;
    private String txt4;
    private String txt5;
    private String txt6;
    private String txt7;
    private String txt8;
    private String txt9;
    private String txt10;
    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;
    private Student student5;
    private List<Student> selectedStudents;

    public List<String> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<String> studentViewList = new ArrayList<>();
        List<Student> students = studentSessionBeanLocal.retrieveAllStudents();
        for (Student student : students) {
            studentViewList.add(student.getEmail());
        }

        return studentViewList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }

    public List<String> noResults(String query) {
        return Collections.EMPTY_LIST;
    }

    public List<Student> completeStudent(String query) {
        String queryLowerCase = query.toLowerCase();
        List<Student> students = studentSessionBeanLocal.retrieveAllStudents();
        return students.stream().filter(t -> t.getEmail().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }

    public void onItemSelect(SelectEvent<String> event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Student Selected", event.getObject()));
    }

    public void onEmptyMessageSelect() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Empty message selected"));
    }

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
    }

    public String getTxt3() {
        return txt3;
    }

    public void setTxt3(String txt3) {
        this.txt3 = txt3;
    }

    public String getTxt4() {
        return txt4;
    }

    public void setTxt4(String txt4) {
        this.txt4 = txt4;
    }

    public String getTxt5() {
        return txt5;
    }

    public void setTxt5(String txt5) {
        this.txt5 = txt5;
    }

    public String getTxt6() {
        return txt6;
    }

    public void setTxt6(String txt6) {
        this.txt6 = txt6;
    }

    public String getTxt7() {
        return txt7;
    }

    public void setTxt7(String txt7) {
        this.txt7 = txt7;
    }

    public String getTxt8() {
        return txt8;
    }

    public void setTxt8(String txt8) {
        this.txt8 = txt8;
    }

    public String getTxt9() {
        return txt9;
    }

    public void setTxt9(String txt9) {
        this.txt9 = txt9;
    }

    public String getTxt10() {
        return txt10;
    }

    public void setTxt10(String txt10) {
        this.txt10 = txt10;
    }

    public Student getStudent1() {
        return student1;
    }

    public void setStudent1(Student student1) {
        this.student1 = student1;
    }

    public Student getStudent2() {
        return student2;
    }

    public void setStudent2(Student student2) {
        this.student2 = student2;
    }

    public Student getStudent3() {
        return student3;
    }

    public void setStudent3(Student student3) {
        this.student3 = student3;
    }

    public Student getStudent4() {
        return student4;
    }

    public void setStudent4(Student student4) {
        this.student4 = student4;
    }

    public Student getStudent5() {
        return student5;
    }

    public void setStudent5(Student student5) {
        this.student5 = student5;
    }

    public List<Student> getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(List<Student> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public char getStudentGroup(Student student) {
        return student.getEmail().charAt(0);
    }
}
