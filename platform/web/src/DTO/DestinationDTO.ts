import {Flight} from "./travel/Flight";
import {Hotel} from "./travel/Hotel";
import {Location} from "./travel/Location"

export interface DestinationDTO{

    location: Location;
    hotel: Hotel;
    flight: Flight;


}