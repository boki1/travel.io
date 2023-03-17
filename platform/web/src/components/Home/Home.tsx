import React, {useState} from 'react'
import HomeForm from "../HomeForm/HomeForm";

export default function Home() {
  const [description, setDescription] = useState("");


  const onButtonClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    e.preventDefault();
    window.alert(description);
  }
  return (
    <div>
    <HomeForm description={description} setDescription={setDescription} onButtonClick={onButtonClick}/>
    </div>
  )
}
