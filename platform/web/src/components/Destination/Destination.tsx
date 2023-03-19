import {DestinationDTO} from "../../DTO/DestinationDTO";
import img from "../../assets/rome_pic.webp";
import img2 from "../../assets/dubai.webp";

export default function Destination({destination}: { destination: DestinationDTO }) {

    return (
        <>
            <div className="wrapper">
                <div className="header header_home">{destination.location.city}/{destination.location.country}</div>
                <div className="content content_home">{destination.location.description}</div>
                <div className="image" style={{backgroundImage: `url(${img})`}}></div>
            </div>


        </>
    );
}