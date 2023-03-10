package lookbook.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lookbook.bean.LikesDTO;
import lookbook.entity.StyleLikesEntity;

@Repository
public interface StyleLikesDAO extends JpaRepository<StyleLikesEntity, String> {

	//쿼리문 select  likes_id from style_likes_table where member_id = 1 and style_seq = 1;
	public Optional<StyleLikesEntity> findByMemberDto_IdAndStyleEntity_Seq(Long memberId, int styleSeq);

	public void deleteByMemberDto_IdAndStyleEntity_Seq(Long memberId, int styleSeq);
	
	//쿼리 select COUNT(*) from style_likes_table where style_seq = 2;
	public int countByStyleEntity_Seq(int styleSeq);

	public List<StyleLikesEntity> findByMemberDto_Id(String id);

//	@Query(nativeQuery = true,
//			value="select * from style_likes_table where member_id=:id and style_seq=:seq")
//	public LikesDTO2 findlist(@Param("id") String id,@Param("seq") int seq);
//

}
