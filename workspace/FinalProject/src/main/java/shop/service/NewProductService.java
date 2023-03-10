package shop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import shop.bean.NewProductDTO;
import shop.bean.NewSortListDTO;

public interface NewProductService {
	
	public void upload(NewProductDTO newProductDTO);

	public List<NewProductDTO> getNewProductList();

	public void update(NewProductDTO newProductDTO);

	public void delete(int seq);

	public List<NewProductDTO> search(Map<String, String> map);

	public Optional<NewProductDTO> updateNewProductInfo(int seq);

	public List<NewSortListDTO> newFavourSort();

	public List<NewSortListDTO> newBuySort();

	public List<NewSortListDTO> newSellSort();

	public List<NewSortListDTO> newReleaseDateSort();

	public Optional<NewProductDTO> getProductBySeqNew(int seq);
}
