import HomeForm from "../HomeForm/HomeForm";
import {Slider} from '../Slider';
import "./styles.css"
import {useState} from "react";
import {LoadingSpinner} from "../LoadingSpinner";


export default function Home() {
    const [isLoading, setIsLoading] = useState(false);


    return (
        isLoading ?
            <LoadingSpinner/>
            :
            <>
                <div className='form'>
                    <h1 className='title'>Let Travel.io find your next vacation</h1>
                    <HomeForm setShowLoading={setIsLoading}/>
                </div>
                <Slider/>
            </>
    );

}
