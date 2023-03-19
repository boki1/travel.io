import {VacationOfferDTO} from "./travel/VacationOfferDTO";
import {Location} from "./travel/Location"

export interface DestinationDTO{

    location: Location;
    vacationOffers: VacationOfferDTO[];
}