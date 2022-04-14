package ws.datamodel;

import entity.Student;
import java.util.ArrayList;
import java.util.List;

public class CreateStudentReq {
    
    private Student student;
    private List<Long> listOfSocietyIdsToBeLeaderOf;
  
    public CreateStudentReq() {        
    }

    public CreateStudentReq(Student student, List<Long> listOfSocietyIdsToBeLeaderOf) {
        this.student = student;
        this.listOfSocietyIdsToBeLeaderOf = new ArrayList<Long>();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Long> getListOfSocietyIdsToBeLeaderOf() {
        return listOfSocietyIdsToBeLeaderOf;
    }

    public void setListOfSocietyIdsToBeLeaderOf(List<Long> listOfSocietyIdsToBeLeaderOf) {
        this.listOfSocietyIdsToBeLeaderOf = listOfSocietyIdsToBeLeaderOf;
    }
}