import HomeForm from "../HomeForm/HomeForm";
import { Slider } from '../Slider';
import "./styles.css"


export default function Home() {
  return (
    <>
      <div className='form'>
        <h1 className='title'>Let Travel.io find your next vacation</h1>
        <HomeForm />
      </div>
      <Slider />
    </>
  );

}
