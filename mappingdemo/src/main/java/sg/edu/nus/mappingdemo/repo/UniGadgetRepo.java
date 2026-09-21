package sg.edu.nus.mappingdemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.mappingdemo.model.uni.UniGadget;

public interface UniGadgetRepo extends JpaRepository<UniGadget, Integer> {
     
}
