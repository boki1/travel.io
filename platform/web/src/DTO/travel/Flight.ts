import { Location } from './Location';

export interface Flight{
    departureAirportCode: string;
    arrivalAirportCode:string;
    price:number;
    currency:string;
    departureDateTime:string;
    arrivalDateTime:string;
    location: Location;
}