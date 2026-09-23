package sg.edu.iss.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.iss.demo.model.Gadget;

public interface GadgetRepo extends JpaRepository<Gadget, Integer> {

}
