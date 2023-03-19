import React from 'react';
import { GoogleMap as Map, LoadScript } from '@react-google-maps/api';
import { VacationOfferDTO } from '../../DTO/travel/VacationOfferDTO';

export default function GoogleMap({ offer, center }: { offer: VacationOfferDTO | null, center: { lat: number, lng: number } }) {

    const containerStyle = {
        width: '50%',
        height: '50%'
    };

    return (
        <LoadScript
            googleMapsApiKey="AIzaSyCHKHtFsBmrAbrAqkDUIXGi5XbK5rFFN4w"
        >
            <Map
                mapContainerStyle={containerStyle}
                center={center}
                zoom={10}
            >
            </Map>
        </LoadScript>
    )
}
