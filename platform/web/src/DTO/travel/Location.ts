export interface Location {
    description: string;
    city: string;
    country: string;
    coordinates: {
        latitude: number;
        longitude: number;
    };
}