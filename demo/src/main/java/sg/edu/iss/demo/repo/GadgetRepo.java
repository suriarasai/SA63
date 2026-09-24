package sg.edu.iss.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.iss.demo.model.Gadget;

public interface GadgetRepo extends JpaRepository<Gadget, Integer> {
	// List Gadgets that are ordered by employee name
	List<Gadget> findByEmployeeIsNullOrderByName();
	
	//For the screen Filter
	long countByEmployeeIsNull();
    
	//Gadget Table Listing
	List<Gadget> findAllByOrderByGadgetId();

}
