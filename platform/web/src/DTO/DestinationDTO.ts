import { VacationOfferDTO } from "./travel/VacationOfferDTO";
import { Location } from "./travel/Location";
import { Landmark } from "./travel/Landmark";

export interface DestinationDTO {
    location: Location;
    vacationOffers: VacationOfferDTO[];
    activities: string[];
    landmarks: Landmark[];
}