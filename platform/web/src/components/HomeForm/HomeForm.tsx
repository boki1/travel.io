import React, {useState} from 'react';
import {FloatingLabel, Form, Button, Row, Col} from 'react-bootstrap';
import {HomeFormDTO} from '../../DTO/HomeFormDTO';
import {postReq} from '../../services/makeRequests';
import './styles.css';


const HomeForm = ({setShowLoading}: { setShowLoading: React.Dispatch<React.SetStateAction<boolean>> }) => {
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

        const [data, err] = await postReq('messages', formData);


        if (err) {
            setShowLoading(false)
            console.log(err);
        } else {
            setShowLoading(false)
            console.log(data);
        }
    };

    const modifyCity = (e: React.FormEvent<HTMLInputElement>) => {
        setCurrentCity((e.currentTarget.value).split("/")[0]);
        setCurrentCountry((e.currentTarget.value).split("/")[1]);
    };

    return (
        <Form>
            <Row className=''>
                <Col className=''>
                    <FloatingLabel label="Enter max price of the vacation">
                        <Form.Control className='input' placeholder=' ' type="number" value={maxPrice}
                                      onInput={(event: React.FormEvent<HTMLInputElement>) => setMaxPrice(parseFloat(event.currentTarget.value))}/>
                    </FloatingLabel>
                    <FloatingLabel label="Enter City/Country of departure">
                        <Form.Control className='input' placeholder=' ' type="text"
                                      value={`${currentCity || ''}/${currentCountry || ''}`}
                                      onInput={(event: React.FormEvent<HTMLInputElement>) => modifyCity(event)}/>
                    </FloatingLabel>
                </Col>
                <Col className=''>
                    <FloatingLabel label="Check in date">
                        <Form.Control className='input' placeholder=' ' type="date" value={checkInDate}
                                      onInput={(event: React.FormEvent<HTMLInputElement>) => setCheckInDate(event.currentTarget.value)}/>
                    </FloatingLabel>
                    <FloatingLabel label="Check out date">
                        <Form.Control className='input' placeholder=' ' type="date" value={checkOutDate}
                                      onInput={(event: React.FormEvent<HTMLInputElement>) => setCheckOutDate(event.currentTarget.value)}/>
                    </FloatingLabel>
                </Col>
            </Row>

            <FloatingLabel label="Add description for your vacation... (max 1500 words)">
                <Form.Control className='input' placeholder=' ' type='text'
                              as='textarea' value={vacationDescription}
                              onInput={(event: React.FormEvent<HTMLInputElement>) => setVacationDescription(event.currentTarget.value)}/>
            </FloatingLabel>

            <Button className='button' variant="primary" type="submit"
                    onClick={(event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => onButtonClick(event)}>
                Submit
            </Button>
        </Form>
    )
}

export default HomeForm