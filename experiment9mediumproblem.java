package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;

    // Default constructor
    public Student() {}

    // Constructor
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // toString for easy display
    @Override
    public String toString() {
        return "Student [id=" +
package com.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create SessionFactory from Hibernate configuration
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class).buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception and throw a RuntimeException to terminate the application
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class StudentDAO {

    private SessionFactory sessionFactory;

    public StudentDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Create Student
    public void createStudent(Student student) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(student);
        transaction.commit();
    }

    // Read Student
    public Student getStudent(int studentId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Student.class, studentId);
    }

    // Update Student
    public void updateStudent(Student student) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.update(student);
        transaction.commit();
    }

    // Delete Student
    public void deleteStudent(int studentId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Student student = session.get(Student.class, studentId);
        if (student != null) {
            session.delete(student);
        }
        transaction.commit();
    }
}
package com.example;

import org.hibernate.SessionFactory;

public class MainApp {

    public static void main(String[] args) {

        // Create StudentDAO instance
        StudentDAO studentDAO = new StudentDAO();

        // Create and save a student (Create)
        Student student1 = new Student("John", 22);
        studentDAO.createStudent(student1);
        System.out.println("Student created: " + student1);

        // Read student by ID (Read)
        Student fetchedStudent = studentDAO.getStudent(student1.getId());
        System.out.println("Student fetched: " + fetchedStudent);

        // Update student details (Update)
        fetchedStudent.setName("John Doe");
        fetchedStudent.setAge(23);
        studentDAO.updateStudent(fetchedStudent);
        System.out.println("Student updated: " + fetchedStudent);

        // Delete student (Delete)
        studentDAO.deleteStudent(fetchedStudent.getId());
        System.out.println("Student deleted");
    }
}
