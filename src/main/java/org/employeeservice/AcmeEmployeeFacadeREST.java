/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.employeeservice;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.validation.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.employeeservice.entity.AcmeEmployee;


/**
 *
 * @author Juneau
 */
@Stateless
@Path("acmeemployee")
public class AcmeEmployeeFacadeREST {

    @PersistenceContext(unitName = "EmployeeService_1.0PU")
    private EntityManager em;

    public AcmeEmployeeFacadeREST() {
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AcmeEmployee> findAll() {
        System.out.println("findAll");
        List<AcmeEmployee> employeeList = null;
        try {
            employeeList = em.createQuery("select object(o) from AcmeEmployee o")
                    .getResultList();
        } catch (NoResultException e){
        }
        return employeeList;
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response putBookmark(
        @FormParam("firstName") final String firstName,
        @FormParam("lastName") final String lastName,
        @FormParam("age") String age,
        @FormParam("startDate") final String startDate
    ) {
        System.out.println("Do POST Start!");
        System.out.println("parm=" + firstName + lastName + age + startDate);
        AcmeEmployee employee = new AcmeEmployee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAge(Integer.valueOf(age));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        java.time.LocalDate date; 
        try {
             date = LocalDate.parse(startDate, formatter);
        } catch (Exception e) {
            date = LocalDate.now();
        }
        System.out.println("Date=" + String.valueOf(date));
        employee.setStartDate(date);
        employee.setJobId(3333);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AcmeEmployee>> constraintViolations = validator.validate(employee);
        if(constraintViolations.size() > 0){
          Iterator<ConstraintViolation<AcmeEmployee>> iterator = constraintViolations.iterator();
          while(iterator.hasNext()){
            ConstraintViolation<AcmeEmployee> cv = iterator.next();
            System.err.println(cv.getRootBeanClass().getName()+"."+cv.getPropertyPath() + " " +cv.getMessage());
          }
          return Response.status(200).build();
        } else {
          em.persist(employee);
          em.flush();
          return Response.status(200).build();
        }
    }
}