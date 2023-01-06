package shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.bean.UsedItemDTO;
import shop.bean.UsedItemLikeDTO;
import shop.dao.UsedItemDAO;
import shop.dao.UsedItemLikeDAO;

@Service
public class UsedItemServiceImpl implements UsedItemService {
	
	@Autowired
	private UsedItemDAO usedItemDAO;
	
	@Autowired
	private UsedItemLikeDAO usedItemLikeDAO;
	
	@Override
	public void upload2(UsedItemDTO usedItemDTO) {
		usedItemDAO.save(usedItemDTO);
		
	}


//	@Override
//	public void writeItem(UsedItemDTO usedItemDTO) {
//		usedItemDAO.save(usedItemDTO);
//		
//	}

	
	@Override
	public List<UsedItemDTO> getItem() {
		return usedItemDAO.findAll();
		
	}

	@Override
	public Optional<UsedItemDTO> viewItem(int seq) {
		return usedItemDAO.findById(seq);
	}


	@Override
	public UsedItemLikeDTO itemLike(int seq, String id,String shopKind) {
		System.out.println("---------------------------");
		System.out.println(shopKind);
		return usedItemLikeDAO.itemLike(seq,id,shopKind);
	}


	@Override
	public void likeSet(UsedItemLikeDTO usedItemLikeDTO) {
		
		System.out.println(usedItemLikeDTO);
		if(usedItemLikeDTO.getUserLike()) {
			System.out.println("좋아요 ++");
			usedItemLikeDAO.save(usedItemLikeDTO);
			usedItemDAO.likeUp(usedItemLikeDTO.getSeq());
			
		}else {
			System.out.println("좋아요 --");
			usedItemLikeDAO.deleteBySeqAndIdAndShopKind(usedItemLikeDTO.getSeq(),usedItemLikeDTO.getId(),usedItemLikeDTO.getShopKind());
			usedItemDAO.likeDown(usedItemLikeDTO.getSeq());
		}
		
	}


	@Override
	public void deleteItem(int seq) {
		usedItemDAO.deleteItem(seq);
		//usedItemDAO.deleteById(seq);
	}
	

	
}
