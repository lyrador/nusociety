/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Student;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author yeeda
 */
@Named(value = "viewAllStudentsManagedBean")
@RequestScoped
public class ViewAllStudentsManagedBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private List<Student> students;

    /**
     * Creates a new instance of ViewAllStudentsManagedBean
     */
    public ViewAllStudentsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        students = studentSessionBeanLocal.retrieveAllStudents();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
}
