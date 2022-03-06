/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.StudentSessionBeanLocal;
import entity.Student;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author yeeda
 */
@Singleton
@LocalBean
@Startup
public class DataIntSessionBean {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    @PersistenceContext(unitName = "NUSociety-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        if (em.find(Student.class, 1l) == null) {
            studentSessionBeanLocal.createNewStudent(new Student("Alex", "alex@gmail.com", "password", "alex", "www.profilepic/alex", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Betty", "betty@gmail.com", "password", "betty", "www.profilepic/betty", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Carl", "carl@gmail.com", "password", "carl", "www.profilepic/carl", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("David", "david@gmail.com", "password", "david", "www.profilepic/david", AccessRightEnum.MEMBER));
            studentSessionBeanLocal.createNewStudent(new Student("Elaine", "elaine@gmail.com", "password", "elaine", "www.profilepic/elaine", AccessRightEnum.MEMBER));
        }
        
        List<Student> students = studentSessionBeanLocal.retrieveAllStudents();
        for (Student student: students) {
            System.out.println(student.getName());
            System.out.println(student.getEmail());
        }
    }
    
}
