import './App.css';
import Layout from './component/Layout/Layout';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import UsedMain from './component/Used/UsedMain'
import UsedWrite from './component/Used/UsedWrite';
import UsedItem from './component/Used/UsedItem';
import UploadForm2 from './component/Used/UploadForm2';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<Layout />}>
                    <Route path="/" element="" />   
                    <Route path='Used/usedMain' element= { <UsedMain/>}/>
                    <Route path='Used/usedWrite' element = { <UsedWrite/>} />
                    <Route path="Used/useditem" element={<UsedItem></UsedItem>}></Route>
                    <Route path="Used/uploadform2" element={<UploadForm2></UploadForm2>}></Route>
                </Route>
            </Routes>
            
        </BrowserRouter>
    );
}

export default App;
