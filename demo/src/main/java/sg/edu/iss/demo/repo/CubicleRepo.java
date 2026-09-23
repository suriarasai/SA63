package sg.edu.iss.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.iss.demo.model.Cubicle;

public interface CubicleRepo extends JpaRepository<Cubicle, Integer> {

}
