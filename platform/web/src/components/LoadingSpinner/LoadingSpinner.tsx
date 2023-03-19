import {ClockLoader} from "react-spinners";
import './styles.css';
import './styles.css';
import {LoadingItemDTO} from "../../DTO/LoadingItemDTO";
import buenos_aires from '../../assets/loadingAssets/BuenosAires.png';
import florence from '../../assets/loadingAssets/Florence.png';
import madrid from '../../assets/loadingAssets/Madrid.png';
import plovdiv from '../../assets/loadingAssets/Plovdiv.png';
import seoul from '../../assets/loadingAssets/Seoul.png';
import Carousel from 'react-bootstrap/Carousel';


export default function LoadingSpinner() {
    const loadingItems: LoadingItemDTO[] = [
        {
            imageSource: buenos_aires,
            city: 'Buenos Aires',
            description: "The Argentina capital has traveled so much over time through its tangos and milongas, its writers, its most emblematic buildings and artists that it is difficult to imagine a life without having stepped on it.",
            suggestions: ["Plaza de Mayo", "Casa Rosada", "La Recoleta Cemetery", "Caminito Street Museum", "Catedral Metropolitana"]
        },
        {
            imageSource: florence,
            city: 'Florence',
            description: "Dubbed the birthplace of the Renaissance, it’s home to works by Leonardo Da Vinci and Michelangelo, plus beautiful attractions like the Ponte Vecchio and the Boboli Gardens.",
            suggestions: ["Cathedral of Santa Maria del Fiore and Piazza Duomo", "Uffizi Palace", "Santa Croce", "Palazzo Vecchio", "Ponte Vecchio"]
        },
        {
            imageSource: plovdiv,
            city: 'Plovdiv',
            description: "Bulgaria's Plovdiv remains relatively unknown to most travelers, but it's beginning to appear on more and more Balkan itineraries - and for good reason.",
            suggestions: ["The Ancient Amphitheater", "The Roman Stadium", "The Old Town", "Kapana Art District", "Dzhumaya Mosque"]
        },
        {
            imageSource: madrid,
            city: 'Madrid',
            description: "Madrid is one of the most fun european capitals, open and welcoming, and the perfect city to feel like a local.",
            suggestions: ["Museo Nacional del Prado", "Royal Palace", "Puerta del Sol", "Museo Nacional Centro de Arte Reina Sofía", "Buen Retiro Park"]
        },
        {
            imageSource: seoul,
            city: 'Seoul',
            description: "One of Asia’s great cities, and a technology-forward powerhouse, Seoul mixes the cutting-edge with the deeply traditional",
            suggestions: ["Gyeongbokgung Palace", "Gwanghwamun Gate", "N Seoul Tower", "Bongeunsa Temple", "Bukchon Hanok Traditional Village"]
        }
    ];


    return (
        <>
            <h1 className="titleLoading">While you are waiting your offer, you can check our trip suggestions</h1>
            <Carousel indicators={false} controls={false} interval={3000} >
                {
                    loadingItems.map((loadingItem, index) =>
                        <Carousel.Item key={index} >
                            <div className="card" style={{backgroundImage: `url(${loadingItem.imageSource})`}}>
                                <div className="inner">
                                    <div className="titleCard">{loadingItem.city}</div>
                                    <div className="descriptionCard">{loadingItem.description}</div>
                                    <time className="subtitle"><h3>Top 5 places to visit, while there:</h3>{loadingItem.suggestions.map((suggestion) =>
                                        <h3>{suggestion}</h3>)}</time>
                                </div>
                            </div>
                        </Carousel.Item>
                    )
                }
            </Carousel>
            <div className='spinLoader'>
                <ClockLoader size={120}/>
            </div>
        </>
    );
}