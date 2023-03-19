import React from 'react';
import { GoogleMap as Map, LoadScript } from '@react-google-maps/api';
import { VacationOfferDTO } from '../../DTO/travel/VacationOfferDTO';

export default function GoogleMap({ offer, center }: { offer: VacationOfferDTO | null, center: { lat: number, lng: number } }) {

    const containerStyle = {
        width: '400px',
        height: '400px'
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
                { /* Child components, such as markers, info windows, etc. */}
                <></>
            </Map>
        </LoadScript>
    )
}
