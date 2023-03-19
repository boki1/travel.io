import { Flight } from './Flight';
import { Hotel } from './Hotel';

export interface VacationOfferDTO {
    flight: Flight;
    hotel: Hotel;
}