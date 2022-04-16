package ws.datamodel;

import entity.Society;
import entity.Student;

public class MakeStudentLeaderReq {
    
    private Student student;
//    private Society society;
    private String societyIdString;
  
    public MakeStudentLeaderReq() {        
    }

//    public MakeStudentLeaderReq(Student student, Society society) {
    public MakeStudentLeaderReq(Student student, String societyIdString) {
        this.student = student;
//        this.society = society;
        this.societyIdString = societyIdString;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

//    public Society getSociety() {
//        return society;
//    }
//
//    public void setSociety(Society society) {
//        this.society = society;
//    }

    public String getSocietyIdString() {
        return societyIdString;
    }

    public void setSocietyIdString(String societyIdString) {
        this.societyIdString = societyIdString;
    }   
}