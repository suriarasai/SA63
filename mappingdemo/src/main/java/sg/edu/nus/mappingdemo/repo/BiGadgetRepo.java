package sg.edu.nus.mappingdemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.mappingdemo.model.bi.BiGadget;

public interface BiGadgetRepo extends JpaRepository<BiGadget, Integer> {
	
	

}
