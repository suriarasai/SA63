package sg.edu.nus.firstapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.firstapp.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
