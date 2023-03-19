import img from '../../assets/rome_pic.webp'
import img2 from "../../assets/dubai.webp"
import {DestinationDTO} from "../../DTO/DestinationDTO";
import {Destination} from '../Destination';
import './styles.css'

export default function Destinations() {
    const destinations = JSON.parse(localStorage.getItem("destinations")!);
    console.log('destinations', destinations);


    return (
        <div className="container" id="destinations">
            {
                destinations?.map((destination: DestinationDTO) =>
                    <Destination destination={destination}/>
                )
            }
        </div>

    );
}