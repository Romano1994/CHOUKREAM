import React, { useEffect } from 'react';
import * as S from './like/LikeStyle';
import { itemImg } from './MyPageMainStyle';

const HistoryProduct = (props) => {
    const {item} = props
    const addComma = num => {
        let str = String(num)
        return (str.replace(/\B(?=(\d{3})+(?!\d))/g, ','))
    };

    
    const arr = JSON.stringify(item.imgName).split(',')
    
    const str = arr[0].slice(1)

    if(!arr[1]){
        var str2 = str.slice(0,str.length-1)
    }

    return (
        <S.Product>
            <S.ImgWrapper>
                <S.ProductImg src={`../resellList/${str2 ? str2:str}`}></S.ProductImg>
            </S.ImgWrapper>
            <S.TextWrapper>
                <S.ProductBrand>Brand: {item.brand}</S.ProductBrand>
                <S.ProductName>
                    Name: {item.subTitle}
                </S.ProductName>
                <S.ProductSize>Size: {item.size}</S.ProductSize>
            </S.TextWrapper>
            <S.PriceText>
            {addComma(item.price)}
            </S.PriceText>
            <S.StatusText>
            </S.StatusText>
        </S.Product>
    );
};

export default HistoryProduct;