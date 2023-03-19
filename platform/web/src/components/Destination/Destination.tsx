import { DestinationDTO } from "../../DTO/DestinationDTO";
import img from "../../assets/rome_pic.webp";
import { useState } from "react";
import { DestinationModal } from "../DestinationModal";

export default function Destination({ destination }: { destination: DestinationDTO }) {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <>
            <div className="wrapper" onClick={() => setIsOpen(true)}>
                <div className="header header_home">{destination.location.city}/{destination.location.country}</div>
                <div className="content content_home">{destination.location.description}</div>
                <div className="image" style={{ backgroundImage: `url(${img})` }}></div>
            </div>

            <DestinationModal destination={destination} isOpen={isOpen} setIsOpen={setIsOpen} />
        </>
    );
}