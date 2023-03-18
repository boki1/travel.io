import Carousel from 'react-bootstrap/Carousel';
import img1 from '../Home/assets/dubai_pic.jpg'
import img2 from '../Home/assets/italy_pic.jpg'
import "./styles.css"
import React, { useState } from 'react'
import { FloatingLabel, Form, Row, Col, Button } from 'react-bootstrap';
import HomeForm from "../HomeForm/HomeForm";
import { HomeFormDTO } from '../../DTO/HomeFormDTO';

export default function Home() {
  const [description, setDescription] = useState("");
  const [maxPrice, setMaxPrice] = useState(1000);
  const [minPrice, setMinPrice] = useState(0);
  const [city, setCity] = useState("");
  const [country, setCountry] = useState("");
  const [date1, setDate1] = useState("");
  const [date2, setDate2] = useState("");


  const onButtonClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    e.preventDefault();
    const vacationDescription: HomeFormDTO = {
      vacationDescription: description,
      maxPrice: maxPrice,
      minPrice: minPrice,
      currentCity: city,
      currentCountry: country,
      checkInDate: date1,
      checkOutDate: date2,
    };

    fetch("http://localhost:8080/api/v1/messages", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(vacationDescription),
    });

  };

  const modifyCity = (e: React.FormEvent<HTMLInputElement>) => {
    setCity((e.currentTarget.value).split("/")[0]);
    setCountry((e.currentTarget.value).split("/")[1]);
  };
  //
  // return (
  //   <div>
  //     {/* <HomeForm description={description} setDescription={setDescription} onButtonClick={onButtonClick}/> */}
  //     <Form>
  //       <FloatingLabel
  //         controlId="floatingTextarea"
  //         label="Description"
  //         className="mb-3"
  //       >
  //         <Form.Control type="text" placeholder="Enter Description" value={description} onInput={(event: React.FormEvent<HTMLInputElement>) => setDescription(event.currentTarget.value)} />
  //         <Form.Control type="number" placeholder="Enter Max price" value={maxPrice} onInput={(event: React.FormEvent<HTMLInputElement>) => setMaxPrice(parseFloat(event.currentTarget.value))} />
  //         <Form.Control type="number" placeholder="Enter Min price" value={minPrice} onInput={(event: React.FormEvent<HTMLInputElement>) => setMinPrice(parseFloat(event.currentTarget.value))} />
  //         <Form.Control type="text" placeholder="Eneter city/Country" value={`${city || 'City'}/${country || 'Country'}`} onInput={(event: React.FormEvent<HTMLInputElement>) => modifyCity(event)} />
  //         <Form.Control type="date" placeholder="Enter date1" value={date1} onInput={(event: React.FormEvent<HTMLInputElement>) => setDate1(event.currentTarget.value)} />
  //         <Form.Control type="date" placeholder="Enter date2" value={date2} onInput={(event: React.FormEvent<HTMLInputElement>) => setDate2(event.currentTarget.value)} />
  //         <Button variant="primary" type="submit" onClick={(event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => onButtonClick(event)}>
  //           Submit
  //         </Button>
  //       </FloatingLabel>
  //     </Form>
  //
  //   </div>
  // )
    return (
        <>
            <Carousel indicators = {false} controls = {false}>
                <Carousel.Item>
                    <img
                        className="d-block w-100 vh-100"
                        src={img2}
                        alt="First slide"
                    />

                </Carousel.Item>
                <Carousel.Item>
                    <img
                        className="d-block w-100"
                        src={img1}
                        alt="Second slide"
                    />

                    {/*<Carousel.Caption>*/}
                    {/*    <h3>Second slide label</h3>*/}
                    {/*    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>*/}
                    {/*</Carousel.Caption>*/}
                </Carousel.Item>
                {/*<Carousel.Item>*/}
                {/*    <img*/}
                {/*        className="d-block w-100"*/}
                {/*        src={img3}*/}
                {/*        alt="Third slide"*/}
                {/*    />*/}

                {/*    <Carousel.Caption>*/}
                {/*        <h3>Third slide label</h3>*/}
                {/*        <p>*/}
                {/*            Praesent commodo cursus magna, vel scelerisque nisl consectetur.*/}
                {/*        </p>*/}
                {/*    </Carousel.Caption>*/}
                {/*</Carousel.Item>*/}
            </Carousel>
        </>
    );

}
