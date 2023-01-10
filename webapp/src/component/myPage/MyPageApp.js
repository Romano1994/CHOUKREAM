import React from 'react';
import MyPageNav from './myPageNav/MyPageNav';
import * as S from './MyPageAppStyle';
import { Route, Routes } from 'react-router';
import BuyHistory from "./buyHistory/BuyHistory"
import SellHistory from "./sellHistory/SellHistory"
import Like from "./like/Like"
import Profile from "./profile/Profile"
import Address from "./address/Address"
import Point from "./point/Point"
import MyPageMain from './MyPageMain';


const MyPageApp = () => {
    return (
        <S.MainContainer>
        <S.MainWrapper>
            <MyPageNav />
            <S.MainRight>
                {/* <CsHeader /> */}
                <Routes>
                    <Route path='/' element={<MyPageMain/>}/>
                    <Route path='buyHistory' element={<BuyHistory/>}/>
                    <Route path='sellHistory' element={<SellHistory/>}/>
                    <Route path='like' element={<Like/>}/>
                    <Route path='profile' element={<Profile/>}/>
                    <Route path='address' element={<Address/>}/>
                    <Route path='point' element={<Point/>}/>
                </Routes>
            </S.MainRight>
        </S.MainWrapper>
    </S.MainContainer>
    );
};

export default MyPageApp;