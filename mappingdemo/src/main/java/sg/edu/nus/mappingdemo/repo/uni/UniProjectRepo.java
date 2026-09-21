package sg.edu.nus.mappingdemo.repo.uni;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.mappingdemo.model.uni.UniProject;

public interface UniProjectRepo extends JpaRepository<UniProject, Integer> {

}
