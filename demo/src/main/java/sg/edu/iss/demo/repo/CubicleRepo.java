package sg.edu.iss.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.iss.demo.model.Cubicle;

public interface CubicleRepo extends JpaRepository<Cubicle, Integer> {
	    // Cubicle is the inverse side, so "free" means no employee has occupied it yet.
		@Query("select c from Cubicle c where not exists (select e from Employee e where e.cubicle = c) order by c.name")
		List<Cubicle> findFree();

	     // List cubicles occupied and the relevant occupant
		@Query("select c from Cubicle c where exists (select e from Employee e where e.cubicle = c) order by c.name")
		List<Cubicle> findOccupied();

		// List employees without a cubicle.
		@Query("select count(c) from Cubicle c where not exists (select e from Employee e where e.cubicle = c)")
		long countFree();

	        // Ordered list
		List<Cubicle> findAllByOrderByName();

}
