export interface Location {
    description: string;
    city: string;
    country: string;
    coordinates: {
        lat: number;
        lng: number;
    };
}