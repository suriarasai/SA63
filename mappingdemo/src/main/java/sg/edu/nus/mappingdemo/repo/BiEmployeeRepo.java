package sg.edu.nus.mappingdemo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.nus.mappingdemo.model.bi.BiEmployee;

public interface BiEmployeeRepo extends JpaRepository<BiEmployee, Integer> {
	
	@Query("select e from BiEmployee e where empId=?1")
	public List<BiEmployee> findByArgument(Integer empId);
	
	
	@Query("select e from BiEmployee e where empId=?1or pay = ?2")
	public List<BiEmployee> findBy2Arguments(Integer empId, Double pay);
	
	@Query("select e from BiEmployee e where e.empId=:empId or e.name like %:name")
	public List<BiEmployee> findByTwoArgumentsByParam(@Param ("empId") Integer empId, @Param("name") String name);

	
}
