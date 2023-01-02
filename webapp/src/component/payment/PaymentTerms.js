import { faL } from '@fortawesome/free-solid-svg-icons';
import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import Inspection from './Inspection';
import PayHeader from './PayHeader';
import Policy from './Policy';
import * as S from './styles/TermStyle';

const buyDataList = [
    { id: 1, name: "chk1", text: "구매하려는 상품이 맞습니다.", subText: "상품 이미지, 모델번호, 출시일, 상품명, 사이즈를 한 번 더 확인했습니다", isChk: false },
    { id: 2, name: "chk2", text: "국내/해외에서 발매한 정품입니다.", subText: "모든 구성품이 그대로이며, 국내 발매 상품 여부는 확인드리지 않습니다", isChk: false },
    { id: 3, name: "chk3", text: "박스/패키지의 상태를 확인합니다.", subText: "박스/패키지 상태에 따른 검수 기준과 패키지(포장) 개봉 검수에 대한 주의사항을 확인했습니다.", linkText: "검수 기준 보기", isChk: false },
    { id: 4, name: "chk4", text: "이중 포장하여 선불 발송합니다.", subText: "반드시 이중 포장하여 택배 상자에 담아 선불 발송합니다.", isChk: false },
    { id: 5, name: "chk5", text: "KREAM의 최신 이용정책을 모두 확인하였으며, 판매를 계속합니다.", subText: "안전한 거래를 위해 반드시 숙지해야 할 미입고, 페널티, 부정거래 등의 이용정책을 확인했습니다.", linkText: "이용 정책 보기", isChk: false },
]
const sellDataList = [
    { id: 1, name: "chk1", text: "판매하려는 상품이 맞습니다.", subText: "상품 이미지, 모델번호, 출시일, 상품명, 사이즈를 한 번 더 확인했습니다", isChk: false },
    { id: 2, name: "chk2", text: "국내/해외에서 발매한 정품입니다.", subText: "모든 구성품이 그대로이며, 국내 발매 상품 여부는 확인드리지 않습니다", isChk: false },
    { id: 3, name: "chk3", text: "제조사에서 불량으로 인정하지 않는 기준은 하자로 판단하지 않습니다.", subText: "박스/패키지와 상품 컨디션에 민감하시다면 검수 시준을 반드시 확인하시기 바랍니다", linkText: "검수 기준 보기", isChk: false },
    { id: 4, name: "chk4", text: "패키지(포장) 개봉 검수에 대한 주의사항을 확인합니다.", subText: "검수 과정 중 밀봉 및 실링이 모두 개봉되어 KREAM 패키지로 포장되어 발송됩니다.", linkText: "검수 기준 보기", isChk: false },
    { id: 5, name: "chk5", text: "KREAM의 최신 이용정책을 모두 확인하였으며, 구매를 계속합니다.", subText: "안전한 거래를 위해 반드시 숙지해야 할 미입고, 페널티, 부정거래 등의 이용정책을 확인했습니다.", linkText: "이용 정책 보기", isChk: false },
]

const PaymentTerms = () => {
    const [data, setData] = useState(buyDataList)
    const [isInpectionOpen, setIsInpectionOpen] = useState(false)
    const [isPolicyOpen, setIsPolicyOpen] = useState(false)
    const [isAllChecked, setIsAllChecked] = useState(false)
    const [checkedItem, setCheckedItem] = useState([])
    const navigate = useNavigate()
    const [searchParams, setSearchParams] = useSearchParams()
    const size = searchParams.get("size")
    const type = searchParams.get("type")

    useEffect(() => {
        type === "sell" ? setData(sellDataList) : setData(buyDataList)
    }, [])

    const onCheck = (e) => {
        const { name, checked } = e.target
        setData(data.map(item => item.name === name ? { ...item, isChk: checked } : item))

        if (checked) {
            setCheckedItem([...checkedItem, name])
        } else {
            setCheckedItem(checkedItem.filter(item => item !== name ))
        }
        setIsAllChecked(data.filter(item => item.isChk === true).length === 4)
    }

    const onLink = (e) => {
        const { innerHTML } = e.target
        innerHTML === " 검수 기준 보기" && setIsInpectionOpen(true)
        innerHTML === " 이용 정책 보기" &&  setIsPolicyOpen(true)
    }
    const onInspenctionClose = () => {
        setIsInpectionOpen(false)
    }
    const onPolicyClose = () => {
        setIsPolicyOpen(false)
    }
    const onOrderType = () => {
        navigate(`/orderType?type=${type}&size=${size}`)
    }

    return (
        <>
            <S.TermWrapper>
                <PayHeader />
                <S.TermBottom>
                    <S.TermBottomUl>
                        {
                            data.map(item =>
                                <S.TermsDiv key={item.id}>
                                    <S.TermsLabel htmlFor={item.name}>
                                        <S.TermsText>{item.text}</S.TermsText>
                                        <S.TermsSubText>{item.subText}</S.TermsSubText>
                                    </S.TermsLabel>
                                    <input type="checkbox" id={item.name} name={item.name} checked={item.isChk} onChange={onCheck} />
                                    <S.TermsLinkTextWrapper name={item.name} onClick={onLink}> {item.linkText}</S.TermsLinkTextWrapper>
                                </S.TermsDiv>)
                        }
                        {
                            isAllChecked ? <S.BuyBtn onClick={onOrderType}>{type === "/buy" ? "구매" : "판매" }계속</S.BuyBtn> : <S.BuyBtn style={{backgroundColor: "#ebebeb", cursor: 'default'}} disabled>구매 계속</S.BuyBtn>
                        }
                        {isInpectionOpen && <Inspection onInspenctionClose={onInspenctionClose} />}
                        {isPolicyOpen && <Policy onPolicyClose={onPolicyClose}/>}
                    </S.TermBottomUl>
                </S.TermBottom>
            </S.TermWrapper>
        </>
    );
};

export default PaymentTerms;