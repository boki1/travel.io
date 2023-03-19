import React, { useState } from 'react';
import Modal from 'react-modal';
import { DestinationDTO } from '../../DTO/DestinationDTO';
import { VacationOfferDTO } from '../../DTO/travel/VacationOfferDTO';
import { GoogleMap } from '../GoogleMap';
import { VacationOffer } from '../VacationOffer';
import './styles.css';


export default function DestinationModal({ destination, isOpen, setIsOpen }:
    { destination: DestinationDTO, isOpen: boolean, setIsOpen: (isOpen: boolean) => void }) {
    const [selectedOffer, setSelectedOffer] = useState<VacationOfferDTO | null>(destination?.vacationOffers[0] || null);

    destination.location.coordinates = { lat: 41.9028, lng: 12.4964 };

    return (
        <Modal isOpen={isOpen}>
            <div className="modalHeader">
                <h2 className="modal__title">{destination.location.city}/{destination.location.country}</h2>
                <button className="modal__close" onClick={() => setIsOpen(false)}>X</button>
            </div>
            <hr />
            
            <div className="vacationOffers">
                {destination.vacationOffers.map((offer, index) => (
                    <VacationOffer key={index} vacationOffer={offer} setSelectedOffer={setSelectedOffer} />
                ))}
            </div>

            <GoogleMap offer={selectedOffer} center={destination.location.coordinates} />
        </Modal>
    )
}