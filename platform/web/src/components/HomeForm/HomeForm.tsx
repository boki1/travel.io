import React, { useState } from 'react';
import { FloatingLabel, Form, Button, Row, Col } from 'react-bootstrap';
import { HomeFormDTO } from '../../DTO/HomeFormDTO';
import { postReq } from '../../services/makeRequests';
import './styles.css';
import { json } from "stream/consumers";


const HomeForm = ({ setShowLoading }: { setShowLoading: React.Dispatch<React.SetStateAction<boolean>> }) => {
    const [vacationDescription, setVacationDescription] = useState('');
    const [maxPrice, setMaxPrice] = useState<number>();
    const [currentCity, setCurrentCity] = useState('');
    const [currentCountry, setCurrentCountry] = useState('');
    const [checkInDate, setCheckInDate] = useState('');
    const [checkOutDate, setCheckOutDate] = useState('');

    const onButtonClick = async (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();
        const formData: HomeFormDTO = {
            vacationDescription,
            maxPrice: maxPrice || 0,
            minPrice: 0,
            currentCity,
            currentCountry,
            checkInDate,
            checkOutDate,
        };

        setShowLoading(true);

        // const [data, err] = await postReq('messages', formData);


        // if (false) {
        //     setShowLoading(false)
        //     console.log(err);
        // } else {
            localStorage.setItem('destinations', JSON.stringify([{ "vacationOffers": [{ "hotel": { "hotelId": 54276, "hotelName": "Victoria Garden Bordeaux Centre", "reviewScore": 7.9, "address": "127 Cours De La Somme", "maxPhotoUrl": "https://cf.bstatic.com/xdata/images/hotel/max1280x900/374005004.jpg?k=e0e03a94a86173b65d2090104554df21be7572133b4ef35d7dbac96d67419912&o=", "url": "https://www.booking.com/hotel/fr/victoria-garden-suites-bordeaux.html", "airportCode": "{\"action_topics\":[],\"landmarks\":[],\"locations\":[[\"Bordeaux\",\"France\"]]}\n", "longitude": -0.572630167007446, "latitude": 44.8257317653906, "price": 663.4, "currency": "EUR" }, "flight": null }, { "hotel": { "hotelId": 8678030, "hotelName": "Beautiful apartment Bordeaux with lovely terrace", "reviewScore": 8.3, "address": "14 Rue Jules Steeg", "maxPhotoUrl": "https://cf.bstatic.com/xdata/images/hotel/max1280x900/367983455.jpg?k=fab9662810226c96c8b334eba5b07a998be7cf040c059ec43dbeea41cc137f60&o=", "url": "https://www.booking.com/hotel/fr/beautiful-apartment-bordeaux-with-lovely-terrace.html", "airportCode": "{\"action_topics\":[],\"landmarks\":[],\"locations\":[[\"Bordeaux\",\"France\"]]}\n", "longitude": -0.5668322, "latitude": 44.8268729, "price": 815.025, "currency": "EUR" }, "flight": null }], "location": { "city": "Bordeaux", "country": "France" } }]));
            window.location.href = '/destinations';
            // setShowLoading(false);
        //     console.log(data);
        // }
    };

    const modifyCity = (e: React.FormEvent<HTMLInputElement>) => {
        setCurrentCity((e.currentTarget.value).split("/")[0]);
        setCurrentCountry((e.currentTarget.value).split("/")[1]);
    };

    return (
        <Form>
            <Row>
                <Col>
                    <FloatingLabel label="Enter max price of the vacation">
                        <Form.Control className='input' placeholder=' ' type="number" value={maxPrice}
                            onInput={(event: React.FormEvent<HTMLInputElement>) => setMaxPrice(parseFloat(event.currentTarget.value))} />
                    </FloatingLabel>
                    <FloatingLabel label="Enter City/Country of departure">
                        <Form.Control className='input' placeholder=' ' type="text"
                            value={`${currentCity || ''}/${currentCountry || ''}`}
                            onInput={(event: React.FormEvent<HTMLInputElement>) => modifyCity(event)} />
                    </FloatingLabel>
                </Col>
                <Col>
                    <FloatingLabel label="Check in date">
                        <Form.Control className='input' placeholder=' ' type="date" value={checkInDate}
                            onInput={(event: React.FormEvent<HTMLInputElement>) => setCheckInDate(event.currentTarget.value)} />
                    </FloatingLabel>
                    <FloatingLabel label="Check out date">
                        <Form.Control className='input' placeholder=' ' type="date" value={checkOutDate}
                            onInput={(event: React.FormEvent<HTMLInputElement>) => setCheckOutDate(event.currentTarget.value)} />
                    </FloatingLabel>
                </Col>
            </Row>

            <FloatingLabel label="Add description for your vacation... (max 1500 words)">
                <Form.Control className='input' placeholder=' ' type='text'
                    as='textarea' value={vacationDescription}
                    onInput={(event: React.FormEvent<HTMLInputElement>) => setVacationDescription(event.currentTarget.value)} />
            </FloatingLabel>

            <Button className='button' variant="primary" type="submit"
                onClick={(event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => onButtonClick(event)}>
                Submit
            </Button>
        </Form>
    )
}

export default HomeForm