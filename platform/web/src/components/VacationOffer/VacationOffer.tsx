import React from 'react'
import { VacationOfferDTO } from '../../DTO/travel/VacationOfferDTO';
import './styles.css';
import { BsFillPlusCircleFill } from "react-icons/bs";
import { Rating } from '@mui/material';

export default function VacationOffer({ vacationOffer, setSelectedOffer }:
    { vacationOffer: VacationOfferDTO, setSelectedOffer: (offer: VacationOfferDTO) => void }) {

    return (
        <div className="vacationOffer" onClick={() => setSelectedOffer(vacationOffer)}>
            <img src={vacationOffer.hotel.maxPhotoUrl} alt="hotel" />
            <div className='hotelDescription'>
                <div className='hotelName'>
                    <a href={vacationOffer.hotel.url} target='_blank'>
                        {vacationOffer.hotel.hotelName}
                    </a>
                </div>
                <hr />
                <div className='hotelAddress'>
                    <p>Address:</p>
                    <a href={vacationOffer.hotel.url} target='_blank'>
                        {vacationOffer.hotel.address}
                    </a>
                </div>
                <div className='hotelStars'>
                    <p>Rating:</p>
                    <Rating name="read-only" value={parseFloat(vacationOffer.hotel.reviewScore.toString()) / 2.0} readOnly />
                </div>
                <div className='hotelPrice'>
                    Price: {vacationOffer.hotel.price} {vacationOffer.hotel.currency}
                </div>
            </div>
            <BsFillPlusCircleFill className='icon' />
            <div className='fligthDescription'>
                {vacationOffer.flight?
                    <>
                        <div className='flightTo'>
                            <p>Flight to:</p>
                            {vacationOffer.flight?.location?.city},{vacationOffer.flight?.location?.country}
                        </div>
                        <hr/>
                        {/* Kris dobavi data i cena i opravi style-a tuk */}
                    </>:
                    <h5>Sorry, we did't found any flights for this dates.</h5>
                }
            </div>
        </div>
    )
}
