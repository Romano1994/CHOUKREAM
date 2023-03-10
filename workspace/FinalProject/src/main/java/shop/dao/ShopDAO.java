package shop.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.Parent;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import lombok.experimental.PackagePrivate;
import my.bean.SellBuyHistory;
import shop.bean.BrandListDTO;
import shop.bean.CompletedOrderDTO;
import shop.bean.OrderDTO;
import shop.bean.ProductDTO;
import shop.bean.SortListDTO;

@Repository
public interface ShopDAO extends JpaRepository<ProductDTO, Integer> {

	List<ProductDTO> findProductDTOsByCategory(String shoes);
	
	@Transactional
	@Modifying
	@Query("delete from NewProductDTO newproductDTO where newproductDTO.seq = :seq ")
	// @Query(value="delete from NewProductDTO newproductDTO where newproductDTO.seq = :seq", nativeQuery = true)
	// query문 그대로 적으면된다
	public void deleteBySeq(@Param("seq") int seq);
	
	
//	@Query("select productDTO from ProductDTO productDTO where productDTO.category = :shoes")
//	List<ProductDTO> getShoesList(@Param("shoes") String shoes);
	@Query(nativeQuery = true, value = "select a.seq, a.brand, ifnull(b.order_price, '-') as price, a.title, a.sub_title, a.img_name from product_table as a left outer join (select seq, min(order_price) AS order_price from order_table where buy_sell = 1 group by seq ) as b on a.seq = b.seq where brand = :brand and a.seq not in(:seq) order by a.seq")
	List<BrandListDTO> getBrandList(@Param("seq") int seq, @Param("brand") String brand);
	
	@Query(nativeQuery = true, value = "select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, comPay.size, comPay.type, comPay.ship_address as shipAddress, comPay.ship_name as shipName, comPay.ship_phone as shipPhone, comPay.pay_price as price, comPay.log_time from product_table as pro left join complete_payment as comPay on pro.seq = comPay.product_num where comPay.id = :id and comPay.type = 'resell'\r\n"
			+ "union all\r\n"
			+ "select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, comPay.size, comPay.type, comPay.ship_address as shipAddress, comPay.ship_name as shipName, comPay.ship_phone as shipPhone, comPay.pay_price as price, comPay.log_time from complete_payment comPay left join new_product pro on comPay.product_num = pro.seq  where comPay.id = :id and type = 'new';")
	List<SellBuyHistory> getBoughtHistorie(@Param("id") String email);

	@Query(nativeQuery = true, value = "select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, ord.size, ord.upload_date as logDate\r\n"
			+ "from product_table as pro join order_table as ord on ord.seq = pro.seq where buy_order_user = :id ;" )
	List<SellBuyHistory> getBuyingHistory(@Param("id") String email);

	@Query(nativeQuery = true, value = "select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, comOrd.price as price, comOrd.size from product_table as pro join completed_order_table as comOrd on pro.seq = comOrd.seq where sell_order_user = :id")
	List<SellBuyHistory> getSoldHistory(@Param("id") String email);

	@Query(nativeQuery = true, value = "select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, ord.size, ord.upload_date as logDate\r\n"
			+ "from product_table as pro join order_table as ord on ord.seq = pro.seq where sell_order_user = :id ;")
	List<SellBuyHistory> getSellingHistory(@Param("id") String email);

	@Query(nativeQuery = true, value = "select seq, img_name as imgName, id, title as subTitle from used_item where id = :id and selling_state=1")
	List<SellBuyHistory> getSellingUsed(@Param("id") String email);

	@Query(nativeQuery = true, value = "select used.shop_kind as type, used.seq, img_name as imgName, used.id, used.title as subTitle, comPay.ship_address as shipAddress, comPay.ship_name as shipName, comPay.ship_phone as shipPhone from used_item as used join complete_payment as comPay on used.seq = comPay.product_num where used.id = :id and used.selling_state=0;")
	List<SellBuyHistory> getSoldUsed(@Param("id")  String email);

	@Query(nativeQuery = true, value = "select seq, img_name as imgName, id, title as subTitle from used_item where id = :id and selling_state=1")
	List<SellBuyHistory> getBuyingUsed(@Param("id")  String email);

	@Query(nativeQuery = true, value = "select used.shop_kind as type, used.seq, img_name as imgName, used.id, used.title as subTitle, comPay.ship_address as shipAddress, comPay.ship_name as shipName, comPay.ship_phone as shipPhone from used_item as used join complete_payment as comPay on used.seq = comPay.product_num where comPay.id = :id and used.selling_state=0;")
	List<SellBuyHistory> getBoughtUsed(@Param("id")  String email);
	
	
	// 반환타입을 interface 로 
	@Query( nativeQuery = true, value= "select a.seq, a.brand, ifnull(b.order_price, '-') as min_price ,ifnull(c.order_price, '-') \r\n"
			+ "as max_price, a.title, a.sub_title, a.img_name, d.like_count, e.order_count, a.category, a.tag, a.release_date, a.gender from product_table as a \r\n"
			+ "left outer join (select seq, min(order_price) AS order_price from order_table where buy_sell = 1 group by seq ) as b on a.seq = b.seq\r\n"
			+ "left outer join (select seq, max(order_price) AS order_price from order_table where buy_sell = 0 group by seq ) as c on a.seq = c.seq\r\n"
			+ "left outer join (select seq, count(*) AS like_count from used_item_like where shop_kind = 'resell' group by seq) as d on a.seq = d.seq\r\n"
			+ "left outer join (select seq, count(*) AS order_count from completed_order_table group by seq) as e on a.seq = e.seq\r\n"
			+ "order by order_count desc")
	List<SortListDTO> favourSort();
	
//	List<BrandListDTO> getBrandList(@Param("seq") int seq, @Param("brand") String brand);
	
	@Transactional
	@Modifying
	@Query("delete from ProductDTO newproductDTO where newproductDTO.seq = :seq ")
	// @Query(value="delete from NewProductDTO newproductDTO where newproductDTO.seq = :seq", nativeQuery = true)
	// query문 그대로 적으면된다
	public void resellDelete(@Param("seq") int seq);
	
	@Query("SELECT u FROM ProductDTO u WHERE u.brand like %:keyword%")
	List<ProductDTO> getSearchBrand(String keyword);
	
	@Query("SELECT u FROM ProductDTO u WHERE u.category like %:keyword%")
	List<ProductDTO> getSearchCategory(String keyword);
	
	@Query(nativeQuery = true, value = "select * from(\n"
			+ "select row_number() over(order by release_date desc) as rn, a.seq, a.brand, ifnull(b.order_price, '-') as min_price ,ifnull(c.order_price, '-')\n"
			+ "as max_price, a.title, a.sub_title, a.img_name, d.like_count, e.order_count, a.category, a.tag, a.release_date from product_table as a left outer join (select seq, min(order_price) AS order_price from order_table where buy_sell = 1 group by seq ) as b on a.seq = b.seq\n"
			+ "left outer join (select seq, max(order_price) AS order_price from order_table where buy_sell = 0 group by seq ) as c on a.seq = c.seq \n"
			+ "			left outer join (select seq, count(*) AS like_count from used_item_like where shop_kind = 'resell' group by seq) as d on a.seq = d.seq\n"
			+ "			left outer join (select seq, count(*) AS order_count from completed_order_table group by seq) as e on a.seq = e.seq\n"
			+ "			) t where rn >= :start and rn<= :end")
	public List<SortListDTO> getRecentReleaseList(int start, int end);
	
	@Query(nativeQuery= true, value= "select a.seq, a.brand, ifnull(b.order_price, '-') as min_price ,ifnull(c.order_price, '-') \r\n"
			+ "as max_price, a.title, a.sub_title, a.img_name, d.like_count, e.order_count, a.category, a.tag, a.release_date, a.gender from product_table as a \r\n"
			+ "left outer join (select seq, min(order_price) AS order_price from order_table where buy_sell = 1 group by seq ) as b on a.seq = b.seq\r\n"
			+ "left outer join (select seq, max(order_price) AS order_price from order_table where buy_sell = 0 group by seq ) as c on a.seq = c.seq\r\n"
			+ "left outer join (select seq, count(*) AS like_count from used_item_like where shop_kind = 'resell' group by seq) as d on a.seq = d.seq\r\n"
			+ "left outer join (select seq, count(*) AS order_count from completed_order_table group by seq) as e on a.seq = e.seq\r\n"
			+ "order by (min_price + 0) asc")
	List<SortListDTO> BuySort();  // 즉시구매가 낮은순
	
	@Query(nativeQuery = true, value = "select a.seq, a.brand, ifnull(b.order_price, '-') as min_price ,ifnull(c.order_price, '-') \r\n"
			+ "as max_price, a.title, a.sub_title, a.img_name, d.like_count, e.order_count, a.category, a.tag, a.release_date, a.gender from product_table as a \r\n"
			+ "left outer join (select seq, min(order_price) AS order_price from order_table where buy_sell = 1 group by seq ) as b on a.seq = b.seq\r\n"
			+ "left outer join (select seq, max(order_price) AS order_price from order_table where buy_sell = 0 group by seq ) as c on a.seq = c.seq\r\n"
			+ "left outer join (select seq, count(*) AS like_count from used_item_like where shop_kind = 'resell' group by seq) as d on a.seq = d.seq\r\n"
			+ "left outer join (select seq, count(*) AS order_count from completed_order_table group by seq) as e on a.seq = e.seq\r\n"
			+ "order by (max_price + 0) desc")
	List<SortListDTO> SellSort(); // 즉시판매가 높은순 
	
	@Query(nativeQuery = true, value = "select a.seq, a.brand, ifnull(b.order_price, '-') as min_price ,ifnull(c.order_price, '-') \r\n"
			+ "as max_price, a.title, a.sub_title, a.img_name, d.like_count, e.order_count, a.category, a.tag, a.release_date, a.gender from product_table as a \r\n"
			+ "left outer join (select seq, min(order_price) AS order_price from order_table where buy_sell = 1 group by seq ) as b on a.seq = b.seq\r\n"
			+ "left outer join (select seq, max(order_price) AS order_price from order_table where buy_sell = 0 group by seq ) as c on a.seq = c.seq\r\n"
			+ "left outer join (select seq, count(*) AS like_count from used_item_like where shop_kind = 'resell' group by seq) as d on a.seq = d.seq\r\n"
			+ "left outer join (select seq, count(*) AS order_count from completed_order_table group by seq) as e on a.seq = e.seq\r\n"
			+ "order by release_date desc")
	List<SortListDTO> releaseDateSort();
	
	//lookbook 
	@Query("select productDTO from ProductDTO productDTO where productDTO.title like '%' || :keyword || '%' OR productDTO.subTitle like '%' || :keyword || '%' ")
	public List<ProductDTO> search(@Param("keyword") String keyword);

	
	@Query(nativeQuery = true, value = "select * from (\n"
			+ "select row_number() over(order by order_count desc) as rn, a.seq, a.brand, ifnull(b.order_price, '-') as min_price ,ifnull(c.order_price, '-')\n"
			+ "as max_price, a.title, a.sub_title, a.img_name, d.like_count, e.order_count, a.category, a.tag from product_table as a\n"
			+ "left outer join (select seq, min(order_price) AS order_price from order_table where buy_sell = 1 group by seq ) as b on a.seq = b.seq\n"
			+ "left outer join (select seq, max(order_price) AS order_price from order_table where buy_sell = 0 group by seq ) as c on a.seq = c.seq\n"
			+ "left outer join (select seq, count(*) AS like_count from used_item_like where shop_kind = 'resell' group by seq) as d on a.seq = d.seq\n"
			+ "left outer join (select seq, count(*) AS order_count from completed_order_table group by seq) as e on a.seq = e.seq\n"
			+ ") t where rn >= :start and rn<= :end")
	List<SortListDTO> getPopularList(int start, int end);

	@Query(nativeQuery = true, value = "select * from (\r\n"
			+ "select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, comOrd.size, comOrd.trade_date as logDate, comPay.type, comOrd.price\r\n"
			+ "from product_table as pro join completed_order_table as comOrd on pro.seq = comOrd.seq join complete_payment as comPay on pro.seq = comPay.product_num where comPay.type = \"resell\" and comOrd.sell_order_user = :id \r\n"
			+ "union all\r\n"
			+ "select used.seq, used.contents as brand, used.img_name as imgName, used.title as subTitle, used.size, comOrd.trade_date as logDate, used.shop_kind as type, comOrd.price\r\n"
			+ "from used_item as used join completed_order_table comOrd on comOrd.sell_order_user = used.id where id = :id)t order by logDate desc limit 3;")
	List<SellBuyHistory> getSellRecent(@Param("id") String email);

	@Query(nativeQuery = true, value = "select * from (\r\n"
			+ "            select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, comPay.size, comPay.type, comPay.ship_address as shipAddress, comPay.ship_name as shipName, comPay.ship_phone as shipPhone, comPay.pay_price as price, comPay.log_time from product_table as pro left join complete_payment as comPay on pro.seq = comPay.product_num where comPay.id = :id and comPay.type = 'resell'\r\n"
			+ "			union all\r\n"
			+ "            select pro.seq, pro.brand, pro.img_name as imgName, pro.sub_title as subTitle, comPay.size, comPay.type, comPay.ship_address as shipAddress, comPay.ship_name as shipName, comPay.ship_phone as shipPhone, comPay.pay_price as price, comPay.log_time from complete_payment comPay left join new_product pro on comPay.product_num = pro.seq  where comPay.id = :id and type = 'new'\r\n"
			+ "			union all\r\n"
			+ "            select pro.seq, pro.product_name as brand, pro.img_name as imgName, pro.title as subTitle, comPay.size, comPay.type, comPay.ship_address as address, comPay.ship_name as shipName, comPay.ship_phone as shipPhone, comPay.pay_price as price, comPay.log_time from complete_payment comPay left join used_item pro on comPay.product_num = pro.seq  where comPay.id = :id and type = 'used') t order by log_time desc limit 3;")
	List<SellBuyHistory> getBuyRecent(@Param("id") String email);
	

}
