/**
 * 
 */
package com.mycompany.maven.hibernate;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Hari Somagatta
 *
 */
public class ManageEmployee {

	private static SessionFactory factory;

	public static void main(String[] args) {

		ServiceRegistry serviceRegistry;
		try {

			Configuration config = new Configuration();
			config.configure();
			config.addAnnotatedClass(Employee.class);

			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			factory = config.buildSessionFactory(serviceRegistry);
		} catch (Exception ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		ManageEmployee me = new ManageEmployee();

		/* Add few employee records in database */
		Integer employeeID1 = me.addEmployee("Khan", "Ali", "M", new BigDecimal(1000), 1);
		System.out.println("Employee " + employeeID1 + " is created in datbase.");
		Integer employeeID2 = me.addEmployee("Daisy", "Das", null, new BigDecimal(5000), 1);
		Integer employeeID3 = me.addEmployee("John", "Paul", "King", new BigDecimal(10000), 3);

		/* List down all the employees */
		 me.listEmployees();

		/* Update employee's records */
		// me.updateEmployee(employeeID1.intValue(), new BigDecimal(5000));

		/* Delete an employee from the database */
		me.deleteEmployee(2);
		me.deleteEmployeesWithLessSalary();

		/* List down new list of the employees */
		// me.listEmployees();
	}

	private void deleteEmployeesWithLessSalary() {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			
			
			//Read employees that match my criteria
			
			
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("delete from Employee where salary <3000");
			session.delete(query);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to CREATE an employee in the database */
	public Integer addEmployee(String fname, String lname, String mname, BigDecimal salary, int deptId) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;

		try {
			tx = session.beginTransaction();
			// One way of doing it
			Employee employee = new Employee();
			employee.setFirstName(fname);
			employee.setLastName(lname);
			employee.setSalary(salary);
			employee.setMiddleName(mname);
			employee.setDeptId(deptId);
			employeeID = (Integer) session.save(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}

	/* Method to READ all the employees */
	public void listEmployees() {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Query myquery = session.createQuery("FROM Employee");
			List<Employee> employees = myquery.list();
			for (Iterator<Employee> iterator = employees.iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.print("First Name: " + employee.getFirstName());
				System.out.print("  Last Name: " + employee.getLastName());
				System.out.println("  Salary: " + employee.getSalary());

			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to UPDATE salary for an employee */
	public void updateEmployee(int EmployeeID, BigDecimal salary) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			employee.setSalary(salary);
			session.update(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to DELETE an employee from the records */
	public void deleteEmployee(Integer EmployeeID) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			session.delete(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}