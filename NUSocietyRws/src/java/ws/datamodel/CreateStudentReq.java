package ws.datamodel;

import entity.Student;

public class CreateStudentReq {
    
    private Student student;
    private String accessRightString;
  
    public CreateStudentReq() {        
    }

    public CreateStudentReq(Student student, String accessRightString) {
        this.student = student;
        this.accessRightString = accessRightString;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getAccessRightString() {
        return accessRightString;
    }

    public void setAccessRightString(String accessRightString) {
        this.accessRightString = accessRightString;
    } 
}