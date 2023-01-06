package shop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import shop.bean.UsedItemDTO;
import shop.bean.UsedItemLikeDTO;

@Repository
public interface UsedItemDAO extends JpaRepository<UsedItemDTO, Integer>{

	@Transactional
	@Modifying
	@Query("update UsedItemDTO usedItemDTO set usedItemDTO.likes = usedItemDTO.likes + 1 where usedItemDTO.seq = :seq")
	public void likeUp(int seq);

	@Transactional
	@Modifying
	@Query("update UsedItemDTO usedItemDTO set usedItemDTO.likes = usedItemDTO.likes - 1 where usedItemDTO.seq = :seq")
	public void likeDown(int seq);
	
	
	@Modifying
	@Transactional
	@Query(value="delete from used_item where seq = ?1", nativeQuery=true)
	public void deleteItem(@Param("seq") int seq);
	//https://stackoverflow.com/questions/62778719/spring-data-jpa-delete-query
	
	
}
