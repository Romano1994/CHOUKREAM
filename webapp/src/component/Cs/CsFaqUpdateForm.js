import axios from 'axios';
import React, { useEffect, useMemo, useState } from 'react';

import { useNavigate, useParams } from 'react-router';

import Header from '../Header/Header';
import CsNav from './Csnav/CsNav';
import { useRef } from 'react';

import { Editor, Viewer } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';



// 리액트가 새 컴파일/ 랜더링 되어야지 수정 전 내용이 뜸  - 즉 리액트가 새로고침이 안되면 내용이 수정페이지에 안들어옴 (제목이랑 카테고리는들어옴.)
const CsFaqUpdateForm = () => {
    const [list, setList] = useState([]);

    const [data, setData] = useState([]);
    const { seq } = useParams();

    const htmlString = useState('');
    const[file , setFile] =useState([])
    const [img1, setImg1] = useState();

    const [form, setForm] = useState({
        id : '',
        category: '',
        title: '',
        content: '',
        filename:''
    });
    const { id,category, title, content,filename } = form;
    const editorRef = useRef();
    useEffect(() => {
        
        axios
            .get(`http://localhost:8080/cs/getBoard?seq=${seq}`) //

            .then(res => {
              
            setForm(res.data)
       //     content = content.replace('<img src="blob:http://localhost:3000/'+filename+'" contenteditable="false">', '<img src="/storage/'+filename+'.png" contenteditable="false">')
           // editorRef.current?.getInstance().setHTML(content)
          
          }
          
           
            )        
        .catch((error) => console.log(error));

    },[])

    useEffect(() => {
        
        editorRef.current?.getInstance().setHTML((content))
    }, [form.filename]) 

    const onInput = e => {
        const { name, value } = e.target;

        setForm({
            ...form,
            [name]: value,
        });
    };

    const navigate = useNavigate();
    const onUploadImage = async (img, callback) => {
       
        
        const url =  window.URL.createObjectURL(img)
        console.log(img);
    
      
        const split = url.split('/');
        console.log(split)
       
        setImg1(split[3])
   
       alert(url)
        file.push(img);
       
        callback(url); 
        
    };
    
    const onUpdate = e => {
        console.log(file)
         var formData = new FormData();
         file.map(files=>formData.append('img',files));
         for (let key of formData.keys()) {
        console.log(key, ":", formData.get(key));
    }
        axios
            .put('http://localhost:8080/cs/update', formData, {
                params: {
                    seq: seq, // seq 필수로 들어가야 함 .그래야 insert가 아닌 update가  (seq가 pk)
                    category: category,
                    title: title,
                    
                    content: editorRef.current?.getInstance().getHTML(),
                    filename :filename
                },
            })
            .then(() => {
                alert(' 수정 등록');
                navigate('/cs/CsFaq');
            })
            .catch(error => console.log(error));
    };
    const onReset = e => {
        e.preventDefault();
        // 리셋 시 변경 전 값 가져오기 위해 다시 한번 가져오기  -
        axios
            .get(`http://localhost:8080/cs/getBoard?seq=${seq}`)

            .then(res => {
                setForm(res.data);
            })
            .catch(error => console.log(error));
    };
    const onList = e => {
        navigate('/cs/CsFaq');
    };
    return (
        <>
            <form>
                <table style={{ border: '1px solid black' }}>
                    <tbody>
                        <tr>
                            <td>
                                <select
                                    name="category"
                                    style={{ width: '100px' }}
                                    onChange={onInput}
                                    value={form.category}
                                >
                                    <option>선택</option>
                                    <option value="common"> 공통</option>
                                    <option value="policy">정책</option>
                                    <option value="buying">구매</option>
                                </select>
                            </td>
                            <td>
                                <input
                                    type="text"
                                    name="title"
                                    placeholder="제목"
                                    style={{ width: '250px' }}
                                    onChange={onInput}
                                    value={form.title}
                                />
                            </td>
                        </tr>
                        <tr>
                            <td colSpan="2">
                                {/* {content} */}
                                <Editor
                                    ref={editorRef}
                                    previewStyle="vertical" // 미리보기 스타일 지정
                                    height="300px" // 에디터 창 높이
                                    initialEditType="wysiwyg" // 초기 입력모드 설정(디폴트 markdown)
                                  //  initialValue={(form.content) }   
                                    toolbarItems={[
                                        // 툴바 옵션 설정
                                        ['heading', 'bold', 'italic', 'strike'],
                                        ['hr', 'quote'],
                                        [
                                            'ul',
                                            'ol',
                                            'indent',
                                            'outdent', 
                                        ],
                                        ['table', 'image', 'link'],
                                        ['code', 'codeblock'],
                                    ]}
                                    hooks={{
                                        //사진 등록 버튼 눌렀을 때.
                                        addImageBlobHook: onUploadImage,
                                    }} //
                                ></Editor>
                                {/* <textarea name ='content' placeholder='내용' style={{width :'350px' , height:'350px' }} onChange={onInput} value={content}/> */}
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
           
            <div>
                <p>
                    <button onClick={onList}>목록</button>
                    <button onClick={onUpdate}>수정</button>
                    <button onClick={onReset}>취소</button>
                </p>
            </div>
        </>
    );
};

export default CsFaqUpdateForm;
