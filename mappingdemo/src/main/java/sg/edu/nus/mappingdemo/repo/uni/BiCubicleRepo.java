package sg.edu.nus.mappingdemo.repo.uni;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.mappingdemo.model.bi.BiCubicle;

public interface BiCubicleRepo extends JpaRepository<BiCubicle, Integer> {
   
}
